package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.DiaryChatRequest
import com.jyami.gemapi.endpoint.DiaryChatResponse
import com.jyami.gemapi.service.AISystemMessageConst.makeDiarySystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendFromPostSystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendSystem
import com.jyami.gemapi.service.ChatService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/chats")
@Validated
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping("/prompt")
    fun makeDiaryAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest): DiaryChatResponse? {
        val chatResponse = chatService.makeChatResponse(diaryDto.userInput, makeDiarySystem(diaryDto.history))
        return DiaryChatResponse(chatResponse)
    }


    @PostMapping("/feedback")
    fun makeDiaryFeedbackAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest,
                                  @RequestParam("type", required = true)  type: String): DiaryChatResponse? {
        val chatResponse = when (type) {
            "chat" -> chatService.makeChatResponse(diaryDto.userInput, makeMusicRecommendSystem(diaryDto.history))
            "post" -> chatService.makeChatResponse(diaryDto.userInput, makeMusicRecommendFromPostSystem())
            else -> throw IllegalArgumentException("type is not valid")
        }

        val musicResponse = chatService.musicApiResponse(chatResponse)
        return DiaryChatResponse(chatResponse, musicResponse)
    }


    @GetMapping("/test")
    fun test(): String {
        return "test"
    }
}
