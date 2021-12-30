package org.jesperancinha.plugins.omni.reporter.parsers

import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import java.io.InputStream
import java.io.InputStreamReader
import javax.xml.bind.JAXBContext
import javax.xml.stream.XMLInputFactory


interface OmniReportParser<T> {
    fun parseInputStream(): T
}

class JacocoParser(val inputStream: InputStream) : OmniReportParser<Report> {
    override fun parseInputStream(): Report {
        val jaxbContext = JAXBContext.newInstance(Report::class.java)
        val xmlInputFactory = XMLInputFactory.newFactory()
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false)
        val xmlStreamReader = xmlInputFactory.createXMLStreamReader(InputStreamReader(inputStream))
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(xmlStreamReader) as Report
    }
}