package com.jyami.gemapi.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class ChatController(
    private final val chatClient: ChatClient
) {

    @GetMapping("/ai")
    fun generation(userInput: String?): ChatResponse {
        return chatClient.prompt()
            .user(userInput)
            .call()
            .chatResponse()
    }
}