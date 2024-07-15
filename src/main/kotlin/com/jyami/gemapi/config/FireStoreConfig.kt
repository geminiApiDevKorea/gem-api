package com.jyami.gemapi.config

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import com.jyami.gemapi.repository.user.UserRepository
import com.jyami.gemapi.repository.user.UserRepositoryFirebaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FireStoreConfig {

    @Bean
    fun fireStore(firebaseApp: FirebaseApp): Firestore {
        return FirestoreClient.getFirestore("gem-api") ?: throw RuntimeException("FireStore init failed")
    }

    @Bean
    fun userRepository(firestore: Firestore): UserRepository {
        return UserRepositoryFirebaseImpl(firestore)
    }

}
