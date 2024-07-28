package com.jyami.gemapi.service

import com.jyami.gemapi.endpoint.AddDailyDiaryRequest
import com.jyami.gemapi.endpoint.GetDiaryResponse
import com.jyami.gemapi.endpoint.GetDiaryResponse.DailyDiaryResponse
import com.jyami.gemapi.repository.diary.ChatContent
import com.jyami.gemapi.repository.diary.DailyDiary
import com.jyami.gemapi.repository.diary.Diary
import com.jyami.gemapi.repository.diary.DiaryRepository
import com.jyami.gemapi.repository.diary.MusicContent
import com.jyami.gemapi.utils.MapperUtil.DATETIME_FORMATTER
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DiaryService(
    val diaryRepository: DiaryRepository
) {

    fun findAllDailyDiary(userId: String): List<DailyDiaryResponse> {
        return diaryRepository.findAllDailyDiary(userId)
            .dateMap
            .toSortedMap(Comparator.reverseOrder())
            .map { (key, value) -> parseDailyDiary(key, value) }
    }

    fun findMonthDailyDiary(userId: String, targetMonth: String): List<DailyDiaryResponse> {
        return diaryRepository.findAllDailyDiary(userId)
            .dateMap
            .filterKeys { key -> key.startsWith(targetMonth) }
            .toSortedMap(Comparator.reverseOrder())
            .map { (key, value) -> parseDailyDiary(key, value) }
    }

    fun pagingDailyDiary(userId: String, offsetTargetDate: String, limit: Int): List<DailyDiaryResponse> {
        val targetDate = LocalDate.parse(offsetTargetDate, DATETIME_FORMATTER)

        return diaryRepository.findAllDailyDiary(userId)
            .dateMap
            .toSortedMap(Comparator.reverseOrder())
            .asIterable()
            .filter { (key, _) -> LocalDate.parse(key, DATETIME_FORMATTER).isBefore(targetDate) }
            .take(limit)
            .map { (key, value) -> parseDailyDiary(key, value) }
    }

    private fun parseDailyDiary(key: String, value: DailyDiary): DailyDiaryResponse {
        return with(value){
            DailyDiaryResponse(
                key,
                title,
                GetDiaryResponse.MusicContent(
                    music.id,
                    music.url,
                    music.title,
                    music.description,
                    music.thumbnailUrl
                ),
                contents.map{GetDiaryResponse.ChatContent(it.role, it.message)},
                type,
                tag
            )
        }
    }

    fun saveDailyDiary(userId: String, addDailyDiaryRequest: AddDailyDiaryRequest): DailyDiary {
        val dailyDiary = with(addDailyDiaryRequest) {
            val contents = this.contents.map { ChatContent(it.role, it.message) }
            val musicContent = with(music){ MusicContent(id, url, title, description, thumbnailUrl) }
            DailyDiary(title = title, contents = contents, music = musicContent, tag = tag, type = type)
        }

        val findDailyDiary = diaryRepository.existDailyDiary(userId)

        val success = if (findDailyDiary) {
            diaryRepository.updateDailyDiary(userId, addDailyDiaryRequest.dateTime.toString(), dailyDiary)
        }else{
            diaryRepository.saveDailyDiary(userId, addDailyDiaryRequest.dateTime.toString(), dailyDiary)
        }

        if (success){
            return dailyDiary
        }
        throw RuntimeException("fail to save dailyDiary")
    }

    fun deleteDailyDiary(userId: String, targetDate: String): String {
        val findDailyDiary = diaryRepository.existDailyDiary(userId)

        val success = if (findDailyDiary) {
            diaryRepository.deleteDailyDiary(userId, targetDate)
        }else{
            throw IllegalArgumentException("not exist dailyDiary")
        }

        if (success){
            return targetDate
        }
        throw RuntimeException("fail to save dailyDiary")
    }

}
