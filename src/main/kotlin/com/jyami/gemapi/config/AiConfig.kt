package com.jyami.gemapi.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiConfig(
    private val chatClientBuilder: ChatClient.Builder
) {

    @Bean
    fun chatClient(): ChatClient{
        return chatClientBuilder.build()
    }

}
