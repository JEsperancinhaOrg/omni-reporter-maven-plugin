package org.jesperancinha.plugins.omni.reporter.domain

import com.codacy.CodacyCoverageReporter
import com.codacy.configuration.parser.BaseCommandConfig
import com.codacy.configuration.parser.Report
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.jesperancinha.plugins.omni.reporter.parsers.Language
import scala.Some
import scala.collection.mutable.ListBuffer
import java.io.File


/**
 * Created by jofisaes on 30/12/2021
 */
open class CodacyClient(
    override val token: String,
    override val url: String? = null,
    val language: Language,
    val repo: Repository
) : ApiClient<List<File>, Unit> {
    override fun submit(report: List<File>) {

        val revision = repo.resolve(Constants.HEAD)
        val commitId = RevWalk(repo).parseCommit(revision).id.name
        val baseConfig =
            BaseCommandConfig(
                Some(token),
                Some.empty(),
                Some.empty(),
                Some.empty(),
                Some.apply(url),
                Some.apply(commitId),
                false,
                false
            )

        var coverageReports = ListBuffer<File>()
        report.forEach { coverageReports = coverageReports.`$plus$eq`(it) }

        val commandConfig = Report(
            baseConfig,
            Some.apply(language.name.lowercase()),
            0,
            Some.apply(coverageReports.toList()),
            0,
            Some.empty(),
        )

        CodacyCoverageReporter.run(commandConfig)
    }
}