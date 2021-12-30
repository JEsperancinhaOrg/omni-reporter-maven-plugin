package org.jesperancinha.plugins.omni.reporter

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.apache.maven.settings.Settings
import java.io.File
import java.io.IOException
import java.util.*

@Mojo(name = "omniReporter", threadSafe = false, aggregator = true)
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
    var basedir: File? = null,
    @Parameter(defaultValue = "\${settings}", readonly = true, required = true)
    var settings: Settings? = null,
    @Component
    var project: MavenProject? = null,
) : AbstractMojo() {

    override fun execute() {
    }
}