package org.jesperancinha.plugins.omni.reporter.transformers

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.parsers.readXmlValue
import org.jesperancinha.plugins.omni.reporter.pipelines.GitLabPipeline
import org.jesperancinha.plugins.omni.reporter.utils.Utils.Companion.root
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.io.path.toPath

internal class AllParserToCodecovTest {

    @Test
    @Disabled
    fun `should return String without parsing`() {
        val resourceAsStream = javaClass.getResourceAsStream("/jacoco-for-codacy.xml")
        resourceAsStream.shouldNotBeNull()
        val resource = javaClass.getResource("/src/main/kotlin")
        resource.shouldNotBeNull()

        val parseInput = AllParserToCodecov(
            token = "token",
            pipeline = GitLabPipeline(),
            root = root,
        ).parseInput(
            resourceAsStream,
            listOf(resource.toURI().toPath().toFile())
        )

        parseInput.shouldNotBeNull()
        parseInput.shouldHaveLength(147509)

        val report2 = readXmlValue<Report>(parseInput.byteInputStream())
        report2.shouldNotBeNull()
        val packages = report2.packages
        packages.shouldNotBeEmpty()
        packages[0].name shouldBe "src/main/kotlin/org/jesperancinha/plugins/omni/reporter/domain"
    }
}