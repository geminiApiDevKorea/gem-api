package com.jyami.gemapi.endpoint

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class DiaryChatRequest(
    @field:NotBlank
    @Schema(description = "User input", required = true)
    val userInput: String,

    @field:Valid
    @Schema(
        nullable = true,
        description = "Insert previous conversation history. (Null if there's no previous conversation or if it's a post-type conversation)",
        example = """[{"role":"user","message":"Hello"},{"role":"assistant","message":"Hello"}]""",
        required = false
    )
    val history: List<@Valid History>?,
)

data class History(
    @field:Pattern(regexp = "assistant|user", message = "Role must be either 'assistant' or 'user'")
    @Schema(pattern = "assistant|user", required = true)
    val role: String, // assistant / user

    @field:NotBlank
    @Schema(required = true)
    val message: String
)

data class DiaryChatResponse(
    @JsonProperty("chatPromptResponse")
    @Schema(description = "Chat response recommended by Gemini AI", required = true)
    val chatPromptResponse: ChatPromptResponse,
) : ResponseDto()

data class DiaryFeedbackChatResponse(
    @JsonProperty("chatPromptResponse")
    @Schema(description = "Music recommendation response recommended by Gemini AI", required = true)
    val chatMusicPromptResponse: ChatMusicResponse,
    @Schema(description = "Music information found via YouTube API", required = true)
    val music: MusicApiResponse
) : ResponseDto()
