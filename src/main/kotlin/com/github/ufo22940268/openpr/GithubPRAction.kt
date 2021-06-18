package com.github.ufo22940268.openpr

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import git4idea.GitUtil
import git4idea.commands.Git
import git4idea.commands.GitCommandResult
import git4idea.repo.GitRemote


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
                val head = GitUtil.getHead(repo).toString()
                val r = instance.lsRemote(e.project!!, e.project?.baseDir!!, remote)
                val prId = getGithubPRId(r, head) ?: return
                if (prId == null) {
                    //TODO
                    cancelText = "Github PR not found"
                    return
                }
                val url = buildGithubURL(prId, remote)
                url?.let {
                    com.intellij.ide.BrowserUtil.browse(it)
                }
            }
        }


        task.queue()
    }

    fun buildGithubURL(prId: String, remote: GitRemote): String? {
        val sources = listOf(GithubPlatformSource(remote, prId))
        return sources.find { it.isMatch() }?.convert()
    }

    private fun getGithubPRId(r: GitCommandResult, head: String): String? {
        for (str in r.output) {
            val (h, ref) = str.split('\t')
            if (h != head) continue
            val match = """refs/pull/(\d+)/head""".toRegex().find(ref)
            if (match != null && match.groups.first()?.value != null) {
                return match.groups.last()?.value
            }
        }
        return null
    }
}