package org.jesperancinha.plugins.omni.reporter

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.apache.maven.settings.Settings
import org.jesperancinha.plugins.omni.reporter.domain.CoverallsClient
import org.jesperancinha.plugins.omni.reporter.parsers.JacocoParser
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.jesperancinha.plugins.omni.reporter.pipelines.PipelineImpl
import org.slf4j.LoggerFactory
import java.io.File

private val MavenProject?.findAllSearchFolders: List<MavenProject?>
    get() = (this?.collectedProjects.let {
        it?.addAll(it.flatMap { subProj -> subProj.findAllSearchFolders })
        it
    } ?: mutableListOf()) + this

private val String.isSupported: Boolean
    get() = equals("xml")


@Mojo(name = "report", threadSafe = false, aggregator = true)
open class OmniReporterMojo(
    @Parameter(property = "coverallsUrl", defaultValue = "https://coveralls.io/api/v1/jobs")
    protected var coverallsUrl: String? = null,
    @Parameter(property = "sourceEncoding", defaultValue = "\${project.build.sourceEncoding}")
    var sourceEncoding: String? = null,
    @Parameter(property = "projectBaseDir", defaultValue = "\${project.basedir}")
    var projectBaseDir: File? = null,
    @Parameter(property = "timestampFormat", defaultValue = "\${maven.build.timestamp.format}")
    protected var timestampFormat: String? = null,
    @Parameter(property = "timestamp", defaultValue = "\${maven.build.timestamp}")
    protected var timestamp: String? = null,
    @Parameter(property = "failOnNoEncoding", defaultValue = "false")
    var failOnNoEncoding: Boolean = false,
    @Parameter(property = "failOnUnknown", defaultValue = "false")
    var failOnUnknown: Boolean = false,
    @Parameter(defaultValue = "\${settings}", readonly = true, required = true)
    var settings: Settings? = null,
    @Parameter(property = "coverallsToken")
    var coverallsToken: String? = null,
    @Parameter(property = "codecovToken")
    var codecovToken: String? = null,
    @Parameter(property = "codacyToken")
    var codacyToken: String? = null,
    @Component
    var project: MavenProject? = null,
) : AbstractMojo() {

    override fun execute() {
        logger.info("*".repeat(OMNI_CHARACTER_LINE_NUMBER))
        logger.info(javaClass.getResourceAsStream("/banner.txt")?.bufferedReader().use { it?.readText() })
        logger.info("*".repeat(OMNI_CHARACTER_LINE_NUMBER))


        val environment = System.getenv()
        coverallsToken = coverallsToken ?: environment["COVERALLS_REPO_TOKEN"] ?: environment["COVERALLS_TOKEN"]
        codecovToken = codecovToken ?: environment["CODECOV_TOKEN"]
        codacyToken = codecovToken ?: environment["CODACY_PROJECT_TOKEN"]

        val allProjects = project.findAllSearchFolders + project

        logger.info("Coveralls URL: $coverallsUrl")
        logger.info("Source Encoding: $sourceEncoding")
        logger.info("Parent Directory: $projectBaseDir")

        val currentPipeline = PipelineImpl.currentPipeline

        coverallsToken?.let { token -> processAndSubmitCoverallsReports(token, currentPipeline, allProjects) }
    }

    private fun processAndSubmitCoverallsReports(
        token: String,
        currentPipeline: Pipeline,
        allProjects: List<MavenProject?>
    ) {
        val jacocoParser =
            JacocoParser(
                token,
                currentPipeline,
                projectBaseDir ?: throw ProjectDirectoryNotFoundException(),
                failOnUnknown
            )
        allProjects.map { project ->
            File(project?.build?.directory ?: throw ProjectDirectoryNotFoundException()).walkTopDown()
                .forEach { report ->
                    if (report.isFile && report.name.startsWith("jacoco") && report.extension.isSupported) {
                        logger.info("Parsing file: $report")
                        jacocoParser.parseSourceFile(
                            report.inputStream(),
                            File(project.build?.sourceDirectory ?: throw ProjectDirectoryNotFoundException()),
                        )
                    }
                }
        }
        val coverallsClient = CoverallsClient(coverallsUrl ?: throw CoverallsUrlNotConfiguredException(), token)
        val response =
            coverallsClient.submit(jacocoParser.coverallsReport ?: throw CoverallsReportNotGeneratedException())
        logger.info("* Omni Reporting to Coveralls response:")
        logger.info(response?.url)
        logger.info(response?.message)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmniReporterMojo::class.java)

        const val OMNI_CHARACTER_LINE_NUMBER = 150
    }
}

class ProjectDirectoryNotFoundException : RuntimeException()

class CoverallsUrlNotConfiguredException : RuntimeException()

class CoverallsReportNotGeneratedException : RuntimeException()