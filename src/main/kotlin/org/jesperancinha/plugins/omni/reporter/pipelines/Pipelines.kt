package org.jesperancinha.plugins.omni.reporter.pipelines

import org.jesperancinha.plugins.omni.reporter.pipelines.GitHubPipeline.Companion.GITHUB_RUN_ID
import org.jesperancinha.plugins.omni.reporter.pipelines.GitHubPipeline.Companion.GITHUB_RUN_NUMBER
import org.jesperancinha.plugins.omni.reporter.pipelines.GitLabPipeline.Companion.CI_COMMIT_REF_NAME
import org.jesperancinha.plugins.omni.reporter.pipelines.GitLabPipeline.Companion.CI_CONCURRENT_ID
import org.jesperancinha.plugins.omni.reporter.pipelines.GitLabPipeline.Companion.CI_JOB_ID
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline.Companion.CI_BUILD_NUMBER
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline.Companion.CI_NAME
import org.jesperancinha.plugins.omni.reporter.pipelines.LocalPipeline.Companion.JOB_NUM
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(Pipeline::class.java)
private val environment: MutableMap<String, String> = System.getenv()
private val allEnv = listOf(
    // General
    CI_NAME,
    CI_BUILD_NUMBER,
    JOB_NUM,

    //GitLab
    CI_CONCURRENT_ID,
    CI_JOB_ID,
    CI_COMMIT_REF_NAME,

    //GitHubs
    GITHUB_RUN_NUMBER,
    GITHUB_RUN_ID,
)
private val rejectWords = listOf("BUILD")

interface Pipeline {

    val branchName: String?
    val serviceName: String
    val serviceNumber: String?
    val serviceJobId: String?
}

abstract class PipelineImpl : Pipeline {

    override fun toString() = "* System Variables\n" +
            "${findAllVariables()}\n" +
            "* Service Found\n" +
            "- Service Name = $serviceName\n" +
            "- Service Number (Build) = $serviceNumber\n" +
            "- Service Job Id = $serviceJobId"

    companion object {
        val currentPipeline: Pipeline = when {
            environment[GITHUB_RUN_ID] != null -> GitHubPipeline()
            environment[CI_JOB_ID] != null -> GitLabPipeline()
            else -> LocalPipeline()
        }.also {
            it.toString().lines().forEach { logLine -> logger.info(logLine) }
        }

        internal fun findAllVariables() = allEnv.joinToString("\n") { "- $it = ${environment[it] ?: "null"}" }

        internal fun findSystemVariableValue(name: String): String? =
            environment[name]?.let { if (rejectWords.contains(it.uppercase())) null else it }

        internal fun findServiceName(fallback: () -> String) = findSystemVariableValue(CI_NAME) ?: fallback()

        internal fun findServiceNumber(fallback: () -> String?) = findSystemVariableValue(CI_BUILD_NUMBER) ?: fallback()

        internal fun findServiceJobId(fallback: () -> String?) = findSystemVariableValue(JOB_NUM) ?: fallback()
    }
}

class GitHubPipeline(
    override val serviceName: String = findServiceName { "github-ci" },
    override val serviceNumber: String? = findServiceNumber {
        findSystemVariableValue(GITHUB_RUN_NUMBER)
    },
    override val serviceJobId: String? = findServiceJobId {
        findSystemVariableValue(GITHUB_RUN_ID)
    },
    override val branchName: String? = null
) : PipelineImpl() {
    companion object {
        const val GITHUB_RUN_NUMBER = "GITHUB_RUN_NUMBER"
        const val GITHUB_RUN_ID = "GITHUB_RUN_ID"
    }
}

class GitLabPipeline(
    override val serviceName: String = findServiceName { "gitlab-ci" },
    override val serviceNumber: String? = findServiceNumber {
        findSystemVariableValue(CI_CONCURRENT_ID)
    },
    override val serviceJobId: String? = findServiceJobId {
        findSystemVariableValue(CI_JOB_ID)
    },
    override val branchName: String? = null
) : PipelineImpl() {
    companion object {
        const val CI_CONCURRENT_ID = "CI_CONCURRENT_ID"
        const val CI_JOB_ID = "CI_JOB_ID"
        const val CI_COMMIT_REF_NAME = "CI_COMMIT_REF_NAME"
    }
}

class LocalPipeline(
    override val serviceName: String = findServiceName { "local-ci" },
    override val serviceNumber: String? = environment[CI_BUILD_NUMBER],
    override val serviceJobId: String? = findServiceJobId { null },
    override val branchName: String? = null
) : PipelineImpl() {

    companion object {
        const val CI_NAME = "CI_NAME"
        const val CI_BUILD_NUMBER = "CI_BUILD_NUMBER"
        const val JOB_NUM = "JOB_NUM"
    }

}