package org.jesperancinha.plugins.omni.reporter.domain

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jesperancinha.plugins.omni.reporter.parsers.JacocoParser
import org.junit.jupiter.api.Test

internal class DomainTest {
    @Test
    fun `should parse JacocoReport`() {
        val inputStream = javaClass.getResourceAsStream("/jacoco.xml")
        inputStream.shouldNotBeNull()
        val readValue = JacocoParser(inputStream).parseInputStream()
        readValue.shouldNotBeNull()
        readValue.name shouldBe "Advanced Library Management Reactive MVC"
        readValue.packages.forEach {
            it.name.shouldNotBeNull()
            it.sourcefile.name.shouldNotBeNull()
        }
    }
}