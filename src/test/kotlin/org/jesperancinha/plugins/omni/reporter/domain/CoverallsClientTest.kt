package org.jesperancinha.plugins.omni.reporter.domain

import io.kotest.matchers.nulls.shouldNotBeNull
import org.jesperancinha.plugins.omni.reporter.parsers.JacocoParser
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline
import org.jesperancinha.plugins.omni.reporter.utils.Utils
import org.jesperancinha.plugins.omni.reporter.utils.Utils.Companion.root
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class CoverallsClientTest {

    @Test
    @Disabled
    fun `should submit test file to coveralls`() {
        val coverallsClient = CoverallsClient("https://coveralls.io/api/v1/jobs")
        val inputStream = javaClass.getResourceAsStream("/jacoco.xml")
        inputStream.shouldNotBeNull()
        val jacocoParser = JacocoParser("test", LocalPipeline(System.getenv()), Utils.root)
        val readValue = jacocoParser.parseInputStream(inputStream)

        val report = jacocoParser.parseSourceFile(readValue, root)
        coverallsClient.submit(report)
    }
}