package com.jyami.gemapi.repository.user

import com.google.cloud.firestore.Firestore
import com.jyami.gemapi.repository.FirebaseUtil.performBlockOperation
import com.jyami.gemapi.repository.FirebaseUtil.toMap

class UserRepositoryFirebaseImpl(
    private val firestore: Firestore
) : UserRepository {

    companion object {
        const val COLLECTION_NAME = "user"
    }

    override fun findUserById(id: String): User? {
        return firestore.collection(COLLECTION_NAME)
            .document(id)
            .get()
            .get()
            .toObject(User::class.java)
    }

    override fun findUserByEmail(email: String): User? {
        return firestore.collection(COLLECTION_NAME)
            .whereEqualTo("email", email)
            .get()
            .get()
            .documents
            .map { it.toObject(User::class.java) }
            .firstOrNull()
    }

    override fun saveUser(user: User): Boolean {
        val documentRef = firestore.collection(COLLECTION_NAME)
            .document(user.id!!)

        return performBlockOperation {
            documentRef.set(user.toMap()).get()
        }
    }

    override fun updateAgreement(user: User, agreement: Boolean): Boolean {
        val documentRef = firestore.collection(COLLECTION_NAME)
            .document(user.id!!)

        return performBlockOperation {
            documentRef.update("agreement", agreement).get()
        }
    }

    override fun deleteUser(user: User): Boolean {
        val documentRef = firestore.collection(COLLECTION_NAME)
            .document(user.id!!)

        return performBlockOperation {
            documentRef.delete().get()
        }
    }

}
