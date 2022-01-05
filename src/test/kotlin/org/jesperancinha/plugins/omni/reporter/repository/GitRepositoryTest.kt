package org.jesperancinha.plugins.omni.reporter.repository

import io.kotest.matchers.nulls.shouldNotBeNull
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.RepositoryBuilder
import org.junit.jupiter.api.Test
import java.io.File

internal class GitRepositoryTest {

    @Test
    fun `should get branch name`() {
        val resource = javaClass.getResource("/")
        resource.shouldNotBeNull()
        val repo = RepositoryBuilder().findGitDir(File(resource.toURI())).build()
        repo.branch.shouldNotBeNull()
        val exactRef = repo.exactRef("HEAD")
        exactRef.shouldNotBeNull()
        exactRef.target.name.shouldNotBeNull()
        val shortenRefName = Repository.shortenRefName(repo.fullBranch)
        shortenRefName.shouldNotBeNull()
    }
}