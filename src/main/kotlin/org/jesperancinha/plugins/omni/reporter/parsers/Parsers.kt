package org.jesperancinha.plugins.omni.reporter.parsers

import org.jesperancinha.plugins.omni.reporter.domain.SourceFile
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.parsers.OmniReportParser.Companion.messageDigester
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.security.MessageDigest
import javax.xml.bind.JAXBContext
import javax.xml.stream.XMLInputFactory


private val File.toFileDigest: String
    get() = bufferedReader().use { it.readText() }.let { text ->
        messageDigester.digest(text.toByteArray()).joinToString(separator = "") { byte -> "%02x".format(byte) }
    }


private val MutableList<Line>.toCoverageArray: Array<Int?>
    get() = let {
        val coverageArray = Array<Int?>(map { it.nr ?: 0 }.maxOf { it }) { null }
        forEach { line ->
            coverageArray[line.nr - 1] = line.ci
        }
        coverageArray
    }

interface OmniReportParser<T> {
    fun parseInputStream(): T

    fun parseSourceFile(source: T): List<SourceFile>

    companion object {
        val messageDigester: MessageDigest = MessageDigest.getInstance("MD5")
    }
}

abstract class OmniReporterParserImpl<T>(internal val inputStream: InputStream, internal val projectBaseDir: File) :
    OmniReportParser<T> {
    fun digestFile(packageName: String?, sourceFile: Sourcefile): String =
        File(projectBaseDir, "${(packageName ?: "").replace("//","/")}/${sourceFile.name}").toFileDigest

}

class JacocoParser(inputStream: InputStream, projectBaseDir: File) :
    OmniReporterParserImpl<Report>(inputStream, projectBaseDir) {
    override fun parseInputStream(): Report {
        val jaxbContext = JAXBContext.newInstance(Report::class.java)
        val xmlInputFactory = XMLInputFactory.newFactory()
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false)
        val xmlStreamReader = xmlInputFactory.createXMLStreamReader(InputStreamReader(inputStream))
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(xmlStreamReader) as Report
    }

    override fun parseSourceFile(source: Report): List<SourceFile> = source.packages
        .map { it.name to it.sourcefile }
        .map { (packageName, sourceFile) ->
            SourceFile(
                name = sourceFile.name,
                coverage = sourceFile.lines.toCoverageArray,
                sourceDigest = digestFile(packageName, sourceFile)
            )
        }
}