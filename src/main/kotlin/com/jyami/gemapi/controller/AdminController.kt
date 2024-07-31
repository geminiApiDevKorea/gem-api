package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.AddDailyDiaryRequest
import com.jyami.gemapi.endpoint.AddDailyDiaryResponse
import com.jyami.gemapi.endpoint.DiaryChatRequest
import com.jyami.gemapi.endpoint.DiaryChatResponse
import com.jyami.gemapi.endpoint.DiaryFeedbackChatResponse
import com.jyami.gemapi.endpoint.EmptyResponse
import com.jyami.gemapi.endpoint.GetDiaryResponse
import com.jyami.gemapi.endpoint.StatusCode
import com.jyami.gemapi.service.AISystemMessageConst.makeDiarySystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendFromPostSystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendSystem
import com.jyami.gemapi.service.ChatService
import com.jyami.gemapi.service.DiaryService
import com.jyami.gemapi.utils.MapperUtil.isValidYearMonth
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("admin")
class AdminController (
    private val diaryService: DiaryService,
    private val chatService: ChatService
){
    @GetMapping("/diary/{userId}")
    fun getDiaryTest(
        @PathVariable userId: String,
        @RequestParam("month") monthDate: String?, // YYYY-MM
        @RequestParam("offset") offsetTargetDate: String?, // YYYY-MM-dd
        @RequestParam("limit") limit: Int = 5,
    ): GetDiaryResponse {
        if (!monthDate.isNullOrEmpty()){
            require(isValidYearMonth(monthDate)) { "Invalid YearMonth Format" }
            return GetDiaryResponse(diaryService.findMonthDailyDiary(userId, monthDate))
        }
        val offset = offsetTargetDate ?: "2999-12-31"
        return GetDiaryResponse(diaryService.pagingDailyDiary(userId, offset, limit))
    }

    @GetMapping("/diary/all/{userId}")
    fun getAllDiaryTest(
        @PathVariable userId: String,
    ): GetDiaryResponse {
        return GetDiaryResponse(diaryService.findAllDailyDiary(userId))
    }


    @PostMapping("/diary/{userId}")
    fun saveDailyDiaryTest(
        @PathVariable userId: String,
        @RequestBody addDailyDiaryRequest: AddDailyDiaryRequest
    ) : AddDailyDiaryResponse {
        val dailyDiary = diaryService.saveDailyDiary(userId, addDailyDiaryRequest)
        return AddDailyDiaryResponse(dailyDiary.title, dailyDiary.music.title)
    }

    @DeleteMapping("/diary/{dateTime}/{userId}")
    fun deleteDailyDiaryTest(
        @PathVariable userId: String,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") dateTime: String,
    ): EmptyResponse {
        val dailyDiary = diaryService.deleteDailyDiary(userId, dateTime)
        return EmptyResponse(StatusCode.OK)
    }

    @PostMapping("/chats/prompt")
    fun makeDiaryAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest): DiaryChatResponse {
        val chatResponse = chatService.makeChatResponse(diaryDto.userInput, makeDiarySystem(diaryDto.history))
        return DiaryChatResponse(chatResponse)
    }


    @PostMapping("/chats/feedback")
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
