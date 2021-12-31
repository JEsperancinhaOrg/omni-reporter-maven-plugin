package org.jesperancinha.plugins.omni.reporter.domain

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JsonMappingConfiguration {
    companion object {
        @JvmStatic
        val objectMapper = jacksonObjectMapper().apply { propertyNamingStrategy = SnakeCaseStrategy() }
    }
}

class PipelineConfigurationException(message:String) : RuntimeException(message) {
    companion object{
        @JvmStatic
        fun createParamFailException(param:String) = PipelineConfigurationException("Parameter $param is not configured!")
    }
}