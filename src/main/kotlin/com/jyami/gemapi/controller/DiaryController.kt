package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.AddDailyDiaryRequest
import com.jyami.gemapi.endpoint.AddDailyDiaryResponse
import com.jyami.gemapi.endpoint.EmptyResponse
import com.jyami.gemapi.endpoint.GetDiaryResponse
import com.jyami.gemapi.endpoint.StatusCode
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.service.DiaryService
import com.jyami.gemapi.utils.MapperUtil.isValidYearMonth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
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
    @Operation(summary = "일기를 조회하는 API",
        description = "특정 달의 일기를 조회하거나, 페이징을 통해 일기를 조회합니다. 기본적으로 내림차순으로 내려갑니다 \n\n" +
            "특정 달의 일기를 조회할 때는 month 파라미터를 사용하고, 페이징을 위한 기준일을 입력할 때는 offset 파라미터와 limit 파라미터를 함께 사용합니다.\n\n" +
            "아무런 쿼리 파라미터가 없다면, offset 기반 조회, 모든 쿼리 파라미터가 있다면 month 기반 조회입니다 \n\n" +
            "offset 기반 조회는 기준일 이전의 일기를 조회합니다. (ex: offset=2021-08-01, limit=5 이면 2021-07-31 이전의 5개의 일기를 조회합니다.)\n\n",
        parameters = [
            Parameter(`in`= ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
            Parameter(`in`= ParameterIn.QUERY, name = "month", description = "특정 달의 일기를 조회할 때 사용합니다. YYYY-MM 형식으로 입력해야합니다.", required = false),
            Parameter(`in`= ParameterIn.QUERY, name = "offset", description = "페이징을 위한 기준일을 입력합니다. YYYY-MM-dd 형식으로 입력해야합니다.", required = false),
            Parameter(`in`= ParameterIn.QUERY, name = "limit", description = "페이징을 위한 limit을 입력합니다. 기본값은 5입니다.", required = false),
        ]
    )
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
    @Operation(summary = "일기를 저장하는 API",
        description = "일기를 저장합니다. chats API에서의 유저와의 인터렉션이 끝나고 나서의 응답을 저장하는 용도입니다. \n\n" +
            "만약 이미 해당일의 일기가 있다면, 통째로 update 됩니다.",
        parameters = [
            Parameter(`in`= ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
        ]
    )
    fun saveDailyDiary(
        @AuthenticationPrincipal user: User,
        @RequestBody addDailyDiaryRequest: AddDailyDiaryRequest
    ): AddDailyDiaryResponse {
        val dailyDiary = diaryService.saveDailyDiary(user.id!!, addDailyDiaryRequest)
        return AddDailyDiaryResponse(dailyDiary.title, dailyDiary.music.title)
    }

    @DeleteMapping("{dateTime}")
    @Operation(summary = "일기를 삭제하는 API",
        description = "특정 날짜의 일기를 삭제합니다.",
        parameters = [
            Parameter(`in`= ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
            Parameter(`in`= ParameterIn.PATH, name = "dateTime", description = "삭제할 일기의 날짜를 입력합니다. YYYY-MM-dd 형식으로 입력해야합니다.", required = true),
        ]
    )
    fun deleteDailyDiary(
        @AuthenticationPrincipal user: User,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") dateTime: String,
    ): EmptyResponse {
        val dailyDiary = diaryService.deleteDailyDiary(user.id!!, dateTime)
        return EmptyResponse(StatusCode.OK)
    }


}
