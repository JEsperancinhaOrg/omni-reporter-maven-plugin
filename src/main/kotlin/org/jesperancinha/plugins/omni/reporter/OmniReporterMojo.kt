package org.jesperancinha.plugins.omni.reporter

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
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
    @Parameter(property = "failOnNoEncoding", defaultValue = "false")
    var failOnNoEncoding: Boolean = false,
    @Parameter(property = "failOnUnknown", defaultValue = "false")
    var failOnUnknown: Boolean = false,
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
) : AbstractMojo() {

    override fun execute() {
        logLine()
        logger.info(javaClass.getResourceAsStream("/banner.txt")?.bufferedReader().use { it?.readText() })
        logLine()


        val environment = System.getenv()
        coverallsToken = (coverallsToken ?: environment["COVERALLS_REPO_TOKEN"]) ?: environment["COVERALLS_TOKEN"]
        codecovToken = codecovToken ?: environment["CODECOV_TOKEN"]
        codacyToken = codecovToken ?: environment["CODACY_PROJECT_TOKEN"]

        val allProjects = project.findAllSearchFolders

        logLine()
        logger.info("Coveralls URL: $coverallsUrl")
        logger.info("Coveralls token: ${checkToken(coverallsToken)}")
        logger.info("Codecov token: ${checkToken(codecovToken)}")
        logger.info("Codacy token: ${checkToken(codacyToken)}")
        logger.info("Source Encoding: $sourceEncoding")
        logger.info("Parent Directory: $projectBaseDir")
        logger.info("failOnNoEncoding: $failOnNoEncoding")
        logger.info("failOnUnknown: $failOnUnknown")
        logger.info("ignoreTestBuildDirectory: $ignoreTestBuildDirectory")
        logger.info("branchCoverage: $branchCoverage")
        logger.info("useCoverallsCount: $useCoverallsCount")
        logLine()

        val currentPipeline = PipelineImpl.currentPipeline

        coverallsToken?.let { token -> processAndSubmitCoverallsReports(token, currentPipeline, allProjects) }
    }

    private fun logLine() = let {
        logger.info("*".repeat(OMNI_CHARACTER_LINE_NUMBER))
    }

    private fun checkToken(token: String?) = token?.let { "found" } ?: "not found"

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
                failOnUnknown,
                branchCoverage,
                useCoverallsCount
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