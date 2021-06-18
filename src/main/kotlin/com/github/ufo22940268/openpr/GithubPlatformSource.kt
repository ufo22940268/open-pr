package com.github.ufo22940268.openpr

import git4idea.repo.GitRemote

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