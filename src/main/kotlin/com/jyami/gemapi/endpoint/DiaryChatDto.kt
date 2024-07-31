package com.jyami.gemapi.endpoint

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class DiaryChatRequest(
    @field:NotBlank
    @Schema(description = "유저 입력", required = true)
    val userInput: String,

    @field:Valid
    @Schema(nullable = true,
        description = "이전 대화 history 를 넣는다. (이전대화가 없거나, post 형 대화일 경우 null)",
        example = """[{"role":"user","message":"안녕하세요"},{"role":"assistant","message":"안녕하세요"}]""",
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
    @Schema(description = "gemini AI에서 추천해준 대화 응답", required = true)
    val chatPromptResponse: ChatPromptResponse,
) : ResponseDto()


data class DiaryFeedbackChatResponse(
    @JsonProperty("chatPromptResponse")
    @Schema(description = "gemini AI에서 추천해준 음악 추천 응답", required = true)
    val chatMusicPromptResponse: ChatMusicResponse,
    @Schema(description = "YOUTUBE API 로 찾은 음악 정보", required = true)
    val music : MusicApiResponse
) : ResponseDto(){


}
