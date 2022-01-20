package org.jesperancinha.plugins.omni.reporter

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.jesperancinha.plugins.omni.reporter.domain.CodacyApiTokenConfig
import org.jesperancinha.plugins.omni.reporter.pipelines.PipelineImpl
import org.jesperancinha.plugins.omni.reporter.processors.CodacyProcessor
import org.jesperancinha.plugins.omni.reporter.processors.CodecovProcessor
import org.jesperancinha.plugins.omni.reporter.processors.CoverallsReportsProcessor
import org.slf4j.LoggerFactory
import java.io.File

private val OmniReporterMojo.isCodacyAPIConfigured: Boolean
    get() = CodacyApiTokenConfig.isApiTokenConfigure(
        codacyApiToken = codacyApiToken,
        codacyOrganizationProvider = codacyOrganizationProvider,
        codacyUsername = codacyUserName,
        codacyProjectName = codacyProjectName
    )

private val MavenProject?.findAllSearchFolders: List<MavenProject>
    get() = ((this?.collectedProjects.let {
        it?.addAll(it.flatMap { subProj -> subProj.findAllSearchFolders })
        it
    } ?: mutableListOf()) + this).filterNotNull()

@Mojo(name = "report", threadSafe = false, aggregator = true)
open class OmniReporterMojo(
    @Parameter(property = "coverallsUrl", defaultValue = "https://coveralls.io/api/v1/jobs")
    protected var coverallsUrl: String? = null,
    @Parameter(property = "codacyUrl", defaultValue = "https://api.codacy.com")
    protected var codacyUrl: String? = null,
    @Parameter(property = "codecovUrl", defaultValue = "https://codecov.io/upload")
    protected var codecovUrl: String? = null,
    @Parameter(property = "sourceEncoding", defaultValue = "\${project.build.sourceEncoding}")
    var sourceEncoding: String? = null,
    @Parameter(property = "projectBaseDir", defaultValue = "\${project.basedir}")
    var projectBaseDir: File? = null,
    @Parameter(property = "failOnNoEncoding", defaultValue = "false")
    var failOnNoEncoding: Boolean = false,
    @Parameter(property = "failOnUnknown", defaultValue = "false")
    var failOnUnknown: Boolean = false,
    @Parameter(property = "failOnReportNotFound", defaultValue = "false")
    var failOnReportNotFound: Boolean = false,
    @Parameter(property = "failOnReportSendingError", defaultValue = "false")
    var failOnReportSendingError: Boolean = false,
    @Parameter(property = "failOnXmlParsingError", defaultValue = "false")
    var failOnXmlParsingError: Boolean = false,
    @Parameter(property = "disableCoveralls", defaultValue = "false")
    var disableCoveralls: Boolean = false,
    @Parameter(property = "disableCodacy", defaultValue = "false")
    var disableCodacy: Boolean = false,
    @Parameter(property = "disableCodecov", defaultValue = "false")
    var disableCodecov: Boolean = false,
    @Parameter(property = "ignoreTestBuildDirectory", defaultValue = "true")
    var ignoreTestBuildDirectory: Boolean = true,
    @Parameter(property = "useCoverallsCount", defaultValue = "true")
    var useCoverallsCount: Boolean = true,
    @Parameter(property = "branchCoverage")
    var branchCoverage: Boolean = false,
    @Parameter(property = "coverallsToken")
    var coverallsToken: String? = null,
    @Parameter(property = "codecovToken")
    var codecovToken: String? = null,
    @Parameter(property = "codacyToken")
    var codacyToken: String? = null,
    @Parameter(property = "codacyApiToken")
    var codacyApiToken: String? = null,
    @Parameter(property = "codacyOrganizationProvider")
    var codacyOrganizationProvider: String? = null,
    @Parameter(property = "codacyUserName")
    var codacyUserName: String? = null,
    @Parameter(property = "codacyProjectName")
    var codacyProjectName: String? = null,
    @Parameter(defaultValue = "\${project}", readonly = true)
    var project: MavenProject? = null,
    @Parameter(property = "extraReportFolder")
    val extraSourceFolders: List<File> = emptyList()
) : AbstractMojo() {

    override fun execute() {
        logLine()
        logger.info(javaClass.getResourceAsStream("/banner.txt")?.bufferedReader().use { it?.readText() })
        logLine()


        val environment = System.getenv()
        coverallsToken = (coverallsToken ?: environment["COVERALLS_REPO_TOKEN"]) ?: environment["COVERALLS_TOKEN"]
        codacyToken = codacyToken ?: environment["CODACY_PROJECT_TOKEN"]
        codacyApiToken = codacyApiToken ?: environment["CODACY_API_TOKEN"]
        codacyOrganizationProvider = codacyOrganizationProvider ?: environment["CODACY_ORGANIZATION_PROVIDER"]
        codacyUserName = codacyUserName ?: environment["CODACY_USERNAME"]
        codacyProjectName = codacyProjectName ?: environment["CODACY_PROJECT_NAME"]
        codecovToken = codecovToken ?: environment["CODECOV_TOKEN"]

        val allProjects = project.findAllSearchFolders.injectExtraSourceFiles(extraSourceFolders)

        logLine()
        logger.info("Coveralls URL: $coverallsUrl")
        logger.info("Codacy URL: $codacyUrl")
        logger.info("Codecov URL: $codecovUrl")
        logger.info("Coveralls token: ${checkToken(coverallsToken)}")
        logger.info("Codecov token: ${checkToken(codecovToken)}")
        logger.info("Codacy token: ${checkToken(codacyToken)}")
        logger.info("Codacy API token: ${checkToken(codacyApiToken)}")
        logger.info("Codacy API fully configured: ${this.isCodacyAPIConfigured}")
        logger.info("Source Encoding: $sourceEncoding")
        logger.info("Parent Directory: $projectBaseDir")
        logger.info("failOnNoEncoding: $failOnNoEncoding")
        logger.info("failOnUnknown: $failOnUnknown")
        logger.info("failOnReportNotFound: $failOnReportNotFound")
        logger.info("failOnReportSendingError: $failOnReportSendingError")
        logger.info("failOnXmlParsingError: $failOnXmlParsingError")
        logger.info("disableCoveralls: $disableCoveralls")
        logger.info("disableCodacy: $disableCodacy")
        logger.info("ignoreTestBuildDirectory: $ignoreTestBuildDirectory")
        logger.info("branchCoverage: $branchCoverage")
        logger.info("useCoverallsCount: $useCoverallsCount")
        logger.info("extraSourceFolders: ${extraSourceFolders.joinToString(";")}")
        logLine()

        val currentPipeline = PipelineImpl.currentPipeline

        coverallsToken?.let { token ->
            if (!disableCoveralls)
                CoverallsReportsProcessor(
                    coverallsToken = token,
                    coverallsUrl = coverallsUrl,
                    currentPipeline = currentPipeline,
                    allProjects = allProjects,
                    projectBaseDir = projectBaseDir,
                    failOnUnknown = failOnUnknown,
                    failOnReportNotFound = failOnReportNotFound,
                    failOnReportSending = failOnReportSendingError,
                    failOnXmlParseError = failOnXmlParsingError,
                    branchCoverage = branchCoverage,
                    ignoreTestBuildDirectory = ignoreTestBuildDirectory,
                    useCoverallsCount = useCoverallsCount
                ).processReports()
        }

        if ((this.isCodacyAPIConfigured || codacyToken != null) && !disableCodacy) {
            val codacyApiTokenConfig = codacyApiToken?.let {
                CodacyApiTokenConfig(
                    codacyApiToken = codacyApiToken ?: throw IncompleteCodacyApiTokenConfigurationException(),
                    codacyOrganizationProvider = codacyOrganizationProvider
                        ?: throw IncompleteCodacyApiTokenConfigurationException(),
                    codacyUsername = codacyUserName ?: throw IncompleteCodacyApiTokenConfigurationException(),
                    codacyProjectName = codacyProjectName ?: throw IncompleteCodacyApiTokenConfigurationException()
                )
            }
            CodacyProcessor(
                token = codacyToken,
                apiToken = codacyApiTokenConfig,
                codacyUrl = codacyUrl,
                currentPipeline = currentPipeline,
                allProjects = allProjects,
                projectBaseDir = projectBaseDir,
                failOnReportNotFound = failOnReportNotFound,
                failOnReportSending = failOnReportSendingError,
                failOnXmlParseError = failOnXmlParsingError,
                failOnUnknown = failOnUnknown,
                ignoreTestBuildDirectory = ignoreTestBuildDirectory,
            ).processReports()
        }


        codecovToken?.let { token ->
            if (!disableCodecov)
                CodecovProcessor(
                    token = token,
                    codecovUrl = codecovUrl,
                    currentPipeline = currentPipeline,
                    allProjects = allProjects,
                    projectBaseDir = projectBaseDir,
                    failOnReportNotFound = failOnReportNotFound,
                    failOnReportSending = failOnReportSendingError,
                    failOnUnknown = failOnUnknown,
                    ignoreTestBuildDirectory = ignoreTestBuildDirectory,
                ).processReports()
        }
    }

    private fun logLine() = let {
        logger.info("*".repeat(OMNI_CHARACTER_LINE_NUMBER))
    }

    private fun checkToken(token: String?) = token?.let { "found" } ?: "not found"

    companion object {
        private val logger = LoggerFactory.getLogger(OmniReporterMojo::class.java)

        const val OMNI_CHARACTER_LINE_NUMBER = 150
    }
}

private fun List<MavenProject>.injectExtraSourceFiles(extraSourceFolders: List<File>): List<MavenProject> =
    this.map { project ->
        val extraSourcesFoldersFound =
            extraSourceFolders.filter { sourceFolder ->
                !project.basedir.toPath().relativize(sourceFolder.toPath()).toString().contains("..")
            }
        project.compileSourceRoots.addAll(extraSourcesFoldersFound.map { foundSourceFolder -> foundSourceFolder.absolutePath }
            .toMutableList())
        project
    }
