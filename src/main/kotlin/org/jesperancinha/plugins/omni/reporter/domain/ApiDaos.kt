package org.jesperancinha.plugins.omni.reporter.domain

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class CoverallsResponse(
    val message: String,
    val error: Boolean,
    val url: String?,
)

class JsonMappingConfiguration {
    companion object {
        val objectMapper = jacksonObjectMapper().apply { propertyNamingStrategy = SnakeCaseStrategy() }
    }
}