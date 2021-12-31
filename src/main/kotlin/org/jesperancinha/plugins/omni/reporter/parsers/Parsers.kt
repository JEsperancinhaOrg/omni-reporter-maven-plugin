package org.jesperancinha.plugins.omni.reporter.parsers

import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.CoverallsReport
import org.jesperancinha.plugins.omni.reporter.domain.SourceFile
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.parsers.OmniReportParser.Companion.messageDigester
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.security.MessageDigest
import javax.xml.bind.JAXBContext
import javax.xml.stream.XMLInputFactory


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
    get() = messageDigester.digest(toByteArray()).joinToString(separator = "") { byte -> "%02x".format(byte) }


private val List<Line>.toCoverageArray: Array<Int?>
    get() = let {
        val coverageArray = Array<Int?>(map { it.nr ?: 0 }.maxOf { it }) { null }
        forEach { line ->
            coverageArray[line.nr - 1] = line.ci
        }
        coverageArray
    }

class SourceCodeFile(projectBaseDir: File, packageName: String?, sourceFile: Sourcefile) :
    File(projectBaseDir, "${(packageName ?: "").replace("//", "/")}/${sourceFile.name}")

interface OmniReportParser<T> {
    fun parseSourceFile(inputStream: InputStream, projectBaseDir: File): CoverallsReport

    fun parseSourceFile(source: T, projectBaseDir: File): CoverallsReport

    companion object {
        val messageDigester: MessageDigest = MessageDigest.getInstance("MD5")
    }
}

abstract class OmniReporterParserImpl<T>(
    internal val token: String,
    internal val pipeline: Pipeline,
    internal val root: File?,
) :
    OmniReportParser<T> {
}

class JacocoParser(token: String, pipeline: Pipeline, root: File) :
    OmniReporterParserImpl<Report>(token, pipeline, root) {

    private var coverallsReport: CoverallsReport? = null

    internal fun parseInputStream(inputStream: InputStream): Report {
        val jaxbContext = JAXBContext.newInstance(Report::class.java)
        val xmlInputFactory = XMLInputFactory.newFactory()
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false)
        val xmlStreamReader = xmlInputFactory.createXMLStreamReader(InputStreamReader(inputStream))
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(xmlStreamReader) as Report
    }

    override fun parseSourceFile(inputStream: InputStream, projectBaseDir: File): CoverallsReport = parseSourceFile(
        parseInputStream(inputStream), projectBaseDir
    )

    override fun parseSourceFile(source: Report, projectBaseDir: File): CoverallsReport = source.packages
        .map { it.name to it.sourcefile }
        .map { (packageName, sourceFile) ->
            val sourceCodeFile = SourceCodeFile(projectBaseDir, packageName, sourceFile)
            val sourceCodeText = sourceCodeFile.bufferedReader().use { it.readText() }
            SourceFile(
                name = sourceCodeFile.absolutePath.replace(
                    root?.absolutePath ?: throw ProjectDirectoryNotFoundException(), ""
                ),
                coverage = sourceFile.lines.toCoverageArray,
                sourceDigest = sourceCodeText.toFileDigest,
                branches = sourceFile.lines.toBranchCoverageArray,
                source = sourceCodeText
            )

        }.let {
            if (coverallsReport == null) {
                coverallsReport = CoverallsReport(
                    repoToken = token,
                    serviceName = pipeline.serviceName,
                    sourceFiles = it.toMutableList(),
                    serviceJobId = pipeline.serviceJobId
                )
            } else {

                coverallsReport?.sourceFiles?.addAll(it)
            }

            coverallsReport ?: throw ProjectDirectoryNotFoundException()
        }
}