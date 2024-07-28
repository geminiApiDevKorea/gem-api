package com.jyami.gemapi.endpoint

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
    val chatResponse: ChatResponse,
    val music : MusicContents? = null
) : ResponseDto()

data class MusicContents(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val thumbnailUrl : String
)
