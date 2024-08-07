package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.DiaryChatRequest
import com.jyami.gemapi.endpoint.DiaryChatResponse
import com.jyami.gemapi.endpoint.DiaryFeedbackChatResponse
import com.jyami.gemapi.service.AISystemMessageConst.makeDiarySystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendFromPostSystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendSystem
import com.jyami.gemapi.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/chats")
@Validated
class ChatController(
    private val chatService: ChatService,
) {

    @PostMapping("/prompt")
    @Operation(
        summary = "API used for conversational diaries.",
        description = "Engage in conversation with Gemini AI until the response's canFeedback is true.\n\n" +
                "When canFeedback is true, call the /feedback API.",
        parameters = [
            Parameter(`in`=ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
        ]
    )
    fun makeDiaryAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest): DiaryChatResponse? {
        val chatResponse = chatService.makeChatResponse(diaryDto.userInput, makeDiarySystem(diaryDto.history))
        return DiaryChatResponse(chatResponse)
    }


    @PostMapping("/feedback")
    @Operation(
        summary = "API for receiving recommended music for a diary",
        description = "For feedback on a diary, music is recommended internally using Gemini AI, and detailed information such as URLs is extracted via the YouTube API.\n\n" +
                "If canFeedback is true from the /prompt API, call this API. In this case, the type must be specified as 'chat'.",
        parameters = [
            Parameter(`in`=ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
            Parameter(`in`=ParameterIn.QUERY, name = "type", description = "'chat' for conversational diary API, 'post' for post-type diary API", required = true),
        ]
    )
    fun makeDiaryFeedbackAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest,
                                  @RequestParam("type", required = true)  type: String): DiaryFeedbackChatResponse {
        val chatMusicResponse = when (type) {
            "chat" -> chatService.makeChatResponseToMusic(diaryDto.userInput, makeMusicRecommendSystem(diaryDto.history))
            "post" -> chatService.makeChatResponseToMusic(diaryDto.userInput, makeMusicRecommendFromPostSystem())
            else -> throw IllegalArgumentException("type is not valid")
        }

        val musicResponse = chatService.musicApiResponse(chatMusicResponse)

        return DiaryFeedbackChatResponse(chatMusicResponse, musicResponse)
    }

}
