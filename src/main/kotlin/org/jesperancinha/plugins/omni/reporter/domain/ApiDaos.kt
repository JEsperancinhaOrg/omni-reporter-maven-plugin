package org.jesperancinha.plugins.omni.reporter.domain

import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport

internal val httpTransport: HttpTransport = NetHttpTransport()

internal val httpRequestFactory: HttpRequestFactory = httpTransport.createRequestFactory()

internal interface ApiClient<REPORT, RESPONSE> {
    val token: String
    val url: String?
    fun submit(report: REPORT): RESPONSE?
}