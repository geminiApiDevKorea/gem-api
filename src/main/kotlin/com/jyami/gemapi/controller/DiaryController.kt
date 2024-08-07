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
    @Operation(
        summary = "API to retrieve diaries",
        description = "Retrieve diaries for a specific month or use paging to browse diaries. By default, the results are sorted in descending order.\n\n" +
                "To retrieve diaries for a specific month, use the month parameter. For paging, use the offset and limit parameters together.\n\n" +
                "If no query parameters are provided, the default behavior is offset-based retrieval. If all query parameters are provided, the retrieval is month-based.\n\n" +
                "Offset-based retrieval fetches diaries before the specified date. (e.g., if offset=2021-08-01 and limit=5, it retrieves 5 diaries before 2021-07-31.)\n\n",
        parameters = [
            Parameter(`in`= ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
            Parameter(`in`= ParameterIn.QUERY, name = "month", description = "Used to retrieve diaries for a specific month. Must be in YYYY-MM format.", required = false),
            Parameter(`in`= ParameterIn.QUERY, name = "offset", description = "Specifies the reference date for paging. Must be in YYYY-MM-dd format.", required = false),
            Parameter(`in`= ParameterIn.QUERY, name = "limit", description = "Specifies the limit for paging. The default value is 5.", required = false),
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
    @Operation(
        summary = "API to save a diary entry",
        description = "Saves a diary entry. This is used to store responses after interactions with the chats API.\n\n" +
                "If a diary entry already exists for the specified date, it will be completely updated.",
        parameters = [
            Parameter(`in` = ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
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
    @Operation(
        summary = "API to delete a diary entry",
        description = "Deletes a diary entry for a specific date.",
        parameters = [
            Parameter(`in` = ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
            Parameter(`in` = ParameterIn.PATH, name = "dateTime", description = "Enter the date of the diary to delete. Must be in YYYY-MM-dd format.", required = true),
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
