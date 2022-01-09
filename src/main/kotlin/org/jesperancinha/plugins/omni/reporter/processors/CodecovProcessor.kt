package org.jesperancinha.plugins.omni.reporter.processors

import org.apache.maven.project.MavenProject
import org.eclipse.jgit.lib.RepositoryBuilder
import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.jesperancinha.plugins.omni.reporter.transformers.AllParserToCodecov
import org.jesperancinha.plugins.omni.reporter.transformers.JacocoParserToCodacy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

private const val CODACY_EOF = "\n<<<<<< EOF\n"

/**
 * Created by jofisaes on 09/01/2022
 */
class CodecovProcessor(
    private val token: String,
    private val codacyUrl: String?,
    private val currentPipeline: Pipeline,
    private val allProjects: List<MavenProject?>,
    private val projectBaseDir: File?,
    private val failOnReportNotFound: Boolean,
    private val failOnReportSending: Boolean,
    private val failOnUnknown: Boolean,
    private val failOnXmlParseError: Boolean,
    ignoreTestBuildDirectory: Boolean
) : Processor(ignoreTestBuildDirectory) {
    override fun processReports() {
        logger.info("* Omni Reporting to Codecov started!")

        val repo = RepositoryBuilder().findGitDir(projectBaseDir).build()
        allProjects.toJacocoReportFiles(supportedPredicate)
            .filter { (project, _) -> project.compileSourceRoots != null }
            .flatMap { (project, reports) ->
                reports.map { report ->
                    logger.info("Parsing file: $report")
                    AllParserToCodecov(
                        token = token,
                        pipeline = currentPipeline,
                        root = projectBaseDir ?: throw ProjectDirectoryNotFoundException(),
                    ).parseInput(
                        report.inputStream(),
                        project.compileSourceRoots.map { file -> File(file) })
                }
            }
            .joinToString(CODACY_EOF)
            .plus(CODACY_EOF)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(CodecovProcessor::class.java)
    }
}