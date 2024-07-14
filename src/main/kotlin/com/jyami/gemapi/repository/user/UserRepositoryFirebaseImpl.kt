package com.jyami.gemapi.repository.user

import com.google.cloud.firestore.Firestore

class UserRepositoryFirebaseImpl(
    val firestore: Firestore
) : UserRepository {


    companion object{
        const val COLLECTION_NAME = "user"
    }

    override fun findUserById(id: String): User {
        return firestore.collection(COLLECTION_NAME)
            .document(id)
            .get()
            .get()
            .toObject(User::class.java)
            ?: throw RuntimeException("User Not Found")
    }

    override fun findUserByEmail(email: String): User {
        return firestore.collection(COLLECTION_NAME)
            .whereEqualTo("email", email)
            .get()
            .get()
            .documents
            .map { it.toObject(User::class.java) }
            .firstOrNull()
            ?: throw RuntimeException("User Not Found")
    }

    override fun saveUser(user: User) {
        val documentRef = firestore.collection(COLLECTION_NAME)
            .document(user.id)
        documentRef.set(user.toMap())
    }


}
