package com.jyami.gemapi.service

import com.jyami.gemapi.endpoint.AddDailyDiaryRequest
import com.jyami.gemapi.repository.diary.ChatContent
import com.jyami.gemapi.repository.diary.DailyDiary
import com.jyami.gemapi.repository.diary.DiaryRepository
import org.springframework.stereotype.Service

@Service
class DiaryService(
    val diaryRepository: DiaryRepository
) {

    fun findAllDailyDiary(userId: String): MutableMap<String, Any>? {
        return diaryRepository.findAllDailyDiary(userId)
    }

    fun saveDailyDiary(userId: String, addDailyDiaryRequest: AddDailyDiaryRequest): DailyDiary {
        val dailyDiary = with(addDailyDiaryRequest) {
            val contents = this.contents.map { ChatContent(it.role, it.message) }
            DailyDiary(title = title, contents = contents, music = music, tag = tag, type = type)
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
