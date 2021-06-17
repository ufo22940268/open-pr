package org.jetbrains.plugins.template.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import git4idea.GitUtil
import git4idea.commands.Git
import org.jetbrains.plugins.template.services.MyProjectService

internal class MyProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        val instance = Git.getInstance()
        val repos = GitUtil.getRepositories(project)
        println("repo size2: " + repos.size)
        project.service<MyProjectService>()
    }
}
