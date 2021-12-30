package org.jesperancinha.plugins.omni.reporter.pipelines

import org.jesperancinha.plugins.omni.reporter.pipelines.GitHubPipeline.Companion.GITHUB_JOB
import org.jesperancinha.plugins.omni.reporter.pipelines.GitLabPipeline.Companion.CI_JOB_ID

interface Pipeline {

    val seviceName:String

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

class GitHubPipeline(environment: MutableMap<String, String>,
                     override val seviceName: String = "github-ci") : Pipeline {
    companion object {
        val GITHUB_JOB = "GITHUB_JOB"
    }
}

class GitLabPipeline(environment: MutableMap<String, String>,
                     override val seviceName: String = "gitlab-ci") : Pipeline {
    companion object {
        val CI_JOB_ID = "CI_JOB_ID"
    }
}

class LocalPipeline(environment: MutableMap<String, String>,
                    override val seviceName: String = "local-ci") : Pipeline {

}