package org.jesperancinha.plugins.omni.reporter.transformers

import org.jesperancinha.plugins.omni.reporter.domain.CoverallsReport
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.jesperancinha.plugins.omni.reporter.repository.GitRepository
import java.io.File
import java.io.InputStream

/**
 * Created by jofisaes on 05/01/2022
 */
interface OmniReportParser<T> {
    fun parseSourceFile(inputStream: InputStream, compiledSourcesDirs: List<File>): CoverallsReport
}

abstract class OmniReporterParserImpl<T>(
    internal val token: String,
    internal val pipeline: Pipeline,
    internal val root: File,
    internal val includeBranchCoverage: Boolean,
) : OmniReportParser<T> {
    val gitRepository = GitRepository(root, pipeline)
}