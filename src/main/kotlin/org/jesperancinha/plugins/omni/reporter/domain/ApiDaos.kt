package org.jesperancinha.plugins.omni.reporter.domain

internal interface ApiClient<REPORT, RESPONSE> {
    val token: String
    val url: String?
    fun submit(report: REPORT): RESPONSE?
}