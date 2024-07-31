package com.jyami.gemapi.service

import com.jyami.gemapi.endpoint.ChatMusicResponse
import com.jyami.gemapi.endpoint.ChatPromptResponse
import com.jyami.gemapi.endpoint.MusicApiResponse
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service


@Service
class ChatService(
    private val chatClient: ChatClient,
    private val youtubeService: YoutubeService
) {

    fun makeChatResponse(userInput: String, systemPrompt: String): ChatPromptResponse {
        return chatClient.prompt()
            .system(systemPrompt)
            .user(userInput)
            .call()
            .entity(ChatPromptResponse::class.java)
    }

    fun makeChatResponseToMusic(userInput: String, systemPrompt: String): ChatMusicResponse {
        return chatClient.prompt()
            .system(systemPrompt)
            .user(userInput)
            .call()
            .entity(ChatMusicResponse::class.java)
    }

    fun musicApiResponse(chatMusicResponse: ChatMusicResponse): MusicApiResponse {
        val searchMusic = youtubeService.searchMusic("${chatMusicResponse.song?.singer}, ${chatMusicResponse.song?.title}")
        return with(searchMusic){
            MusicApiResponse(
                id.videoId,
                "https://www.youtube.com/watch?v=${id.videoId}",
                snippet.title,
                snippet.description,
                snippet.thumbnails.medium.url
            )
        }
    }
}
