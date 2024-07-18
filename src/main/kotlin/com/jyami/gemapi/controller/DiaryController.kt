package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.DiaryDto
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("diary")
class DiaryController(
    val aiService: AIService
) {

    @PostMapping("prompt")
    fun writeDiary(@RequestBody diaryDto: DiaryDto): ChatResponse? {
        val makeDiaryAIClient = aiService.makeDiaryAIClient(diaryDto.history ?: "", diaryDto.userInput)
        return makeDiaryAIClient
    }

}
