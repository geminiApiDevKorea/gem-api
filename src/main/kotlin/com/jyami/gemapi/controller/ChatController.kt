package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.DiaryChatRequest
import com.jyami.gemapi.endpoint.DiaryChatResponse
import com.jyami.gemapi.endpoint.History
import com.jyami.gemapi.service.AISystemMessageConst.makeDiarySystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendSystem
import jakarta.validation.Valid
import org.springframework.ai.chat.client.ChatClient
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/chats")
@Validated
class ChatController(
    private val chatClient: ChatClient
) {

    @PostMapping("/prompt")
    fun makeDiaryAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest): DiaryChatResponse? {
        val chatResponse = chatClient.prompt()
            .system(makeDiarySystem(diaryDto.history))
            .user(diaryDto.userInput)
            .call()
            .chatResponse()
        return DiaryChatResponse(chatResponse)
    }


    @PostMapping("/feedback")
    fun makeDiaryFeedbackAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest): DiaryChatResponse? {
        val chatResponse = chatClient.prompt()
            .system(makeMusicRecommendSystem(diaryDto.history))
            .user(diaryDto.userInput)
            .call()
            .chatResponse()
        return DiaryChatResponse(chatResponse)
    }

    @GetMapping("/test")
    fun test(): String {
        return "test"
    }
}
