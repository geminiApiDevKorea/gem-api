package com.jyami.gemapi.config

import com.google.cloud.vertexai.VertexAI
import com.google.cloud.vertexai.api.GenerationConfig
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiConfig {

    @Bean
    fun chatClient(): ChatClient {
        val chatModel = VertexAiGeminiChatModel(
            VertexAI(System.getenv("GEMINI_PROJECT_ID"), System.getenv("GEMINI_LOCATION"))
        )

        val generationConfig = GenerationConfig.newBuilder()
            .setResponseMimeType("application/json") // responseMimeType 을 설정할 수 없어서. 직접 접근하여 설정하였다.
            .setTemperature(0.8f)
            .build()

        val generationConfigField = VertexAiGeminiChatModel::class.java.getDeclaredField("generationConfig")
        generationConfigField.isAccessible = true
        generationConfigField.set(chatModel, generationConfig)

        return ChatClient.create(chatModel)
    }

}
