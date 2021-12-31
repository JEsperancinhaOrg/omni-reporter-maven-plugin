package org.jesperancinha.plugins.omni.reporter.parsers

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Package
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.pipelines.GitHubPipeline
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline
import org.jesperancinha.plugins.omni.reporter.utils.Utils.Companion.root
import org.junit.jupiter.api.Test
import java.io.File

internal class JacocoParserTest {
    private val jacocoParser = JacocoParser(
        "token",
        LocalPipeline(System.getenv()),
        root
    )

    @Test
    fun parseSourceFile() {
        val report = Report()
        val pack = Package()
        val sourcefile = Sourcefile()
        val element = Line()
        element.nr = 1
        element.ci = 10
        val element2 = Line()
        element2.nr = 2
        element2.ci = 11
        element2.cb = 3
        element2.mb = 2
        sourcefile.name = "Racoons.kt"
        sourcefile.lines.add(element)
        sourcefile.lines.add(element2)
        pack.name = "/"
        pack.sourcefile = sourcefile

        report.packages.add(pack)
        val sourceFiles = jacocoParser.parseSourceFile(report, root).sourceFiles

        sourceFiles.shouldNotBeNull()
        sourceFiles.shouldHaveSize(1)
        val sourceFile = sourceFiles[0]
        sourceFile.name.shouldNotBeNull()
        sourceFile.sourceDigest.shouldNotBeNull()
        sourceFile.coverage shouldBe arrayOf(10, 11)
        sourceFile.branches shouldBe arrayOf(2, 5, 3, 11)
        sourceFile.source shouldBe File(root, "Racoons.kt").bufferedReader().use { it.readText() }
    }
}