package org.jesperancinha.plugins.omni.reporter

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.jesperancinha.plugins.omni.reporter.pipelines.PipelineImpl
import org.jesperancinha.plugins.omni.reporter.processors.CodacyProcessor
import org.jesperancinha.plugins.omni.reporter.processors.CoverallsReportsProcessor
import org.slf4j.LoggerFactory
import java.io.File

private val MavenProject?.findAllSearchFolders: List<MavenProject>
    get() = ((this?.collectedProjects.let {
        it?.addAll(it.flatMap { subProj -> subProj.findAllSearchFolders })
        it
    } ?: mutableListOf()) + this).filterNotNull()

internal val String.isSupported: Boolean
    get() = equals("xml")

@Mojo(name = "report", threadSafe = false, aggregator = true)
open class OmniReporterMojo(
    @Parameter(property = "coverallsUrl", defaultValue = "https://coveralls.io/api/v1/jobs")
    protected var coverallsUrl: String? = null,
    @Parameter(property = "codacyUrl", defaultValue = "https://api.codacy.com")
    protected var codacyUrl: String? = null,
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
        codecovToken = codecovToken ?: environment["CODECOV_TOKEN"]
        codacyToken = codacyToken ?: environment["CODACY_PROJECT_TOKEN"]

        val allProjects = project.findAllSearchFolders.injectExtraSourceFiles(extraSourceFolders)

        logLine()
        logger.info("Coveralls URL: $coverallsUrl")
        logger.info("Copdacy URL: $codacyUrl")
        logger.info("Coveralls token: ${checkToken(coverallsToken)}")
        logger.info("Codecov token: ${checkToken(codecovToken)}")
        logger.info("Codacy token: ${checkToken(codacyToken)}")
        logger.info("Source Encoding: $sourceEncoding")
        logger.info("Parent Directory: $projectBaseDir")
        logger.info("failOnNoEncoding: $failOnNoEncoding")
        logger.info("failOnUnknown: $failOnUnknown")
        logger.info("failOnReportNotFound: $failOnReportNotFound")
        logger.info("failOnReportSendingError: $failOnReportSendingError")
        logger.info("ignoreTestBuildDirectory: $ignoreTestBuildDirectory")
        logger.info("branchCoverage: $branchCoverage")
        logger.info("useCoverallsCount: $useCoverallsCount")
        logger.info("extraSourceFolders: ${extraSourceFolders.joinToString(";")}")
        logLine()

        val currentPipeline = PipelineImpl.currentPipeline

        coverallsToken?.let { token ->
            CoverallsReportsProcessor(
                coverallsToken = token,
                coverallsUrl = coverallsUrl,
                currentPipeline = currentPipeline,
                allProjects = allProjects,
                projectBaseDir = projectBaseDir,
                failOnUnknown = failOnUnknown,
                failOnReportNotFound = failOnReportNotFound,
                failOnReportSending = failOnReportSendingError,
                branchCoverage = branchCoverage,
                ignoreTestBuildDirectory = ignoreTestBuildDirectory,
                useCoverallsCount = useCoverallsCount
            ).processReports()
        }

        codacyToken?.let { token ->
            CodacyProcessor(
                token = token,
                codacyUrl = codacyUrl,
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


class ProjectDirectoryNotFoundException : RuntimeException()

class CoverallsUrlNotConfiguredException : RuntimeException()

class CoverallsReportNotGeneratedException : RuntimeException()