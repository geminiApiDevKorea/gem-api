package com.jyami.gemapi.service

import com.jyami.gemapi.endpoint.MusicContents
import com.jyami.gemapi.utils.MapperUtil.MAPPER
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatClient: ChatClient,
    private val youtubeService: YoutubeService
) {

    fun makeChatResponse(userInput: String, systemPrompt: String): ChatResponse {
        return chatClient.prompt()
            .system(systemPrompt)
            .user(userInput)
            .call()
            .chatResponse()
    }

    fun musicApiResponse(chatResponse: ChatResponse): MusicContents {
        val content = chatResponse.result.output.content
        val songResponse = MAPPER.readValue(content, ChatMusicResponse::class.java)

        val searchMusic = youtubeService.searchMusic("${songResponse.song?.singer}, ${songResponse.song?.title}")
        return with(searchMusic){
            MusicContents(
                id.videoId,
                "https://www.youtube.com/watch?v=${id.videoId}",
                snippet.title,
                snippet.description,
                snippet.thumbnails.medium.url)
        }
    }

    data class ChatMusicResponse(
        val comment: String,
        val song: SongResponse?
    ){
        data class SongResponse(
            val singer: String?,
            val title: String?
        )
    }





}
