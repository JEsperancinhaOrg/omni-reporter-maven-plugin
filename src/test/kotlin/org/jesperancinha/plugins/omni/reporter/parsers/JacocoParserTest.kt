package org.jesperancinha.plugins.omni.reporter.parsers

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Package
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline
import org.jesperancinha.plugins.omni.reporter.utils.Utils.Companion.root
import org.junit.jupiter.api.Test

internal class JacocoParserTest {
    private val jacocoParser = JacocoParser(
        "token",
        LocalPipeline(),
        root,
        false
    )

    @Test
    fun parseSourceFile() {
        val report = Report()
        val pack = Package()
        val pack2 = Package()

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

        val sourcefile2 = Sourcefile()
        val element21 = Line()
        element21.nr = 1
        element21.ci = 10
        val element22 = Line()
        element22.nr = 2
        element22.ci = 11
        element22.cb = 3
        element22.mb = 2
        sourcefile2.name = "Racoons.kt"
        sourcefile2.lines.add(element21)
        sourcefile2.lines.add(element22)

        pack.name = "/"
        pack.sourcefiles.add(sourcefile)

        pack2.name = "/"
        pack2.sourcefiles.add(sourcefile)

        report.packages.add(pack)
        report.packages.add(pack2)
        jacocoParser.parseSourceFile(report, listOf(root))
        jacocoParser.parseSourceFile(report, listOf(root))
        val sourceFiles = jacocoParser.parseSourceFile(report, listOf(root)).sourceFiles

        sourceFiles.shouldNotBeNull()
        sourceFiles.shouldHaveSize(1)
        val sourceFile = sourceFiles[0]
        sourceFile.name.shouldNotBeNull()
        sourceFile.sourceDigest.shouldNotBeNull()
        sourceFile.coverage shouldBe arrayOf(50, 55, null, null, null)
//        sourceFile.branches shouldBe arrayOf(2, 5, 3, 11)
//        sourceFile.source shouldBe File(root, "Racoons.kt").bufferedReader().use { it.readText() }
    }
}