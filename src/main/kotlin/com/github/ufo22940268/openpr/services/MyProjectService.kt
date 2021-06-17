package com.github.ufo22940268.openpr.services

import com.github.ufo22940268.openpr.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
