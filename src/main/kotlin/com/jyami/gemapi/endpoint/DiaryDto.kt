package com.jyami.gemapi.endpoint

import com.fasterxml.jackson.annotation.JsonUnwrapped
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate


// request와 response의 객체 구조가 동일하다.
data class GetDiaryResponse(
    @field:JsonUnwrapped
    val diary: List<DailyDiaryContent> = emptyList()
) : ResponseDto()

data class AddDailyDiaryRequest(
    @Schema(description = "Request to wrap and save a daily diary entry.", required = true)
    val dailyDiary: DailyDiaryContent,
)

data class DailyDiaryContent(
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date the diary was written.", example = "2021-08-01", pattern = "yyyy-MM-dd", required = true)
    val dateTime: LocalDate,
    @Schema(description = "Title of the diary.", required = true)
    val title: String,
    @Schema(description = "Content of the diary. Stores the content generated by the chats API.", required = true)
    val contents: List<ChatContent>,
    @field:Pattern(regexp = "post|chat", message = "Type must be either 'post' or 'chat'")
    @Schema(description = "Type of the diary. 'post' indicates a typical text format, 'chat' indicates a conversational diary format. Stores the content generated by the chats API.", required = true)
    val type: String,
    @Schema(description = "Tags for the diary. Processed as null if there are no tags.")
    val tag: List<String>?,
    @Schema(description = "Music information for the diary. Stores music information generated by the chats API and fetched from the YouTube API.", required = true)
    val music: MusicContent
) {
    data class ChatContent(
        @field:Pattern(regexp = "assistant|user", message = "Role must be either 'assistant' or 'user'")
        @Schema(required = true)
        val role: String,
        @field:NotBlank
        @Schema(required = true)
        val message: String,
    )

    data class MusicContent(
        @Schema(description = "YouTube video identifier.", required = true)
        val id: String,
        @Schema(description = "YouTube URL.", required = true)
        val url: String,
        @Schema(description = "Video title.", required = true)
        val title: String,
        @Schema(description = "Video description.", required = true)
        val description: String,
        @Schema(description = "Thumbnail URL fetched from the YouTube API (width=320, height=180).", required = true)
        val thumbnailUrl: String
    )
}

data class AddDailyDiaryResponse(
    @Schema(description = "Title of the saved diary.", required = true)
    val title: String,
    @Schema(description = "Title of the music associated with the saved diary.", required = true)
    val music: String,
) : ResponseDto()

