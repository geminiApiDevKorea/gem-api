package com.jyami.gemapi.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.time.format.DateTimeFormatter

object MapperUtil {
    val MAPPER = ObjectMapper()
        .registerKotlinModule()
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    val DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val regex = Regex("""^\d{4}-(0[1-9]|1[0-2])$""")

    fun isValidYearMonth(input: String): Boolean {
        return regex.matches(input)
    }

}
