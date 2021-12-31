package org.jesperancinha.plugins.omni.reporter.parsers

import org.jesperancinha.plugins.omni.reporter.domain.SourceFile
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import java.io.InputStream
import java.io.InputStreamReader
import javax.xml.bind.JAXBContext
import javax.xml.stream.XMLInputFactory


private val MutableList<Line>.toCoverageArray: Array<Int?>
    get() = let {
        val coverageArray = Array<Int?>(map { it.nr ?: 0 }.maxOf { it }) { null }
        forEach { line ->
            coverageArray[line.nr] = line.ci
        }
        coverageArray
    }

interface OmniReportParser<T> {
    fun parseInputStream(): T

    fun parseSourceFile(source: T): List<SourceFile>
}

class JacocoParser(private val inputStream: InputStream) : OmniReportParser<Report> {
    override fun parseInputStream(): Report {
        val jaxbContext = JAXBContext.newInstance(Report::class.java)
        val xmlInputFactory = XMLInputFactory.newFactory()
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false)
        val xmlStreamReader = xmlInputFactory.createXMLStreamReader(InputStreamReader(inputStream))
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(xmlStreamReader) as Report
    }

    override fun parseSourceFile(source: Report): List<SourceFile> {
        val sourceFile = source.packages
            .map { it.name to it.sourcefile }
            .map { (packageName, sourceFile) ->
                SourceFile(
                    name = sourceFile.name,
                    coverage = sourceFile.lines.toCoverageArray,
                    sourceDigest = "$packageName/${sourceFile.name}",
                )
            }

        TODO("Not yet implemented")
    }

}