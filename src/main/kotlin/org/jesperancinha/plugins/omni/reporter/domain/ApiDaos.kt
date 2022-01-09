package org.jesperancinha.plugins.omni.reporter.domain

import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport

internal val httpTransport: HttpTransport = NetHttpTransport()

internal val httpRequestFactory: HttpRequestFactory = httpTransport.createRequestFactory()

internal interface ApiClient<REPORT, RESPONSE> {
    val token: String?
    val apiToken: CodacyApiTokenConfig?
    val url: String?
    fun submit(report: REPORT): RESPONSE?

    fun responseText(httpResponse: HttpResponse): String? = null
}


abstract class ApiClientImpl<REPORT, RESPONSE> : ApiClient<REPORT, RESPONSE> {
    override val apiToken: CodacyApiTokenConfig? = null
}

internal const val REDACTED = "REDACTED"

fun String.redact(token: String?): String = token?.let { replace(token, REDACTED) } ?: this