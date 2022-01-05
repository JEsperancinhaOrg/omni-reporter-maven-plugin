package org.jesperancinha.plugins.omni.reporter.processors

import org.apache.maven.project.MavenProject
import org.jesperancinha.plugins.omni.reporter.CoverallsReportNotGeneratedException
import org.jesperancinha.plugins.omni.reporter.CoverallsUrlNotConfiguredException
import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.CoverallsClient
import org.jesperancinha.plugins.omni.reporter.isSupported
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.jesperancinha.plugins.omni.reporter.transformers.JacocoParser
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Created by jofisaes on 05/01/2022
 */
interface Processors {
    fun processReports()
}

class CoverallsReportsProcessor(
    private val coverallsToken: String,
    private val coverallsUrl: String?,
    private val currentPipeline: Pipeline,
    private val allProjects: List<MavenProject?>,
    private val projectBaseDir: File?,
    private val failOnUnknown: Boolean,
    private val failOnReportNotFound: Boolean,
    private val branchCoverage: Boolean,
    private val ignoreTestBuildDirectory: Boolean,
    private val useCoverallsCount: Boolean
) : Processors {

    override fun processReports() {
        val jacocoParser =
            JacocoParser(
                coverallsToken,
                currentPipeline,
                projectBaseDir ?: throw ProjectDirectoryNotFoundException(),
                failOnUnknown = failOnUnknown,
                includeBranchCoverage = branchCoverage,
                useCoverallsCount = useCoverallsCount
            )
        val supportedPredicate =
            if (ignoreTestBuildDirectory) { testDirectory: String, report: File ->
                !report.absolutePath.contains(testDirectory)
            } else { _, _ -> true }
        allProjects.map { project ->
            File(project?.build?.directory ?: throw ProjectDirectoryNotFoundException()).walkTopDown()
                .forEach { report ->
                    if (report.isFile && report.name.startsWith("jacoco") && report.extension.isSupported
                        && supportedPredicate(project.build.testOutputDirectory, report)
                    ) {
                        logger.info("Parsing file: $report")
                        jacocoParser.parseSourceFile(
                            report.inputStream(),
                            project.compileSourceRoots.map { File(it) })
                    }
                }
        }
        val coverallsClient =
            CoverallsClient(coverallsUrl ?: throw CoverallsUrlNotConfiguredException(), coverallsToken)
        val response =
            coverallsClient.submit(jacocoParser.coverallsReport ?: let {
                if (failOnReportNotFound) throw CoverallsReportNotGeneratedException() else {
                    logger.warn("Coveralls report was not generated! This usually means that no jacoco.xml reports have been generated.")
                    return
                }
            })
        logger.info("* Omni Reporting to Coveralls response:")
        logger.info(response?.url)
        logger.info(response?.message)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CoverallsReportsProcessor::class.java)
    }
}