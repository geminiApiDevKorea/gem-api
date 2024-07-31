package com.jyami.gemapi.endpoint


data class ChatMusicResponse(
    val comment: String = "",
    val song: SongResponse? = null
){
    data class SongResponse(
        val singer: String? = null,
        val title: String? = null,
        val reason: String? = null
    )
}

data class ChatPromptResponse(
    val canFeedback: Boolean = false,
    val react: String = ""
)

data class MusicApiResponse(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val thumbnailUrl : String
)
