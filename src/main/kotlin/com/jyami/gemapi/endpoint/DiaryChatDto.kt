package com.jyami.gemapi.endpoint

import com.fasterxml.jackson.annotation.JsonUnwrapped
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.ai.chat.model.ChatResponse

data class DiaryChatRequest(
    @field:NotBlank
    val userInput: String,
    @field:Valid
    val history: List<@Valid History>?,
)

data class History(
    @field:Pattern(regexp = "assistant|user", message = "Role must be either 'assistant' or 'user'")
    val role: String, // assistant / user

    @field:NotBlank
    val message: String
)

data class DiaryChatResponse(
    @field:JsonUnwrapped
    val chatResponse: ChatResponse
) : ResponseDto()

