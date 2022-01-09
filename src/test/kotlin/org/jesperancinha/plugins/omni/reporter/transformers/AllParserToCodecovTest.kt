package org.jesperancinha.plugins.omni.reporter.transformers

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldHaveLength
import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.GITLAB
import org.jesperancinha.plugins.omni.reporter.pipelines.GitLabPipeline
import org.jesperancinha.plugins.omni.reporter.utils.Utils.Companion.root
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class AllParserToCodecovTest {

    @Test
    fun `should return String without parsing`() {
        val resourceAsStream = javaClass.getResourceAsStream("/jacoco.xml")
        resourceAsStream.shouldNotBeNull()

        val parseInput = AllParserToCodecov(
            token = "token",
            pipeline = GitLabPipeline(),
            root = root,
        ).parseInput(
            resourceAsStream
        )

        parseInput.shouldNotBeNull()
        parseInput.shouldHaveLength(20810)
    }
}