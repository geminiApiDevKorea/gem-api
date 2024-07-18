package com.jyami.gemapi.endpoint

import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.ai.chat.model.ChatResponse

data class DiaryChatResponse(
    @field:JsonUnwrapped
    val chatResponse: ChatResponse
) : ResponseDto()
