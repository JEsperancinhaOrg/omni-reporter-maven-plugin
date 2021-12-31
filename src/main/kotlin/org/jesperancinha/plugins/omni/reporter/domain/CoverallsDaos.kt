package org.jesperancinha.plugins.omni.reporter.domain

data class CoverallsResponse(
    val message: String,
    val error: Boolean,
    val url: String?,
)

data class SourceFile(
    val name: String,
    val sourceDigest: String,
    val coverage: Array<Int?> = arrayOf(),
    val branches: Array<String>? = null,
    val source: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SourceFile) return false

        if (name != other.name) return false
        if (sourceDigest != other.sourceDigest) return false
        if (!coverage.contentEquals(other.coverage)) return false
        if (branches != null) {
            if (other.branches == null) return false
            if (!branches.contentEquals(other.branches)) return false
        } else if (other.branches != null) return false
        if (source != other.source) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + sourceDigest.hashCode()
        result = 31 * result + coverage.contentHashCode()
        result = 31 * result + (branches?.contentHashCode() ?: 0)
        result = 31 * result + (source?.hashCode() ?: 0)
        return result
    }
}


data class Git(
    val head: Head? = null,
    val branch: String? = null,
    val remotes: List<Remote>? = null,
)

data class Head(
    val id: String? = null,
    val authorName: String? = null,
    val authorEmail: String? = null,
    val committerName: String? = null,
    val committerEmail: String? = null,
    val message: String? = null,
)

data class Remote(
    val name: String,
    val url: String,
)

data class CoverallsReport(
    val repoToken: String,
    val serviceName: String,
    val sourceFiles: List<SourceFile> = mutableListOf(),
    val git: Git? = null,
    val serviceNumber: String? = null,
    val serviceJobId: String? = null,
    val servicePullRequest: String? = null,
    val parallel: Boolean? = null,
    val flagName: String? = null,
    val commitSha: String? = null,
    val runAt: String? = null
)