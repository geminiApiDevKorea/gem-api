package com.jyami.gemapi.repository.diary

import com.fasterxml.jackson.annotation.JsonUnwrapped

// null 처리는 firebase 를 사용함에 따라 어쩔 수 없이 발생한 문제이다.
data class Diary(
    @field:JsonUnwrapped
    val dateMap: Map<String, DailyDiary>? = null, // key: YYYY-MM-DD // value: DailyDiaryId
)

data class DailyDiary(
    val title: String? = null,
    val music: String? = null,
    val contents: List<ChatContent>? = null,
)

data class ChatContent(
    val role: String? = null,
    val message: String? = null,
    val localDateTime: String? = null // localDateTime
)
