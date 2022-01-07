package org.jesperancinha.plugins.omni.reporter.domain

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import org.jesperancinha.plugins.omni.reporter.parsers.camelCaseJsonObjectMapper
import org.jesperancinha.plugins.omni.reporter.parsers.readJsonValue
import org.jesperancinha.plugins.omni.reporter.parsers.readXmlValue
import org.junit.jupiter.api.Test

internal class CodacySourceFileTest {

    @Test
    fun `should parse Codacy file correctly`() {
        val inputStream = javaClass.getResourceAsStream("/codacy-coverage.json")
        inputStream.shouldNotBeNull()
        val codacyReport = camelCaseJsonObjectMapper.readValue<CodacyReport>(inputStream)
        codacyReport.total.shouldBe(34)
        codacyReport.fileReports.shouldNotBeEmpty()
        codacyReport.fileReports.forEach { report ->
            report.shouldNotBeNull()
            report.filename.shouldNotBeNull()
            report.total.shouldNotBeNull()
            report.coverage.shouldNotBeNull()
            report.coverage.entries.shouldHaveAtLeastSize(1)
        }
    }
}