package com.github.ufo22940268.openpr

import git4idea.repo.GitRemote

enum class URLType {
    HTTPS, SSH
}

abstract class PlatformSource(open val remote: GitRemote, prId: String) {
    val remoteURL: String?
        get() = remote.firstUrl

    val urlType: URLType?
        get() {
            return remoteURL?.run {
                return if (contains("@")) {
                    URLType.SSH
                } else {
                    URLType.HTTPS
                }
            }
        }

    abstract fun isMatch(): Boolean
    abstract fun convert(): String

}