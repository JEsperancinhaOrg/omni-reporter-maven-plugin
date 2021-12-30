package org.jesperancinha.plugins.omni.reporter.domain

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JsonMappingConfiguration {
    companion object {
        val objectMapper = jacksonObjectMapper().apply { propertyNamingStrategy = SnakeCaseStrategy() }
    }
}
