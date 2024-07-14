package com.jyami.gemapi.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object MapperUtil {
    val MAPPER = ObjectMapper()
        .registerKotlinModule()
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

}
