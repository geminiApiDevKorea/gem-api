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
    @Schema(description = "저장할 하루의 일기를 래핑해서 요청한다.", required = true)
    val dailyDiary: DailyDiaryContent,
)

data class DailyDiaryContent(
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "일기를 작성한 날짜", example = "2021-08-01", pattern = "yyyy-MM-dd", required = true)
    val dateTime: LocalDate,
    @Schema(description = "일기의 제목", required = true)
    val title: String,
    @Schema(description = "일기의 내용. chats API에서 생성한 내용을 저장합니다.", required = true)
    val contents: List<ChatContent>,
    @field:Pattern(regexp = "post|chat", message = "Role must be either 'post' or 'chat'")
    @Schema(description = "일기의 타입. post인 경우는 일반적인 글 형식, chat인 경우는 대화형 일기 형식입니다. chats API에서 생성한 내용을 저장합니다.", required = true)
    val type: String,
    @Schema(description = "일기의 태그. 없을 경우 null로 처리합니다")
    val tag: List<String>?,
    @Schema(description = "일기의 음악 정보. chats API에서 생성 + youtube API에서 가져온 음악 정보를 저장합니다.", required = true)
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
        @Schema(description = "youtube 비디오 식별값", required = true)
        val id: String,
        @Schema(description = "youtube url",required = true)
        val url: String,
        @Schema(description = "비디오 제목", required = true)
        val title: String,
        @Schema(description = "비디오 설명", required = true)
        val description: String,
        @Schema(description = "youtube api에서 가져온 썸네일 url (width=320, height=180)", required = true)
        val thumbnailUrl: String
    )
}

data class AddDailyDiaryResponse(
    @Schema(description = "저장된 일기의 제목", required = true)
    val title: String,
    @Schema(description = "저장된 일기의 음악 제목", required = true)
    val music: String,
) : ResponseDto()

