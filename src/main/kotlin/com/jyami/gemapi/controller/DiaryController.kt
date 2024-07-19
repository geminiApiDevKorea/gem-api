package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.AddDailyDiaryRequest
import com.jyami.gemapi.endpoint.AddDailyDiaryResponse
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.service.DiaryService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("diary")
class DiaryController(
    private val diaryService: DiaryService
) {

    @GetMapping
    fun getAllDiary(
        @AuthenticationPrincipal user: User,
    ): MutableMap<String, Any>? {
        return diaryService.findAllDailyDiary(user.id!!)
    }

    @GetMapping("{userId}")
    fun getAllDiary(
        @PathVariable userId: String,
    ): MutableMap<String, Any>? {
        return diaryService.findAllDailyDiary(userId)
    }

    @PostMapping
    fun saveDailyDiary(
        @AuthenticationPrincipal user: User,
        @RequestBody addDailyDiaryRequest: AddDailyDiaryRequest
    ): AddDailyDiaryResponse {
        val dailyDiary = diaryService.saveDailyDiary(user.id!!, addDailyDiaryRequest)
        return AddDailyDiaryResponse(dailyDiary.title!!, dailyDiary.music!!)
    }

    @PostMapping("/{userId}")
    fun saveDailyDiaryTest(
        @PathVariable userId: String,
        @RequestBody addDailyDiaryRequest: AddDailyDiaryRequest
    ) : AddDailyDiaryResponse {
        val dailyDiary = diaryService.saveDailyDiary(userId, addDailyDiaryRequest)
        return AddDailyDiaryResponse(dailyDiary.title!!, dailyDiary.music!!)
    }

}
