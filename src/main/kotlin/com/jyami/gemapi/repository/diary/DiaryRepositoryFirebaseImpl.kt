package com.jyami.gemapi.repository.diary

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.FieldValue
import com.google.cloud.firestore.Firestore
import com.jyami.gemapi.repository.FirebaseUtil.performBlockOperation
import com.jyami.gemapi.repository.FirebaseUtil.toMap


class DiaryRepositoryFirebaseImpl(
    private val firestore: Firestore
) : DiaryRepository {

    companion object {
        const val COLLECTION_NAME = "diary"
    }

    override fun findAllDailyDiary(userId: String) : MutableMap<String, Any>? {
        val documentSnapshot = firestore.collection(COLLECTION_NAME)
            .document(userId)
            .get()
            .get()

        return documentSnapshot.data

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
