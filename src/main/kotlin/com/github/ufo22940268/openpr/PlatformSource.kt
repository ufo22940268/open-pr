package com.github.ufo22940268.openpr

import git4idea.repo.GitRemote

abstract class PlatformSource(open val remote: GitRemote, prId: String) {
    val remoteURL: String?
        get() = remote.firstUrl

    abstract fun isMatch(): Boolean
    abstract fun convert(): String
}