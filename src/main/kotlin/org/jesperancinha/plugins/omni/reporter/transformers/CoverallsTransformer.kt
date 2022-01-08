package org.jesperancinha.plugins.omni.reporter.transformers

import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.CoverallsReport
import org.jesperancinha.plugins.omni.reporter.domain.CoverallsSourceFile
import org.jesperancinha.plugins.omni.reporter.domain.isBranch
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.readJacocoPackages
import org.jesperancinha.plugins.omni.reporter.parsers.toFileDigest
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import kotlin.math.max


private val List<Line>.toBranchCoverageArray: Array<Int?>
    get() = let {
        val branchesArray = filter { isBranch(it) }
        val coverageArray = Array<Int?>(branchesArray.size * 4) { null }
        branchesArray.forEachIndexed { lineNumber, line ->
            coverageArray[lineNumber] = line.nr
            coverageArray[lineNumber + 1] = line.mb + line.cb
            coverageArray[lineNumber + 2] = line.cb
            coverageArray[lineNumber + 3] = line.ci
        }
        coverageArray
    }

private fun List<Line?>.toCoverallsCoverage(lines: Int): Array<Int?> = let {
    if (isNotEmpty()) {
        val calculatedLength = map { it?.nr ?: 0 }.maxOf { it }
        val coverageArray = Array<Int?>(max(lines, calculatedLength)) { null }
        forEach { line ->
            line?.let { coverageArray[line.nr - 1] = line.ci }
        }
        coverageArray
    } else {
        emptyArray()
    }
}

class SourceCodeFile(projectBaseDir: File, val packageName: String?, sourceFile: Sourcefile) :
    File(projectBaseDir, "${(packageName ?: "").replace("//", "/")}/${sourceFile.name}")


class JacocoParserToCoveralls(
    token: String,
    pipeline: Pipeline,
    root: File,
    failOnUnknown: Boolean,
    includeBranchCoverage: Boolean,
    val useCoverallsCount: Boolean
) :
    OmniReporterParserImpl<InputStream, CoverallsReport>(token, pipeline, root, includeBranchCoverage) {

    internal var coverallsReport: CoverallsReport? = null

    private var coverallsSources = mutableMapOf<String, CoverallsSourceFile>()

    val failOnUnknownPredicate =
        if (failOnUnknown) { file: File ->
            if (!file.exists()) throw FileNotFoundException(file.absolutePath) else true
        } else { file: File ->
            if (!file.exists()) {
                logger.warn("File ${file.absolutePath} has not been found. Please activate flag `failOnUnknown` in your maven configuration if you want reporting to fail in these cases.")
                logger.warn("Files not found are not included in the complete coverage report. They are sometimes included in the report due to bugs from reporting frameworks and in those cases it is safe to ignore them")
            }
            file.exists()
        }

    /**
     * Comparison is based on the size. If there is a missmatch then the size is different.
     */
    val failOnUnknownPredicateFilePack =
        { foundSources: List<Pair<SourceCodeFile, Sourcefile>>, sourceFiles: List<Sourcefile> ->
            val jacocoSourcesFound = foundSources.map { (_, foundJacocoFile) -> foundJacocoFile }
            val sourceFilesNotFound = sourceFiles.filter { !jacocoSourcesFound.contains(it) }
            sourceFilesNotFound
                .forEach { foundSource ->
                    logger.warn("File ${foundSource.name} has not been found. Please activate flag `failOnUnknown` in your maven configuration if you want reporting to fail in these cases.")
                    logger.warn("Files not found are not included in the complete coverage report. They are sometimes included in the report due to bugs from reporting frameworks and in those cases it is safe to ignore them")
                }
            if (failOnUnknown) {
                logger.error("Stopping build due to one or more files not being found")
                throw FileNotFoundException()
            }
            sourceFilesNotFound.isEmpty()
        }


    override fun parseInput(input: InputStream, compiledSourcesDirs: List<File>): CoverallsReport =
        input.readJacocoPackages
            .asSequence()
            .map { it.name to it.sourcefiles }
            .flatMap { (packageName, sourceFiles) ->
                val foundSources = sourceFiles.map {
                    compiledSourcesDirs.map { compiledSourcesDir ->
                        SourceCodeFile(compiledSourcesDir, packageName, it)
                    }.filter { it.exists() }.map { sourceCodeFile -> sourceCodeFile to it }
                }.flatten()
                if (foundSources.size != sourceFiles.size) {
                    failOnUnknownPredicateFilePack(foundSources, sourceFiles)
                }
                foundSources
            }
            .filter { (sourceCodeFile) -> failOnUnknownPredicate(sourceCodeFile) }
            .map { (sourceCodeFile, sourceFile) ->
                val sourceCodeText = sourceCodeFile.bufferedReader().use { it.readText() }
                val lines = sourceCodeText.split("\n").size
                val coverage = sourceFile.lines.toCoverallsCoverage(lines)
                val branchCoverage = sourceFile.lines.toBranchCoverageArray
                if (coverage.isEmpty()) {
                    null
                } else {
                    CoverallsSourceFile(
                        name = sourceCodeFile.toRelativeString(root),
                        coverage = coverage,
                        branches = if (includeBranchCoverage) branchCoverage else emptyArray(),
                        sourceDigest = sourceCodeText.toFileDigest,
                    )
                }
            }
            .filterNotNull()
            .toList()
            .let { sourceFiles ->
                val keys = coverallsSources.keys
                val (existing, nonExisting) = sourceFiles.partition { source -> keys.contains(source.name) }
                existing.forEach { source ->
                    ((coverallsSources[source.name] mergeCoverallsSourceTo source).also {
                        coverallsSources[source.name] = it
                    })
                }
                nonExisting.forEach { coverallsSources[it.name] = it }
                if (coverallsReport == null) {
                    coverallsReport = CoverallsReport(
                        repoToken = token,
                        serviceName = pipeline.serviceName,
                        serviceNumber = if (useCoverallsCount) null else pipeline.serviceNumber,
                        serviceJobId = if (useCoverallsCount) null else pipeline.serviceJobId,
                        sourceFiles = sourceFiles.toMutableList(),
                        git = gitRepository.git
                    )
                } else {
                    coverallsReport?.sourceFiles?.clear()
                    coverallsReport?.sourceFiles?.addAll(coverallsSources.values)
                }

                coverallsReport ?: throw ProjectDirectoryNotFoundException()
            }

    companion object {
        private val logger = LoggerFactory.getLogger(JacocoParserToCoveralls::class.java)
    }
}

private infix fun CoverallsSourceFile?.mergeCoverallsSourceTo(source: CoverallsSourceFile): CoverallsSourceFile {
    val nextSize = max(source.coverage.size, this?.coverage?.size ?: throw NullSourceFileException())
    val newCoverage = arrayOfNulls<Int>(nextSize)
    source.coverage.forEachIndexed { index, value -> newCoverage[index] = value }
    this.coverage.forEachIndexed { index, value ->
        newCoverage[index] = newCoverage[index]?.let { it + (value ?: 0) } ?: value
    }
    return source.copy(coverage = newCoverage)
}

class NullSourceFileException : RuntimeException()