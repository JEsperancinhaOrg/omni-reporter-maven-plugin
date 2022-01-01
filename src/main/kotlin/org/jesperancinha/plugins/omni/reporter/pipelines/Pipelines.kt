package org.jesperancinha.plugins.omni.reporter.pipelines

import org.jesperancinha.plugins.omni.reporter.domain.PipelineConfigurationException
import org.jesperancinha.plugins.omni.reporter.domain.PipelineConfigurationException.Companion.createParamFailException
import org.jesperancinha.plugins.omni.reporter.pipelines.GitHubPipeline.Companion.GITHUB_JOB
import org.jesperancinha.plugins.omni.reporter.pipelines.GitLabPipeline.Companion.CI_JOB_ID
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline.Companion.findServiceJobId
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline.Companion.findServiceName
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline.Companion.findServiceNumber
import java.util.*

interface Pipeline {

    val serviceName: String
    val serviceNumber: String?
    val serviceJobId: String?

    companion object {
        private val environment: MutableMap<String, String> = System.getenv()

        val currentPipeline: Pipeline = when {
            environment[GITHUB_JOB] != null -> GitHubPipeline(environment)
            environment[CI_JOB_ID] != null -> GitLabPipeline(environment)
            else -> LocalPipeline(environment)
        }
    }
}

class GitHubPipeline(
    environment: MutableMap<String, String>,
    override val serviceName: String = findServiceName("github-ci"),
    override val serviceNumber: String? = findServiceNumber {
        environment[GITHUB_RUN_NUMBER]
    },
    override val serviceJobId: String? = findServiceJobId {
        environment[GITHUB_JOB]
    }
) : Pipeline {
    companion object {
        const val GITHUB_JOB = "GITHUB_JOB"
        const val GITHUB_RUN_NUMBER = "GITHUB_RUN_NUMBER"
    }
}

class GitLabPipeline(
    environment: MutableMap<String, String>,
    override val serviceName: String = findServiceName("gitlab-ci"),
    override val serviceNumber: String? = findServiceNumber {
        environment[CI_CONCURRENT_ID]
    },
    override val serviceJobId: String? = findServiceJobId {
        environment[CI_JOB_ID]
    }
) : Pipeline {
    companion object {
        const val CI_JOB_ID = "CI_JOB_ID"
        const val CI_CONCURRENT_ID = "CI_CONCURRENT_ID"
    }
}

class LocalPipeline(
    environment: MutableMap<String, String>,
    override val serviceName: String = findServiceName("local-ci"),
    override val serviceNumber: String? = environment[CI_BUILD_NUMBER],
    override val serviceJobId: String? =  findServiceJobId { null }
) : Pipeline {

    companion object {
        const val CI_NAME = "CI_NAME"
        const val CI_BUILD_NUMBER = "CI_BUILD_NUMBER"
        const val JOB_NUM = "JOB_NUM"
        const val CI_BUILD_URL = "CI_BUILD_URL"
        const val CI_BRANCH = "CI_BRANCH"
        const val CI_PULL_REQUEST = "CI_PULL_REQUEST"

        fun findServiceName(failName: String) = System.getenv()[CI_NAME] ?: failName

        fun findServiceNumber(fallback: () -> String?) = System.getenv()[CI_BUILD_NUMBER] ?: fallback()

        fun findServiceJobId(fallback: () -> String?) = System.getenv()[JOB_NUM] ?: fallback()
    }

}