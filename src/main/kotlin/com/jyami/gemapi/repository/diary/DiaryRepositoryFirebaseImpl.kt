package com.jyami.gemapi.repository.diary

import com.google.cloud.firestore.FieldValue
import com.google.cloud.firestore.Firestore
import com.jyami.gemapi.repository.FirebaseUtil.performBlockOperation
import com.jyami.gemapi.repository.FirebaseUtil.toMap
import com.jyami.gemapi.utils.MapperUtil.MAPPER


class DiaryRepositoryFirebaseImpl(
    private val firestore: Firestore
) : DiaryRepository {

    companion object {
        const val COLLECTION_NAME = "diary"
    }

    override fun findAllDailyDiary(userId: String) : Diary {
        val documentMap = firestore.collection(COLLECTION_NAME)
            .document(userId)
            .get()
            .get()
            .data

        val documents = documentMap?.mapValues { (key, value) -> makeDailyDiary(value) } ?: emptyMap()
        return Diary(dateMap = documents)
    }

    private fun makeDailyDiary(value: Any): DailyDiary {
        return MAPPER.convertValue(value, DailyDiary::class.java)
    }

    fun findDailyDiaryByMonth(userId: String){
        val docRef = firestore.collection(COLLECTION_NAME)
            .document(userId)
        docRef
    }

    override fun existDailyDiary(userId: String): Boolean {
        return firestore.collection(COLLECTION_NAME)
            .document(userId)
            .get()
            .get()
            .exists()
    }

    override fun updateDailyDiary(userId: String, dateTime: String, dailyDiary: DailyDiary): Boolean {
        val documentRef = firestore.collection(COLLECTION_NAME)
            .document(userId)

        val dailyDailyDocument = mapOf(dateTime to dailyDiary.toMap())

        return performBlockOperation {
            documentRef.update(dailyDailyDocument).get()
        }
    }

    override fun saveDailyDiary(userId: String, dateTime: String, dailyDiary: DailyDiary): Boolean {
        val documentRef = firestore.collection(COLLECTION_NAME)
            .document(userId)

        val dailyDailyDocument = mapOf(dateTime to dailyDiary.toMap())

        return performBlockOperation {
            documentRef.set(dailyDailyDocument).get()
        }
    }

    override fun deleteDailyDiary(userId: String, targetDate: String): Boolean {
        val documentRef = firestore.collection(COLLECTION_NAME)
            .document(userId)

        return performBlockOperation {
            documentRef.update(mapOf(targetDate to FieldValue.delete())).get()
        }
    }
}
