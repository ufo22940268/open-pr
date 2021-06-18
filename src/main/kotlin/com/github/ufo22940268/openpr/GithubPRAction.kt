package com.github.ufo22940268.openpr

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import git4idea.GitUtil
import git4idea.commands.Git
import git4idea.commands.GitCommandResult
import git4idea.repo.GitRemote


abstract class PlatformSource(open val remote: GitRemote, prId: String) {
    val remoteURL: String?
        get() = remote.firstUrl

    abstract fun isMatch(): Boolean
    abstract fun convert(): String
}

//"git@github.com:ufo22940268/GitLink.git"
class GithubPlatformSource(override val remote: GitRemote, val prId: String) : PlatformSource(remote, prId) {
    override fun isMatch(): Boolean {
        return """git@github.com:.+/.+\.git""".toRegex().matches(remoteURL ?: "")
    }

    override fun convert(): String {
        val find = """git@github.com:(\w+)/(\w+).git""".toRegex().find(remoteURL!!)
            ?: throw Exception("${remoteURL} not valid url")
        val (userName, repoName) = find.destructured
        return "https://github.com/${userName}/${repoName}/pull/${prId}"
    }
}


public class GithubPRAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Opening PR") {
            override fun run(indicator: ProgressIndicator) {
                if (e.project == null) {
                    return
                }
                val instance = Git.getInstance()
                val repo = e.project?.let { GitUtil.getRepositories(it).first() } ?: return
                val remote = GitUtil.findRemoteByName(repo, "origin") ?: return
                val r = instance.lsRemote(e.project!!, e.project?.baseDir!!, remote)
                val prId = getGithubPRId(r)
                if (prId == null) {
                    //TODO PR
                    return
                }

                val url = buildGithubURL(prId, remote)
                print(url)
            }
        }

        task.queue()
    }

    fun buildGithubURL(prId: String, remote: GitRemote): String? {
        val sources = listOf(GithubPlatformSource(remote, prId))
        return sources.find { it.isMatch() }?.convert()
    }

    private fun getGithubPRId(r: GitCommandResult): String? {
        for (str in r.output) {
            val ref = str.split('\t')[1]
            val match = """refs/pull/(\d+)/head""".toRegex().find(ref)
            if (match != null && match.groups.first()?.value != null) {
                return match.groups.last()?.value
            }
        }
        return null
    }
}