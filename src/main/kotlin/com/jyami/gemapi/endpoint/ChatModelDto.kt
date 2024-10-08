package com.jyami.gemapi.endpoint

import io.swagger.v3.oas.annotations.media.Schema


data class ChatMusicResponse(
    @Schema(description = "System prompt : Comments that make users' better life")
    val comment: String = "",
    val song: SongResponse? = null
) {
    data class SongResponse(
        @Schema(description = "System prompt : recommended song's singer")
        val singer: String? = null,
        @Schema(description = "System prompt : recommended song's title")
        val title: String? = null,
        @Schema(description = "System prompt : why the song was recommended")
        val reason: String? = null
    )
}

data class ChatPromptResponse(
    @Schema(description = "System prompt : true or false, whether or not to give feedback")
    val canFeedback: Boolean = false,
    @Schema(description = "System prompt : if canFeedback is false, a sentence that reacts to the user's diary. if canFeedback is true, a sentence that asks the user if it's okay to give feedback")
    val react: String = ""
)

data class MusicApiResponse(
    @Schema(description = "YouTube Data API: Video identifier", required = true)
    val id: String,
    @Schema(description = "YouTube URL generated based on the id", required = true)
    val url: String,
    @Schema(description = "YouTube Data API: Video title", required = true)
    val title: String,
    @Schema(description = "YouTube Data API: Video description", required = true)
    val description: String,
    @Schema(description = "YouTube Data API: Thumbnail URL (width=320, height=180)", required = true)
    val thumbnailUrl: String
)
