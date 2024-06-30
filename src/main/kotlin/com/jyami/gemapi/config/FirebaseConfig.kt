package com.jyami.gemapi.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.jyami.gemapi.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseConfig {

    @Bean
    fun firebaseApp(@Value("\${firebase.databaseUrl}") databaseUrl: String): FirebaseApp? {

        val serviceAccount = this::class.java.classLoader.getResourceAsStream("firebase/account.json")

        logger().warn(serviceAccount.toString())
        val options: FirebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl(databaseUrl)
            .build()

        logger().info("Firebase init success")

        return FirebaseApp.initializeApp(options)?: throw RuntimeException("FirebaseApp init failed")
    }

    @Bean
    fun getFirebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(firebaseApp)

}