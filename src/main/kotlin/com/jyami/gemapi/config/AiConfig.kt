package com.jyami.gemapi.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiConfig(
    private final val chatClientBuilder: ChatClient.Builder
) {

    @Bean
    fun chatClient(): ChatClient{
        return chatClientBuilder
            .defaultSystem("You are a friendly chat bot that answers question in the voice of a Pirate")
            .build()
    }

}