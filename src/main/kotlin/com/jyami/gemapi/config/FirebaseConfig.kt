package com.jyami.gemapi.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.jyami.gemapi.utils.logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FirebaseConfig {

    @Bean
    fun firebaseApp(): FirebaseApp {

        val serviceAccount = this::class.java.classLoader.getResourceAsStream("firebase/account.json")
        val options: FirebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        logger().info("Firebase init success")

        return FirebaseApp.initializeApp(options)?: throw RuntimeException("FirebaseApp init failed")
    }

    @Bean
    fun getFirebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(firebaseApp)

}
