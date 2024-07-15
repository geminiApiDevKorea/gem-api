package com.jyami.gemapi.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.jyami.gemapi.utils.logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


@Configuration
class FirebaseConfig {

    @Bean
    fun firebaseApp(): FirebaseApp {
        val serviceAccount = getServiceAccountFromFile()
        val options: FirebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        logger().info("Firebase init success")

        return FirebaseApp.initializeApp(options)?: throw RuntimeException("FirebaseApp init failed")
    }

    private fun getServiceAccountFromFile(): InputStream {
        val serviceAccountPath = System.getenv(FIREBASE_CONFIG_PATH_ENV)
        val serviceAccount = if (serviceAccountPath != null) {
            FileInputStream(File(serviceAccountPath))
        } else { // 로컬 개발 환경의 경로 설정
            this::class.java.classLoader.getResourceAsStream("firebase/account.json")
        }
        return serviceAccount
    }

    @Bean
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(firebaseApp)

    companion object{
        const val FIREBASE_CONFIG_PATH_ENV = "FIREBASE_CONFIG_PATH"
    }

}
