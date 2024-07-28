package com.jyami.gemapi.repository.diary

import com.google.cloud.firestore.DocumentSnapshot

interface DiaryRepository {
    fun updateDailyDiary(userId: String, dateTime: String, dailyDiary: DailyDiary): Boolean
    fun existDailyDiary(userId: String): Boolean
    fun saveDailyDiary(userId: String, dateTime: String, dailyDiary: DailyDiary): Boolean
    fun findAllDailyDiary(userId: String): Diary
    fun deleteDailyDiary(userId: String, targetDate: String): Boolean
}
