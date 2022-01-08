package org.jesperancinha.plugins.omni.reporter.transformers

import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.CodacyFileReport
import org.jesperancinha.plugins.omni.reporter.domain.CodacyReport
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.readJacocoPackages
import org.jesperancinha.plugins.omni.reporter.parsers.Language
import org.jesperancinha.plugins.omni.reporter.parsers.readXmlValue
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream


private val Sourcefile.calculateLinePercentage: Int
    get() = counters.first { it.type == "LINE" }.run { (covered * 100) / (covered + missed) }

private val List<CodacyFileReport>.calculateTotalPercentage: Int
    get() = sumOf { it.coverage.values.sum() } * 100 / sumOf { it.total }

val List<Line?>.toCodacyCoverage: MutableMap<String, Int>
    get() = if (isNotEmpty()) {
        filterNotNull().associate { line -> line.nr.toString() to if (line.ci > 0) 1 else 0 }.toMutableMap()
    } else {
        mutableMapOf()
    }

/**
 * Created by jofisaes on 05/01/2022
 */
class JacocoParserToCodacy(
    token: String,
    pipeline: Pipeline,
    root: File,
    failOnUnknown: Boolean,
    private val language: Language
) : OmniReporterParserImpl<InputStream, CodacyReport>(token, pipeline, root) {

    internal var codacyReport: CodacyReport? = null

    private var codacySources = mutableMapOf<String, CodacyFileReport>()

    val failOnUnknownPredicate = if (failOnUnknown) { file: File ->
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

    override fun parseInput(input: InputStream, compiledSourcesDirs: List<File>): CodacyReport =
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
                val coverage = sourceFile.lines.toCodacyCoverage
                val sourceCodeText = sourceCodeFile.bufferedReader().use { it.readText() }
                val lines = sourceCodeText.split("\n").size
                if (coverage.isEmpty() || !sourceFile.name.endsWith(language.ext)) {
                    null
                } else {
                    CodacyFileReport(
                        filename = "${sourceCodeFile.packageName}/${sourceFile.name}".replace("//", "/"),
                        total = sourceFile.calculateLinePercentage,
                        coverage = coverage
                    )
                }
            }
            .filterNotNull()
            .toList()
            .let { fileReports ->
                val keys = codacySources.keys
                val (existing, nonExisting) = fileReports.partition { source -> keys.contains(source.filename) }
                existing.forEach { source ->
                    ((codacySources[source.filename] mergeCodacySourceTo source).also {
                        codacySources[source.filename] = it
                    })
                }
                nonExisting.forEach { codacySources[it.filename] = it }
                if (codacyReport == null) {
                    codacyReport = CodacyReport(
                        total = fileReports.calculateTotalPercentage,
                        fileReports = fileReports.toTypedArray()
                    )
                } else {
                    val fileReportValues = codacySources.values.toList()
                    val fileReportsNew = fileReportValues.toTypedArray()
                    codacyReport =
                        codacyReport?.copy(
                            total = fileReportValues.calculateTotalPercentage,
                            fileReports = fileReportsNew
                        )
                }

                codacyReport ?: throw ProjectDirectoryNotFoundException()
            }


    companion object {
        private val logger = LoggerFactory.getLogger(JacocoParserToCoveralls::class.java)
    }
}

private infix fun CodacyFileReport?.mergeCodacySourceTo(source: CodacyFileReport): CodacyFileReport {
    this?.coverage?.entries?.forEach { (line, cov) ->
        val origCov = source.coverage[line] ?: 0
        source.coverage[line] = cov or origCov
    }
    return source
}