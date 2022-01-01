package org.jesperancinha.plugins.omni.reporter.domain

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jesperancinha.plugins.omni.reporter.domain.JsonMappingConfiguration.Companion.objectMapper
import org.jesperancinha.plugins.omni.reporter.parsers.JacocoParser
import org.jesperancinha.plugins.omni.reporter.pipelines.GitHubPipeline
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline
import org.jesperancinha.plugins.omni.reporter.utils.Utils.Companion.root
import org.junit.jupiter.api.Test

internal class DomainTest {
    @Test
    fun `should parse basic JacocoReport`() {
        val inputStream = javaClass.getResourceAsStream("/jacoco.xml")
        inputStream.shouldNotBeNull()
        val readValue = JacocoParser("token", LocalPipeline(System.getenv()), root).parseInputStream(inputStream)
        readValue.shouldNotBeNull()
        readValue.name shouldBe "Advanced Library Management Reactive MVC"
        readValue.packages.forEach {
            it.name.shouldNotBeNull()
            it.sourcefile.name.shouldNotBeNull()
        }
    }

    @Test
    fun `should parse basic JacocoReport 1`() {
        val inputStream = javaClass.getResourceAsStream("/jacoco1.xml")
        inputStream.shouldNotBeNull()
        val readValue = JacocoParser("token", LocalPipeline(System.getenv()), root).parseInputStream(inputStream)
        readValue.shouldNotBeNull()
        readValue.name shouldBe "Advanced Library Management Reactive MVC"
        readValue.packages.forEach {
            it.name.shouldNotBeNull()
            it.sourcefile.name.shouldNotBeNull()
        }
    }

    @Test
    fun `should generate snake case source object`() {
        val sourceFile = SourceFile("name", "sourceDigest")
        val writeValueAsString = objectMapper.writeValueAsString(sourceFile)
        writeValueAsString.shouldBe("{\"name\":\"name\",\"source_digest\":\"sourceDigest\"}")
    }

    @Test
    fun `should generate snake case coveralls object`() {
        val sourceFile = CoverallsReport(
            "repoToken", "serviceName",
            mutableListOf(SourceFile("name", "sourceDigest")),
            Git(
                Head("id", "authorName", "authorEmail", "committerName", "committerEmail"),
                "branch", listOf(Remote("remote", "url"))
            )
        )
        val writeValueAsString = objectMapper.writeValueAsString(sourceFile)
        writeValueAsString.shouldBe("{\"repo_token\":\"repoToken\",\"service_name\":\"serviceName\",\"source_files\":[{\"name\":\"name\",\"source_digest\":\"sourceDigest\"}],\"git\":{\"head\":{\"id\":\"id\",\"author_name\":\"authorName\",\"author_email\":\"authorEmail\",\"committer_name\":\"committerName\",\"committer_email\":\"committerEmail\",\"message\":null},\"branch\":\"branch\",\"remotes\":[{\"name\":\"remote\",\"url\":\"url\"}]}}")
    }
}