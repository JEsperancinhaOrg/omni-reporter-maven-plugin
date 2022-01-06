package org.jesperancinha.plugins.omni.reporter.transformers

import com.codacy.api.CoverageReport
import com.codacy.parsers.implementation.JacocoParser
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import java.io.File

/**
 * Created by jofisaes on 05/01/2022
 */
class JacocoParserToCodacy(
    token: String,
    pipeline: Pipeline,
    root: File
) : OmniReporterParserImpl<File, CoverageReport>(token, pipeline, root) {
    override fun parseInput(input: File, compiledSourcesDirs: List<File>): CoverageReport =
        JacocoParser.parse(root, input).right().get()
}