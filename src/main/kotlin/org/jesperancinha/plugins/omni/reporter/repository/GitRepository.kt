package org.jesperancinha.plugins.omni.reporter.repository

import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.RepositoryBuilder
import org.eclipse.jgit.revwalk.RevWalk
import org.jesperancinha.plugins.omni.reporter.domain.Git
import org.jesperancinha.plugins.omni.reporter.domain.Head
import org.jesperancinha.plugins.omni.reporter.domain.Remote
import java.io.File

/**
 * Created by jofisaes on 01/01/2022
 */
class GitRepository(sourceDirectory: File) {

    private val repo = RepositoryBuilder().findGitDir(sourceDirectory).build()

    private val head = let {
        val revision = repo.resolve(Constants.HEAD)
        val commit = RevWalk(repo).parseCommit(revision)
        Head(
            revision.name,
            commit.authorIdent.name,
            commit.authorIdent.emailAddress,
            commit.committerIdent.name,
            commit.committerIdent.emailAddress,
            commit.fullMessage
        )
    }

    private val branch = repo.branch

    private val remotes = repo.config.getSubsections("remote")
        .map { Remote(it, repo.config.getString("remote", it, "url")) }

    val git = Git(head, branch, remotes)
}