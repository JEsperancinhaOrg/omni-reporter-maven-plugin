package org.jesperancinha.plugins.omni.reporter.transformers

import com.codacy.api.CoverageReport
import com.codacy.parsers.implementation.JacocoParser
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.domain.readValue
import org.jesperancinha.plugins.omni.reporter.parsers.Language
import org.jesperancinha.plugins.omni.reporter.parsers.Language.*
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import java.io.File

/**
 * Created by jofisaes on 05/01/2022
 */
class JacocoParserToCodacy(
    token: String,
    pipeline: Pipeline,
    root: File
) : OmniReporterParserImpl<File, File>(token, pipeline, root) {

    var languages = emptyList<Language>()

    override fun parseInput(input: File, compiledSourcesDirs: List<File>): File = let {
        val readValue = readValue<Report>(input.inputStream())
        languages = readValue.packages.flatMap {
            it.sourcefiles.map { sourcefile ->
                when {
                    sourcefile.name?.endsWith("kt") ?: false -> KOTLIN
                    sourcefile.name?.endsWith("java") ?: false -> JAVA
                    sourcefile.name?.endsWith("scala") ?: false -> SCALA
                    else -> null
                }
            }
        }.filterNotNull()
      input
    }
}