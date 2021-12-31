package org.jesperancinha.plugins.omni.reporter.domain

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jesperancinha.plugins.omni.reporter.domain.JsonMappingConfiguration.Companion.objectMapper
import org.jesperancinha.plugins.omni.reporter.parsers.JacocoParser
import org.junit.jupiter.api.Test

internal class DomainTest {
    @Test
    fun `should parse basic JacocoReport`() {
        val inputStream = javaClass.getResourceAsStream("/jacoco.xml")
        inputStream.shouldNotBeNull()
        val readValue = JacocoParser(inputStream).parseInputStream()
        readValue.shouldNotBeNull()
        readValue.name shouldBe "Advanced Library Management Reactive MVC"
        readValue.packages .forEach {
            it.name.shouldNotBeNull()
            it.sourcefile.name.shouldNotBeNull()
        }
    }

    @Test
    fun `should generate snake case source object`() {
        val sourceFile = SourceFile("name", "sourceDigest")
        val writeValueAsString = objectMapper.writeValueAsString(sourceFile)
        writeValueAsString.shouldBe("{\"name\":\"name\",\"source_digest\":\"sourceDigest\",\"coverage\":[],\"branches\":null,\"source\":null}")
    }

    @Test
    fun `should generate snake case coveralls object`() {
        val sourceFile = CoverallsReport(
            "repoToken", "serviceName",
            listOf(SourceFile("name", "sourceDigest")),
            Git(
                Head("id", "authorName", "authorEmail", "committerName", "committerEmail"),
                "banch", listOf(Remote("remote", "url"))
            )
        )
        val writeValueAsString = objectMapper.writeValueAsString(sourceFile)
        writeValueAsString.shouldBe("{\"repo_token\":\"repoToken\",\"service_name\":\"serviceName\",\"source_files\":[{\"name\":\"name\",\"source_digest\":\"sourceDigest\",\"coverage\":[],\"branches\":null,\"source\":null}],\"git\":{\"head\":{\"id\":\"id\",\"author_name\":\"authorName\",\"author_email\":\"authorEmail\",\"committer_name\":\"committerName\",\"committer_email\":\"committerEmail\",\"message\":null},\"branch\":\"banch\",\"remotes\":[{\"name\":\"remote\",\"url\":\"url\"}]},\"service_number\":null,\"service_job_id\":null,\"service_pull_request\":null,\"parallel\":null,\"flag_name\":null,\"commit_sha\":null,\"run_at\":null}")
    }
}