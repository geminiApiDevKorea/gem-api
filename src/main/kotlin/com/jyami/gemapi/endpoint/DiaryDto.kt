package com.jyami.gemapi.endpoint

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class DiaryDto(
    @field:NotBlank
    val userInput: String,
    @field:Valid
    val history: List<@Valid HistoryDto>?,
)

data class HistoryDto(
    @field:Pattern(regexp = "assistant|user", message = "Role must be either 'assistant' or 'user'")
    val role: String, // assistant / user

    @field:NotBlank
    val message: String
)
