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
        logger.info("*".repeat(150))
        logger.info(javaClass.getResourceAsStream("/banner.txt")?.bufferedReader().use { it?.readText() })
        logger.info("*".repeat(150))


        val environment = System.getenv()
        coverallsToken = coverallsToken ?: environment["COVERALLS_REPO_TOKEN"] ?: environment["COVERALLS_TOKEN"]
        codecovToken = codecovToken ?: environment["CODECOV_TOKEN"]
        codacyToken = codecovToken ?: environment["CODACY_PROJECT_TOKEN"]

        val allProjects = project.findAllSearchFolders

        println(sourceEncoding)
        println(coverallsToken)
        println(projectBaseDir)

        val currentPipeline = Pipeline.currentPipeline

        coverallsToken?.let { token ->
            allProjects.map { project ->
                File(project?.build?.directory?: throw ProjectDirectoryNotFoundException()).walkTopDown()
                    .forEach { report ->
                        println(report)
                        if (report.isFile && report.name.startsWith("jacoco") && report.extension.isSupported) {
                            val jacocoParser =
                                JacocoParser(
                                    report.inputStream(),
                                    File(project.build?.sourceDirectory?: throw ProjectDirectoryNotFoundException()),
                                    projectBaseDir
                                )
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
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmniReporterMojo::class.java)
    }
}

class ProjectDirectoryNotFoundException : RuntimeException()