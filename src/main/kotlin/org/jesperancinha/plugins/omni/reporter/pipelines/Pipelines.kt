package org.jesperancinha.plugins.omni.reporter.pipelines

import org.jesperancinha.plugins.omni.reporter.domain.PipelineConfigurationException
import org.jesperancinha.plugins.omni.reporter.domain.PipelineConfigurationException.Companion.createParamFailException
import org.jesperancinha.plugins.omni.reporter.pipelines.GitHubPipeline.Companion.GITHUB_JOB
import org.jesperancinha.plugins.omni.reporter.pipelines.GitLabPipeline.Companion.CI_JOB_ID
import java.util.*

interface Pipeline {

    val seviceName: String
    val serviceNumber: String
    val serviceJobId: String

    companion object {
        val currentPipeline = {
            val environment = System.getenv()
            when {
                environment[GITHUB_JOB] != null -> GitHubPipeline(environment)
                environment[CI_JOB_ID] != null -> GitLabPipeline(environment)
                else -> LocalPipeline(environment)
            }
        }
    }
}

class GitHubPipeline(
    environment: MutableMap<String, String>,
    override val seviceName: String = "github-ci",
    override val serviceNumber: String = environment[GITHUB_RUN_NUMBER] ?: throw PipelineConfigurationException(
        GITHUB_RUN_NUMBER
    ),
    override val serviceJobId: String = environment[GITHUB_JOB] ?: throw createParamFailException(GITHUB_JOB)
) : Pipeline {
    companion object {
        val GITHUB_JOB = "GITHUB_JOB"
        val GITHUB_RUN_NUMBER = "GITHUB_RUN_NUMBER"
    }
}

class GitLabPipeline(
    environment: MutableMap<String, String>,
    override val seviceName: String = "gitlab-ci",
    override val serviceNumber: String = environment[CI_CONCURRENT_ID] ?: throw PipelineConfigurationException(
        CI_CONCURRENT_ID
    ),
    override val serviceJobId: String = environment[GITHUB_JOB] ?: throw createParamFailException(GITHUB_JOB)
) : Pipeline {
    companion object {
        val CI_JOB_ID = "CI_JOB_ID"
        val CI_CONCURRENT_ID = "CI_CONCURRENT_ID"
    }
}

class LocalPipeline(
    environment: MutableMap<String, String>,
    override val seviceName: String = "local-ci",
    override val serviceNumber: String = UUID.randomUUID().toString(),
    override val serviceJobId: String = UUID.randomUUID().toString(),
) : Pipeline {

}