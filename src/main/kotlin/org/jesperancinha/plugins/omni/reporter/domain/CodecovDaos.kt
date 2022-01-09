package org.jesperancinha.plugins.omni.reporter.domain


val TRAVIS = "travis"
val BUILDBOT = "buildbot"
val CIRCLECI = "circleci"
val BUDDYBUILD = "buddybuild"
val SOLANO = "solano"
val TEAMCITY = "teamcity"
val APPVEYOR = "appveyor"
val WERCKER = "wercker"
val MAGNUM = "magnum"
val SHIPPABLE = "shippable"
val CODESHIP = "codeship"
val DRONE_IO = "drone.io"
val JENKINS = "jenkins"
val SEMAPHORE = "semaphore"
val GITLAB = "gitlab"
val BAMBOO = "bamboo"
val SNAP = "snap"
val BUILDKITE = "buildkite"
val BITRISE = "bitrise"
val GREENHOUSE = "greenhouse"
val CUSTOM = "custom"

/**
 * Created by jofisaes on 30/12/2021
 */
class CodecovDaos(override val url: String, override val token: String) : ApiClient<String, String> {
    override fun submit(coverallsReport: String): String? {
        TODO("Not yet implemented")
    }
}