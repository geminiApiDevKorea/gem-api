package com.jyami.gemapi.endpoint

import com.fasterxml.jackson.annotation.JsonUnwrapped
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate


data class GetDiaryResponse(
    @field:JsonUnwrapped
    val dateMap: Map<String, DailyDiary> = emptyMap() // key : YYYY-MM-DD
){
    data class DailyDiary(
        val title: String,
        val music: String,
        val contents: List<ChatContent>,
    )

    data class ChatContent(
        val role: String,
        val message: String,
    )
}

data class AddDailyDiaryRequest(
//    val userId: String,
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val dateTime: LocalDate,
    val title: String,
    val contents: List<ChatContent>,
    val type: String,
    val tag: List<String>?,
    val music: MusicContents
){
    data class ChatContent(
        @field:Pattern(regexp = "assistant|user", message = "Role must be either 'assistant' or 'user'")
        val role: String,
        @field:NotBlank
        val message: String,
    )
}

data class AddDailyDiaryResponse(
    val title: String,
    val music: String?,
): ResponseDto()

