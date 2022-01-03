package org.jesperancinha.plugins.omni.reporter.parsers

import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.CoverallsReport
import org.jesperancinha.plugins.omni.reporter.domain.SourceFile
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.parsers.OmniReportParser.Companion.messageDigester
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.jesperancinha.plugins.omni.reporter.repository.GitRepository
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.MessageDigest
import javax.xml.bind.JAXBContext
import javax.xml.stream.XMLInputFactory
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

private fun isBranch(it: Line) =
    it.mb != null && it.mb > 0 || it.cb != null && it.cb > 0

private val String.toFileDigest: String
    get() = messageDigester.digest(toByteArray())
        .joinToString(separator = "") { byte -> "%02x".format(byte) }
        .uppercase()


private fun List<Line?>.toCoverageArray(lines: Int): Array<Int?> = let {
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

class SourceCodeFile(projectBaseDir: File, packageName: String?, sourceFile: Sourcefile) :
    File(projectBaseDir, "${(packageName ?: "").replace("//", "/")}/${sourceFile.name}")

interface OmniReportParser<T> {
    fun parseSourceFile(inputStream: InputStream, compiledSourcesDirs: List<File>): CoverallsReport

    fun parseSourceFile(source: T, compiledSourcesDirs: List<File>): CoverallsReport

    companion object {
        val messageDigester: MessageDigest = MessageDigest.getInstance("MD5")
    }
}

abstract class OmniReporterParserImpl<T>(
    internal val token: String,
    internal val pipeline: Pipeline,
    internal val root: File,
    failOnUnknown: Boolean,
    internal val includeBranchCoverage: Boolean,
) : OmniReportParser<T> {

    val gitRepository = GitRepository(root)

    val failOnUnknownPredicate =
        if (failOnUnknown) { file: File -> if (!file.exists()) throw FileNotFoundException(file.absolutePath) else true }
        else { file: File ->
            if (!file.exists()) {
                logger.warn("File ${file.absolutePath} has not been found. Please activate flag `failOnUnknown` in your maven configuration if you want reporting to fail in these cases.")
                logger.warn("Files not found are not included in the complete coverage report. They are sometimes included in the report due to bugs from reporting frameworks and in those cases it is safe to ignore them")
            }
            file.exists()
        }

    val failOnUnknownPredicateFilePack =
        if (failOnUnknown) { foundSources: List<Pair<SourceCodeFile, Sourcefile>>, sourceFiles: List<Sourcefile> ->
            sourceFiles.filter { sourcefile ->
                foundSources.any { (foundSource, _) ->
                    foundSource.name != sourcefile.name
                }
            }.forEach { foundSource ->
                logger.warn("File ${foundSource.name} has not been found. Please activate flag `failOnUnknown` in your maven configuration if you want reporting to fail in these cases.")
                logger.warn("Files not found are not included in the complete coverage report. They are sometimes included in the report due to bugs from reporting frameworks and in those cases it is safe to ignore them")
            }
            false
        } else { _, _ ->
            true
        }

    companion object {
        private val logger = LoggerFactory.getLogger(OmniReportParser::class.java)
    }
}

class JacocoParser(
    token: String,
    pipeline: Pipeline,
    root: File,
    failOnUnknown: Boolean,
    includeBranchCoverage: Boolean,
    val useCoverallsCount:Boolean
) :
    OmniReporterParserImpl<Report>(token, pipeline, root, failOnUnknown, includeBranchCoverage) {

    internal var coverallsReport: CoverallsReport? = null

    private var coverallsSources = mutableMapOf<String, SourceFile>()

    internal fun parseInputStream(inputStream: InputStream): Report {
        val jaxbContext = JAXBContext.newInstance(Report::class.java)
        val xmlInputFactory = XMLInputFactory.newFactory()
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false)
        val xmlStreamReader = xmlInputFactory.createXMLStreamReader(InputStreamReader(inputStream))
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(xmlStreamReader) as Report
    }

    override fun parseSourceFile(inputStream: InputStream, compiledSourcesDirs: List<File>): CoverallsReport =
        parseSourceFile(
            parseInputStream(inputStream), compiledSourcesDirs
        )

    override fun parseSourceFile(source: Report, compiledSourcesDirs: List<File>): CoverallsReport = source.packages
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
            val coverage = sourceFile.lines.toCoverageArray(lines)
            val branchCoverage = sourceFile.lines.toBranchCoverageArray
            if (coverage.isEmpty()) {
                null
            } else {
                SourceFile(
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
                ((coverallsSources[source.name] mergeTo source).also {
                    coverallsSources[source.name] = it
                })
            }
            nonExisting.forEach { coverallsSources[it.name] = it }
            if (coverallsReport == null) {
                coverallsReport = CoverallsReport(
                    repoToken = token,
                    serviceName = pipeline.serviceName,
                    serviceNumber= if(useCoverallsCount) null else pipeline.serviceNumber,
                    serviceJobId =  if(useCoverallsCount) null else pipeline.serviceJobId,
                    sourceFiles = sourceFiles.toMutableList(),
                    git = gitRepository.git
                )
            } else {
                coverallsReport?.sourceFiles?.clear()
                coverallsReport?.sourceFiles?.addAll(coverallsSources.values)
            }

            coverallsReport ?: throw ProjectDirectoryNotFoundException()
        }
}

private infix fun SourceFile?.mergeTo(source: SourceFile): SourceFile {
    val nextSize = max(source.coverage.size, this?.coverage?.size ?: throw NullSourceFileException())
    val newCoverage = arrayOfNulls<Int>(nextSize)
    source.coverage.forEachIndexed { index, value -> newCoverage[index] = value }
    this.coverage.forEachIndexed { index, value ->
        newCoverage[index] = newCoverage[index]?.let { it + (value ?: 0) } ?: value
    }
    return source.copy(coverage = newCoverage)
}

class NullSourceFileException : RuntimeException()