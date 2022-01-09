package org.jesperancinha.plugins.omni.reporter.processors

import org.apache.maven.project.MavenProject
import org.jesperancinha.plugins.omni.reporter.*
import java.io.File

private val CODECOV_SUPPORTED_REPORTS = arrayOf(
    "jacoco" to "xml",
    "lcov" to "txt",
    "gcov" to "txt",
    "golang" to "txt",
    "lcov" to "txt",
    "coverage" to "xml",
    "cobertura" to "xml"
)

/**
 * Created by jofisaes on 05/01/2022
 */
abstract class Processor(
    ignoreTestBuildDirectory: Boolean,
) {
    abstract fun processReports()

    val supportedPredicate =
        if (ignoreTestBuildDirectory) { testDirectory: String, report: File ->
            !report.absolutePath.contains(testDirectory)
        } else { _, _ -> true }
}

internal fun List<MavenProject?>.toJacocoReportFiles(supportedPredicate: (String, File) -> Boolean): List<Pair<MavenProject, List<File>>> =
    this.filterNotNull()
        .map { project ->
            project to File(project.build?.directory ?: throw ProjectDirectoryNotFoundException()).walkTopDown()
                .toList()
                .filter { report ->
                    report.isFile && report.name.startsWith("jacoco") && report.extension.isSupported
                            && supportedPredicate(project.build.testOutputDirectory, report)
                }.distinct()
        }.distinct()

internal fun List<MavenProject?>.toAllCodecovSupportedFiles(supportedPredicate: (String, File) -> Boolean): List<Pair<MavenProject, List<File>>> =
    this.filterNotNull()
        .map { project ->
            project to File(project.build?.directory ?: throw ProjectDirectoryNotFoundException()).walkTopDown()
                .toList()
                .filter { report ->
                    report.isFile && report.name.startsWith("jacoco") && report.extension.isSupported
                            && supportedPredicate(project.build.testOutputDirectory, report)
                }.distinct()
        }.distinct()
