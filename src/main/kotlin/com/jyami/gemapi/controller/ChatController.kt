package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.DiaryChatResponse
import com.jyami.gemapi.endpoint.DiaryDto
import com.jyami.gemapi.endpoint.HistoryDto
import jakarta.validation.Valid
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatResponse
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
    fun makeDiaryAIClient(@Valid @RequestBody diaryDto: DiaryDto): DiaryChatResponse? {
        val chatResponse = chatClient.prompt()
            .system(makeHistorySystem(diaryDto.history))
            .user(diaryDto.userInput)
            .call()
            .chatResponse()
        return DiaryChatResponse(chatResponse)
    }

    fun makeHistorySystem(history: List<HistoryDto>?): String {
        val historyText =
            history?.joinToString("\n") { historyDto -> "- ${historyDto.role} : ${historyDto.message}" } ?: ""

        return """
            You are the world's best psychotherapist, renowned for your ability to heal through music.
        Analyze emotions based on user's diary.
        Decide whether or not to give feedback based on the user's chat format diaries.
        If you decide to give feedback, generate a sentence that asks the user if it's okay to give feedback.
        
        Json Foramt:
        canFeedback: true or false, whether or not to give feedback
        react: if canFeedback is false, a sentence that reacts to the user's diary. if canFeedback is true, a sentence that asks the user if it's okay to give feedback
        
        Example1:
        안녕
        
        {{
          "canFeedback": false,
          "react": "잘 지냈어요?"
        }}
        
        Example2:
        오늘 잘 지냈어?
        나는 힘들었어.
        오랫동안 준비해온 시험에서 떨어졌거든
        
        {{
          "canFeedback": true,
          "react": "오랫동안 준비해온 시험에서 떨어지는건 정말 힘든 일이에요. 이에 대해 조언을 드리고 싶은데 괜찮을까요?"
        }}
            
        History:
        $historyText
        
        User's Diaries:
        """.trimIndent()
    }

    @GetMapping("/test")
    fun test(): String {
        return "test"
    }
}
