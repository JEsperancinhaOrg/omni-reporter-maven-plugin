package org.jesperancinha.plugins.omni.reporter.transformers

import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.jesperancinha.plugins.omni.reporter.repository.GitRepository
import java.io.File
import java.io.InputStream

/**
 * Created by jofisaes on 05/01/2022
 */
interface OmniReportParser<INPUT, OUTPUT> {
    fun parseInputStream(input: INPUT, compiledSourcesDirs: List<File>): OUTPUT
}

abstract class OmniReporterParserImpl<INPUT, OUTPUT>(
    internal val token: String,
    internal val pipeline: Pipeline,
    internal val root: File,
    internal val includeBranchCoverage: Boolean = false,
) : OmniReportParser<INPUT, OUTPUT> {
    val gitRepository = GitRepository(root, pipeline)
}