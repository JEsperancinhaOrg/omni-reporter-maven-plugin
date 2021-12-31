package org.jesperancinha.plugins.omni.reporter

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.apache.maven.settings.Settings
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import java.io.File
import java.util.*

@Mojo(name = "report", threadSafe = false, aggregator = true)
open class OmniReporterMojo(
    @Parameter(property = "jacocoReports")
    protected var jacocoReports: MutableList<File> = mutableListOf(),
    @Parameter(property = "relativeReportDirs")
    protected var relativeReportDirs: List<String> = mutableListOf(),
    @Parameter(property = "coverallsFile", defaultValue = "\${project.build.directory}/coveralls.json")
    var coverallsFile: File? = null,
    @Parameter(property = "coverallsUrl", defaultValue = "https://coveralls.io/api/v1/jobs")
    protected var coverallsUrl: String? = null,
    @Parameter(property = "sourceDirectories")
    var sourceDirectories: List<File>? = null,
    @Parameter(property = "sourceEncoding", defaultValue = "\${project.build.sourceEncoding}")
    var sourceEncoding: String? = null,
    @Parameter(property = "serviceName")
    var serviceName: String? = null,
    @Parameter(property = "serviceJobId")
    var serviceJobId: String? = null,
    @Parameter(property = "serviceBuildNumber")
    var serviceBuildNumber: String? = null,
    @Parameter(property = "serviceBuildUrl")
    var serviceBuildUrl: String? = null,
    @Parameter(property = "serviceEnvironment")
    var serviceEnvironment: Properties? = null,
    @Parameter(property = "branch")
    var branch: String? = null,
    @Parameter(property = "pullRequest")
    var pullRequest: String? = null,
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
        coverallsToken =  coverallsToken ?: environment["COVERALLS_REPO_TOKEN"] ?: environment["COVERALLS_TOKEN"]
        codecovToken = codecovToken?: environment["CODECOV_TOKEN"]
        codacyToken = codecovToken?: environment["CODACY_PROJECT_TOKEN"]

        val currentPipeline = Pipeline.currentPipeline

    }
}