package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.DiaryChatRequest
import com.jyami.gemapi.endpoint.DiaryChatResponse
import com.jyami.gemapi.endpoint.DiaryFeedbackChatResponse
import com.jyami.gemapi.service.AISystemMessageConst.makeDiarySystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendFromPostSystem
import com.jyami.gemapi.service.AISystemMessageConst.makeMusicRecommendSystem
import com.jyami.gemapi.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/chats")
@Validated
class ChatController(
    private val chatService: ChatService,
) {

    @PostMapping("/prompt")
    @Operation(summary = "대화형 일기로 대화를 할 때 사용하는 API.",
        description = "응답의 canFeedBack 이 true가 나올때까지 유저와 gemini AI와 대화를 진행합니다.\n\n" +
            "canFeedBack이 true가 나오면 /feedback API를 호출합니다.",
        parameters = [
            Parameter(`in`=ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
        ]
    )
    fun makeDiaryAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest): DiaryChatResponse? {
        val chatResponse = chatService.makeChatResponse(diaryDto.userInput, makeDiarySystem(diaryDto.history))
        return DiaryChatResponse(chatResponse)
    }


    @PostMapping("/feedback")
    @Operation(summary = "일기에 대한 추천 음악을 받기 위한 API",
        description = "일기에 대한 피드백을 위해 내부적으로 gemini AI를 이용해 음악을 추천받고, 자세한건 youtube api로 url을 추출합니다.\n\n" +
            "만약 /prompt API 에서 canFeedBack이 true가 나오면 이 API를 호출합니다. 이때 type을 chat으로 지정해야합니다.",
        parameters = [
            Parameter(`in`=ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
            Parameter(`in`=ParameterIn.QUERY, name = "type", description = "chat인 경우는 대화형 일기를 위한 API, post인 경우는 post형 일기를 위한 API", required = true),
        ],
    )
    fun makeDiaryFeedbackAIClient(@Valid @RequestBody diaryDto: DiaryChatRequest,
                                  @RequestParam("type", required = true)  type: String): DiaryFeedbackChatResponse {
        val chatMusicResponse = when (type) {
            "chat" -> chatService.makeChatResponseToMusic(diaryDto.userInput, makeMusicRecommendSystem(diaryDto.history))
            "post" -> chatService.makeChatResponseToMusic(diaryDto.userInput, makeMusicRecommendFromPostSystem())
            else -> throw IllegalArgumentException("type is not valid")
        }

        val musicResponse = chatService.musicApiResponse(chatMusicResponse)

        return DiaryFeedbackChatResponse(chatMusicResponse, musicResponse)
    }

}
