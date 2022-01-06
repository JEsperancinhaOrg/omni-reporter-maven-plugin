package org.jesperancinha.plugins.omni.reporter.processors

import org.apache.maven.project.MavenProject
import org.eclipse.jgit.lib.RepositoryBuilder
import org.jesperancinha.plugins.omni.reporter.CoverallsReportNotGeneratedException
import org.jesperancinha.plugins.omni.reporter.CoverallsUrlNotConfiguredException
import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.CodacyClient
import org.jesperancinha.plugins.omni.reporter.domain.CoverallsClient
import org.jesperancinha.plugins.omni.reporter.isSupported
import org.jesperancinha.plugins.omni.reporter.parsers.Language
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.jesperancinha.plugins.omni.reporter.transformers.JacocoParserToCodacy
import org.jesperancinha.plugins.omni.reporter.transformers.JacocoParserToCoveralls
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Created by jofisaes on 05/01/2022
 */
abstract class Processor(
    private val ignoreTestBuildDirectory: Boolean,
) {
    abstract fun processReports()

    val supportedPredicate =
        if (ignoreTestBuildDirectory) { testDirectory: String, report: File ->
            !report.absolutePath.contains(testDirectory)
        } else { _, _ -> true }
}

class CoverallsReportsProcessor(
    private val coverallsToken: String,
    private val coverallsUrl: String?,
    private val currentPipeline: Pipeline,
    private val allProjects: List<MavenProject?>,
    private val projectBaseDir: File?,
    private val failOnUnknown: Boolean,
    private val failOnReportNotFound: Boolean,
    private val failOnReportSending: Boolean,
    private val branchCoverage: Boolean,
    private val useCoverallsCount: Boolean,
    ignoreTestBuildDirectory: Boolean
) : Processor(ignoreTestBuildDirectory) {

    override fun processReports() {
        val jacocoParser =
            JacocoParserToCoveralls(
                coverallsToken,
                currentPipeline,
                projectBaseDir ?: throw ProjectDirectoryNotFoundException(),
                failOnUnknown = failOnUnknown,
                includeBranchCoverage = branchCoverage,
                useCoverallsCount = useCoverallsCount
            )

        allProjects.toReportFiles(supportedPredicate)
            .filter { (project, _) -> project.compileSourceRoots != null }
            .forEach { (project, reports) ->
                reports.forEach { report ->
                    logger.info("Parsing file: $report")
                    jacocoParser.parseInput(
                        report.inputStream(),
                        project.compileSourceRoots.map { file -> File(file) })
                }

            }

        val coverallsClient =
            CoverallsClient(coverallsUrl ?: throw CoverallsUrlNotConfiguredException(), coverallsToken)
        try {
            val response =
                coverallsClient.submit(jacocoParser.coverallsReport ?: let {
                    if (failOnReportNotFound) throw CoverallsReportNotGeneratedException() else {
                        logger.warn("Coveralls report was not generated! This usually means that no jacoco.xml reports have been generated.")
                        return
                    }
                })
            logger.info("* Omni Reporting to Coveralls complete!")
            logger.info("- Response")
            logger.info(response?.url)
            logger.info(response?.message)
        } catch (ex: RuntimeException) {
            logger.error("Failed sending Coveralls report!", ex)
            if (failOnReportSending) {
                throw ex
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CoverallsReportsProcessor::class.java)
    }
}

private fun List<MavenProject?>.toReportFiles(supportedPredicate: (String, File) -> Boolean): List<Pair<MavenProject, List<File>>> =
    this.filterNotNull()
        .map { project ->
            project to File(project.build?.directory ?: throw ProjectDirectoryNotFoundException()).walkTopDown()
                .toList()
                .filter { report ->
                    report.isFile && report.name.startsWith("jacoco") && report.extension.isSupported
                            && supportedPredicate(project.build.testOutputDirectory, report)
                }.distinct()
        }.distinct()


class CodacyProcessor(
    private val token: String,
    private val codacyUrl: String?,
    private val currentPipeline: Pipeline,
    private val allProjects: List<MavenProject?>,
    private val projectBaseDir: File?,
    private val failOnReportSending: Boolean,
    ignoreTestBuildDirectory: Boolean
) : Processor(ignoreTestBuildDirectory) {
    override fun processReports() {
        val codacyJacocoReports = allProjects.toReportFiles(supportedPredicate)
            .filter { (project, _) -> project.compileSourceRoots != null }
            .flatMap { (project, reports) ->
                reports.map { report ->
                    val jacocoParserToCodacy = JacocoParserToCodacy(
                        token = token,
                        pipeline = currentPipeline,
                        root = projectBaseDir ?: throw ProjectDirectoryNotFoundException(),
                    )
                    jacocoParserToCodacy.parseInput(report, project.compileSourceRoots.map { file ->
                        File(file)
                    }) to jacocoParserToCodacy.languages
                }
            }

        val repo = RepositoryBuilder().findGitDir(projectBaseDir).build()

        val reportsPerLanguage = Language.values().map { currentLanguage ->
            currentLanguage to codacyJacocoReports
                .filter { (_, languages) -> languages.contains(currentLanguage) }
                .map { (report, _) -> report }
        }.toMap()
            .filter { (_, reports) -> reports.isNotEmpty() }

        try {
            reportsPerLanguage.entries.forEach { (language, reports) ->
                CodacyClient(
                    token = token,
                    language = language,
                    url = codacyUrl,
                    repo = repo
                ).submit(reports)
            }
            logger.info("* Omni Reporting to Codacy complete!")
        } catch (ex: RuntimeException) {
            logger.error("Failed sending Codacy report!", ex)
            if (failOnReportSending) {
                throw ex
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CodacyProcessor::class.java)
    }
}