package com.jyami.gemapi.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/chats")
class ChatController(
    private val chatClient: ChatClient
) {

    @PostMapping("/prompt")
    fun makeDiaryAIClient(history: String, userInput: String): ChatResponse? {
        return chatClient.prompt()
            .user(makeUserInput(history, userInput))
            .call()
            .chatResponse()
    }

    fun makeUserInput(history: String, userInput: String) = """
        History:
        $history
        
        User's Diaries:
        $userInput
    """.trimIndent()

    @GetMapping("/test")
    fun test(): String {
        return "test"
    }
}
