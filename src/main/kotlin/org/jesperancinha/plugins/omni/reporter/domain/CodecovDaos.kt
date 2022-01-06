package org.jesperancinha.plugins.omni.reporter.domain

/**
 * Created by jofisaes on 30/12/2021
 */
class CodecovDaos(override val url: String, override val token: String) : ApiClient<String, String> {
    override fun submit(coverallsReport: String): String? {
        TODO("Not yet implemented")
    }
}