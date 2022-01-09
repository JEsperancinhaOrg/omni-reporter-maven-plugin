package org.jesperancinha.plugins.omni.reporter.transformers

import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Package
import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Report
import org.jesperancinha.plugins.omni.reporter.parsers.readXmlValue
import org.jesperancinha.plugins.omni.reporter.parsers.xmlObjectMapper
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream

/**
 * Codecov allows sending reports from many brands
 * This means that the codecov transformer can provide support for types of reports supported by codecov
 * It will eventually provide support for all of them.
 * Detection is still based on filename
 * This is because no transformation is necessary
 * The final report is an aggregate of all original raw files
 *
 * Created by jofisaes on 09/01/2022
 */
class AllParserToCodecov(
    token: String,
    pipeline: Pipeline,
    root: File,
    includeBranchCoverage: Boolean = false,
) : OmniReporterParserImpl<InputStream, String>
    (token = token, pipeline = pipeline, root = root, includeBranchCoverage = includeBranchCoverage) {
    override fun parseInput(input: InputStream, compiledSourcesDirs: List<File>): String {
        val report: Report = readXmlValue(input)
        if (report.packages.isEmpty()) {
            return input.bufferedReader().readText()
        }
        val copy = report.copy(
            packages = report.packages.map { p: Package -> p.copy(name = findNewPackageName(p, compiledSourcesDirs)) },
        )
        return xmlObjectMapper.writeValueAsString(copy)
    }

    private fun findNewPackageName(p: Package, compiledSourcesDirs: List<File>) =
        compiledSourcesDirs
            .map { File(it, p.name) }
            .filter { file -> file.exists() }
            .map { file -> file.toRelativeString(root) }
            .first()

    companion object {
        val logger = LoggerFactory.getLogger(AllParserToCodecov::class.java)
    }
}