package org.jesperancinha.plugins.omni.reporter

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.apache.maven.settings.Settings
import org.jesperancinha.plugins.omni.reporter.domain.CoverallsReport
import org.jesperancinha.plugins.omni.reporter.domain.JsonMappingConfiguration.Companion.objectMapper
import org.jesperancinha.plugins.omni.reporter.parsers.JacocoParser
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.slf4j.LoggerFactory
import java.io.File

@Mojo(name = "report", threadSafe = false, aggregator = true)
open class OmniReporterMojo(
    @Parameter(property = "coverallsUrl", defaultValue = "https://coveralls.io/api/v1/jobs")
    protected var coverallsUrl: String? = null,
    @Parameter(property = "sourceEncoding", defaultValue = "\${project.build.sourceEncoding}")
    var sourceEncoding: String? = null,
    @Parameter(property = "timestampFormat", defaultValue = "\${maven.build.timestamp.format}")
    protected var timestampFormat: String? = null,
    @Parameter(property = "timestamp", defaultValue = "\${maven.build.timestamp}")
    protected var timestamp: String? = null,
    @Parameter(property = "failOnNoEncoding", defaultValue = "false")
    var failOnNoEncoding: Boolean = false,
    @Parameter(property = "failOnUnknown", defaultValue = "false")
    var failOnUnknown: Boolean = false,
    @Parameter(property = "basedir", defaultValue = "\${project.basedir}")
    var projectBaseDir: File? = null,
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
        val environment = System.getenv()
        coverallsToken = coverallsToken ?: environment["COVERALLS_REPO_TOKEN"] ?: environment["COVERALLS_TOKEN"]
        codecovToken = codecovToken ?: environment["CODECOV_TOKEN"]
        codacyToken = codecovToken ?: environment["CODACY_PROJECT_TOKEN"]

        val searchDirectories = project?.let { p ->
            val reportingDirectory = File(p.model.reporting.outputDirectory)
            val buildDirectory = File(p.build.directory)
            listOf(reportingDirectory, buildDirectory)
        } ?: listOf()

        val currentPipeline = Pipeline.currentPipeline

        coverallsToken?.let { token ->
            searchDirectories.map { directory ->
                val report = File(directory, "jacoco.xml")
                val jacocoParser =
                    JacocoParser(report.inputStream(), projectBaseDir ?: throw ProjectDirectoryNotFoundException())
                val coverallsReport = CoverallsReport(
                    repoToken = token,
                    serviceName = currentPipeline.serviceName,
                    sourceFiles = jacocoParser.parseSourceFile(),
                    serviceJobId = currentPipeline.serviceJobId
                )
                logger.info(objectMapper.writeValueAsString(coverallsReport))
                return
            }
        }
    }

    companion object {
        private val  logger = LoggerFactory.getLogger(OmniReporterMojo::class.java)
    }
}

class ProjectDirectoryNotFoundException : RuntimeException()