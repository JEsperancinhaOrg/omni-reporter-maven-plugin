package org.jesperancinha.plugins.omni.reporter.domain

import io.kotest.matchers.nulls.shouldNotBeNull
import org.jesperancinha.plugins.omni.reporter.domain.JsonMappingConfiguration.Companion.objectMapper
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline
import org.jesperancinha.plugins.omni.reporter.transformers.JacocoParser
import org.jesperancinha.plugins.omni.reporter.utils.Utils
import org.jesperancinha.plugins.omni.reporter.utils.Utils.Companion.root
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class CoverallsClientTest {

    @Test
    @Disabled
    fun `should submit test file to coveralls`() {
        val coverallsClient = CoverallsClient("https://coveralls.io/api/v1/jobs", "token")
        val inputStream = javaClass.getResourceAsStream("/jacoco.xml")
        inputStream.shouldNotBeNull()
        val jacocoParser = JacocoParser("token", LocalPipeline(), root,
            failOnUnknown = false,
            includeBranchCoverage = false,
            useCoverallsCount = false
        )

        val report = jacocoParser.parseSourceFile(inputStream, listOf(root))
        logger.info(objectMapper.writeValueAsString(report))

        coverallsClient.submit(report)
    }

    companion object {
        val logger = LoggerFactory.getLogger(CoverallsClientTest::class.java)
    }
}