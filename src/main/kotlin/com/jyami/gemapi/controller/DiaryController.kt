package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.AddDailyDiaryRequest
import com.jyami.gemapi.endpoint.AddDailyDiaryResponse
import com.jyami.gemapi.endpoint.EmptyResponse
import com.jyami.gemapi.endpoint.GetDiaryResponse
import com.jyami.gemapi.endpoint.StatusCode
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.service.DiaryService
import com.jyami.gemapi.utils.MapperUtil.isValidYearMonth
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("diary")
class DiaryController(
    private val diaryService: DiaryService
) {

    @GetMapping
    fun getDiary(
        @AuthenticationPrincipal user: User,
        @RequestParam("month") monthDate: String?, // YYYY-MM
        @RequestParam("offset") offsetTargetDate: String?, // YYYY-MM-dd
        @RequestParam("limit") limit: Int = 5,
    ): GetDiaryResponse {
        val userId = user.id!!
        if (!monthDate.isNullOrEmpty()){
            require(isValidYearMonth(monthDate)) { "Invalid YearMonth Format" }
            return GetDiaryResponse(diaryService.findMonthDailyDiary(userId, monthDate))
        }
        val offset = offsetTargetDate ?: "2999-12-31"
        return GetDiaryResponse(diaryService.pagingDailyDiary(userId, offset, limit))
    }

    @PostMapping
    fun saveDailyDiary(
        @AuthenticationPrincipal user: User,
        @RequestBody addDailyDiaryRequest: AddDailyDiaryRequest
    ): AddDailyDiaryResponse {
        val dailyDiary = diaryService.saveDailyDiary(user.id!!, addDailyDiaryRequest)
        return AddDailyDiaryResponse(dailyDiary.title, dailyDiary.music.title)
    }

    @DeleteMapping("{dateTime}")
    fun deleteDailyDiary(
        @AuthenticationPrincipal user: User,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") dateTime: String,
    ): EmptyResponse {
        val dailyDiary = diaryService.deleteDailyDiary(user.id!!, dateTime)
        return EmptyResponse(StatusCode.OK)
    }


}
