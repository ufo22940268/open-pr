package org.jetbrains.plugins.template

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import git4idea.GitUtil
import git4idea.commands.Git

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
                println(r)
            }
        }

        task.queue()
    }
}