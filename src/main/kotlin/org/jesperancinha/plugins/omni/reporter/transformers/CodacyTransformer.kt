package org.jesperancinha.plugins.omni.reporter.transformers

import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.CodacyFileReport
import org.jesperancinha.plugins.omni.reporter.domain.CodacyReport
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.parsers.Language
import org.jesperancinha.plugins.omni.reporter.parsers.readXmlValue
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream


val List<Line?>.toCodacyCoverage: MutableMap<String, Int>
    get() = if (isNotEmpty()) {
        filterNotNull().associate { line -> line.nr.toString() to line.ci }.toMutableMap()
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
        readXmlValue<Report>(input).packages
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
                if (coverage.isEmpty() || (sourceFile.name == null ||
                            !sourceFile.name.endsWith(language.ext))
                ) {
                    null
                } else {
                    CodacyFileReport(
                        filename = sourceCodeFile.toRelativeString(root),
                        total = coverage.size,
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
                        total = fileReports.size,
                        fileReports = fileReports.toTypedArray()
                    )
                } else {
                    codacyReport = codacyReport?.copy(fileReports = codacySources.values.toTypedArray())
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
        source.coverage[line] = cov + origCov
    }
    return source
}