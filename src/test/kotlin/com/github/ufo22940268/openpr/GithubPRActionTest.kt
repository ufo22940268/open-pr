package com.github.ufo22940268.openpr

import git4idea.repo.GitRemote
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GithubPRActionTest {

    @Test
    fun testBuildGithubURLForGithub() {
        val url = "git@github.com:ufo22940268/GitLink.git"
        val remote = GitRemote("origin", listOf(url), listOf(), listOf(), listOf())
        val githubURL = GithubPRAction().buildGithubURL("123123", remote)
        assertEquals("https://github.com/ufo22940268/GitLink/pull/123123", githubURL)
    }

    @Test
    fun testBuildGithubURLForOtherPlatform() {
        val url = "git@gitlab.com:ufo22940268/GitLink.git"
        val remote = GitRemote("origin", listOf(url), listOf(), listOf(), listOf())
        val githubURL = GithubPRAction().buildGithubURL("123123", remote)
        assertNull(githubURL)
    }

    @Test
    fun testConvertGithubURL() {
        val url = "git@github.com:UrbanCompass/uc-frontend.git"
        val remote = GitRemote("origin", listOf(url), listOf(), listOf(), listOf())
        val githubURL = GithubPRAction().buildGithubURL("123123", remote)
        assertEquals("https://github.com/UrbanCompass/uc-frontend/pull/123123", githubURL)
    }

    @Test
    fun testBuildGithubHTTPSURLForGithub() {
        val url = "https://github.com/ufo22940268/GitLink.git"
        val remote = GitRemote("origin", listOf(url), listOf(), listOf(), listOf())
        val githubURL = GithubPRAction().buildGithubURL("123123", remote)
        assertEquals("https://github.com/ufo22940268/GitLink/pull/123123", githubURL)
    }
}