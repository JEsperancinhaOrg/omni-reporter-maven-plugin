package org.jesperancinha.plugins.omni.reporter

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.slf4j.LoggerFactory
import java.io.File

private val List<MavenProject>.toOmniProjects: List<OmniProject>
    get() = map {
        OmniProjectGeneric(
            it.compileSourceRoots,
            OmniBuildGeneric(it.build.testOutputDirectory, it.build.directory)
        )
    }

private val MavenProject?.findAllSearchProjects: List<MavenProject>
    get() = ((this?.collectedProjects.let {
        it?.addAll(it.flatMap { subProj -> subProj.findAllSearchProjects })
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
    @Parameter(property = "branchCoverage", defaultValue = "false")
    var branchCoverage: Boolean = false,
    @Parameter(property = "fetchBranchNameFromEnv")
    var fetchBranchNameFromEnv: Boolean = false,
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
    @Parameter(property = "codacyUsername")
    var codacyUsername: String? = null,
    @Parameter(property = "codacyProjectName")
    var codacyProjectName: String? = null,
    @Parameter(defaultValue = "\${project}", readonly = true)
    var project: MavenProject? = null,
    @Parameter(property = "extraSourceFolder")
    val extraSourceFolders: List<File> = emptyList(),
    @Parameter(property = "extraReportFolders")
    val extraReportFolders: List<File> = emptyList(),
    @Parameter(property = "reportRejectList")
    val reportRejectList: List<String> = emptyList()
) : AbstractMojo() {
    override fun execute() {
        val allProjects: List<OmniProject>? =
            projectBaseDir?.let { root ->
                project.findAllSearchProjects.toOmniProjects.injectExtraSourceFiles(
                    extraSourceFolders,
                    root
                )
            }
        val extraProjects = extraReportFolders.toExtraProjects(extraSourceFolders)
        val allOmniProjects = allProjects?.plus(extraProjects) ?: emptyList()
        OmniReporterCommon(
            coverallsUrl,
            codacyUrl,
            codecovUrl,
            sourceEncoding,
            projectBaseDir,
            failOnNoEncoding,
            failOnUnknown,
            failOnReportNotFound,
            failOnReportSendingError,
            failOnXmlParsingError,
            disableCoveralls,
            disableCodacy,
            disableCodecov,
            ignoreTestBuildDirectory,
            useCoverallsCount,
            branchCoverage,
            fetchBranchNameFromEnv,
            coverallsToken,
            codecovToken,
            codacyToken,
            codacyApiToken,
            codacyOrganizationProvider,
            codacyUsername,
            codacyProjectName,
            extraSourceFolders,
            extraReportFolders,
            reportRejectList
        ).execute(allOmniProjects)
    }
}
