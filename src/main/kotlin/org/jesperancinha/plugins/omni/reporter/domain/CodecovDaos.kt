package org.jesperancinha.plugins.omni.reporter.domain

import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpContent
import org.apache.http.entity.ContentType
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.jesperancinha.plugins.omni.reporter.pipelines.Pipeline
import org.slf4j.LoggerFactory


val TRAVIS = "travis"
val BUILDBOT = "buildbot"
val CIRCLECI = "circleci"
val BUDDYBUILD = "buddybuild"
val SOLANO = "solano"
val TEAMCITY = "teamcity"
val APPVEYOR = "appveyor"
val WERCKER = "wercker"
val MAGNUM = "magnum"
val SHIPPABLE = "shippable"
val CODESHIP = "codeship"
val DRONE_IO = "drone.io"
val JENKINS = "jenkins"
val SEMAPHORE = "semaphore"
val GITLAB = "gitlab"
val BAMBOO = "bamboo"
val SNAP = "snap"
val BUILDKITE = "buildkite"
val BITRISE = "bitrise"
val GREENHOUSE = "greenhouse"
val CUSTOM = "custom"

enum class CodecovEndpoint {
    V2,
    V4,
    V5
}

/**
 * Created by jofisaes on 30/12/2021
 */
class CodecovClient(
    override val url: String,
    override val token: String,
    private val pipeline: Pipeline,
    private val repo: Repository,
) : ApiClient<String, String> {
    override fun submit(report: String): String? {
        val revision = repo.resolve(Constants.HEAD)
        val commitId = RevWalk(repo).parseCommit(revision).id.name
        val query
        = "commit=$commitId&token=$token&branch=${pipeline.branchName?: repo.branch}&build=${pipeline.serviceName}&job=${pipeline.serviceJobId}&build_url=${pipeline.buildUrl}&service=${pipeline.codecovServiceName}"
        val codacyReportUrl = "$url/v4?${query}"
        logger.info("Sending report to codecov's S3 server at ${codacyReportUrl.replace(token, "<PROTECTED>")}")
        logger.debug(report.replace(token, "<PROTECTED>"))
        val content: HttpContent = ByteArrayContent(ContentType.TEXT_PLAIN.mimeType, report.toByteArray())
        val httpRequest = httpRequestFactory.buildPostRequest(GenericUrl(codacyReportUrl), content)
        httpRequest.headers.contentType = ContentType.TEXT_PLAIN.mimeType
        val httpResponse = httpRequest?.execute()
        return httpResponse?.content?.bufferedReader()?.readText()
    }
    
    companion object {
        private val logger = LoggerFactory.getLogger(CodecovClient::class.java)
    }
}