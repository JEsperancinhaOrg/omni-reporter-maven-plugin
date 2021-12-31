package org.jesperancinha.plugins.omni.reporter.parsers

import io.kotest.matchers.nulls.shouldNotBeNull
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Package
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Sourcefile
import org.jesperancinha.plugins.omni.reporter.utils.Utils.Companion.root
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

internal class JacocoParserTest {
    private val jacocoParser = JacocoParser(
        "test".byteInputStream(),
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
        sourcefile.name = "Racoons.kt"
        sourcefile.lines.add(element)
        pack.name = "/"
        pack.sourcefile = sourcefile

        report.packages.add(pack)
        val sourceFile = jacocoParser.parseSourceFile(report)

        sourceFile.shouldNotBeNull()
    }
}