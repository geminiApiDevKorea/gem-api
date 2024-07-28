package com.jyami.gemapi.repository.diary

import com.fasterxml.jackson.annotation.JsonUnwrapped

// 여기는 firebase에서 바로 toObject를 안해서 null 처리를 안해도 된다.
data class Diary(
    @field:JsonUnwrapped
    val dateMap: Map<String, DailyDiary> = emptyMap(), // key: YYYY-MM-DD // value: DailyDiaryId
)

data class DailyDiary(
    val title: String,
    val music: MusicContent,
    val type: String,
    val tag: List<String>? = null,
    val contents: List<ChatContent>,
)

data class ChatContent(
    val role: String,
    val message: String
)

data class MusicContent(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val thumbnailUrl : String
)
