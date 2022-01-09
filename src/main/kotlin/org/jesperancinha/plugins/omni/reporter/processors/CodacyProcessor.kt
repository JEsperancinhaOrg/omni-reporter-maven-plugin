package org.jesperancinha.plugins.omni.reporter.processors

import org.apache.maven.project.MavenProject
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.RepositoryBuilder
import org.jesperancinha.plugins.omni.reporter.CodacyReportNotGeneratedException
import org.jesperancinha.plugins.omni.reporter.CodacyUrlNotConfiguredException
import org.jesperancinha.plugins.omni.reporter.ProjectDirectoryNotFoundException
import org.jesperancinha.plugins.omni.reporter.domain.CodacyClient
import org.jesperancinha.plugins.omni.reporter.domain.CodacyReport
import org.jesperancinha.plugins.omni.reporter.parsers.Language
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.jesperancinha.plugins.omni.reporter.transformers.JacocoParserToCodacy
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Created by jofisaes on 07/01/2022
 */
class CodacyProcessor(
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
        logger.info("* Omni Reporting to Codacy started!")

        val repo = RepositoryBuilder().findGitDir(projectBaseDir).build()

        Language.values().forEach { language ->
            val reportsPerLanguage = allProjects.toJacocoReportFiles(supportedPredicate)
                .filter { (project, _) -> project.compileSourceRoots != null }
                .flatMap { (project, reports) ->
                    reports.map { report ->
                        logger.info("Parsing file: $report")
                        JacocoParserToCodacy(
                            token = token,
                            pipeline = currentPipeline,
                            root = projectBaseDir ?: throw ProjectDirectoryNotFoundException(),
                            failOnUnknown = failOnUnknown,
                            failOnXmlParseError = failOnXmlParseError,
                            language = language
                        ).parseInput(
                            report.inputStream(),
                            project.compileSourceRoots.map { file -> File(file) })
                    }
                }
                .filter {
                    if (it.fileReports.isEmpty()) {
                        if (failOnReportNotFound) throw CodacyReportNotGeneratedException() else {
                            logger.warn("Codacy report was not generated! This usually means that no jacoco.xml reports have been found.")
                        }
                        false
                    } else true
                }

            logger.info("- Found ${reportsPerLanguage.size} reports for language ${language.lang}")
            if (reportsPerLanguage.size > 1) {
                reportsPerLanguage.forEach { codacyReport -> sendCodacyReport(language, repo, codacyReport, true) }
                val response = CodacyClient(
                    token = token,
                    language = language,
                    url = codacyUrl ?: throw CodacyUrlNotConfiguredException(),
                    repo = repo
                ).submitEndReport()
                logger.info("- Response")
                logger.info(response.success)
            } else if (reportsPerLanguage.size == 1) {
                sendCodacyReport(language, repo, reportsPerLanguage[0], false)
            }

            logger.info("* Omni Reporting processing for Codacy complete!")
        }

    }

    private fun sendCodacyReport(
        language: Language,
        repo: Repository,
        codacyReport: CodacyReport,
        partial: Boolean
    ) {
        try {
            val codacyClient = CodacyClient(
                token = token,
                language = language,
                url = codacyUrl ?: throw CodacyUrlNotConfiguredException(),
                repo = repo,
                partial = partial
            )
            val response =
                codacyClient.submit(codacyReport)
            logger.info("* Omni Reporting to Codacy for language $language complete!")
            logger.info("- Response")
            logger.info(response.success)
        } catch (ex: Exception) {
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