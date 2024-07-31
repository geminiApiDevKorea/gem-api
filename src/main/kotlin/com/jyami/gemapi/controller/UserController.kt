package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.AgreementRequest
import com.jyami.gemapi.endpoint.UserSignupRequest
import com.jyami.gemapi.endpoint.UserInfoResponse
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.service.AuthService
import com.jyami.gemapi.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    val authService: AuthService

) {

    @PostMapping("")
    @Operation(summary = "회원가입 / 로그인 API",
        description = "토큰을 확인해서 존재하지 않는 유저라면 회원가입을 진행합니다. \n\n" +
            "이미 가입된 유저라면 본인이 갖고있는 정보를 리턴합니다. (로그인)",
        parameters = [
            Parameter(`in`= ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
        ]
    )
    fun registerUser(
        @RequestHeader("Authorization") authorization: String,
        @RequestBody userSignupRequest: UserSignupRequest
    ): UserInfoResponse {
        val validateAndGetUserToken = authService.validateAndGetUserToken(authorization)

        val user = userService.loadUserById(validateAndGetUserToken.uid)
        if (user == null) {
            val savedUser = userService.saveUser(validateAndGetUserToken, userSignupRequest)
            return UserInfoResponse(savedUser)
        }
        return UserInfoResponse(user)
    }

    @PutMapping("/agreement")
    @Operation(summary = "약관 동의 API",
        description = "약관 동의를 업데이트합니다. 본인의 정보를 받았을때, 약관 동의가 false 라면 클라에서 약관 동의 page를 띄워야합니다.",
        parameters = [
            Parameter(`in`= ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
        ])
    fun agreementTrue(
        authentication: Authentication,
        @RequestBody agreementRequest: AgreementRequest
    ): UserInfoResponse {
        val user = (authentication.principal as User)
        userService.agreementUpdate(user, agreementRequest.agreement)
        return UserInfoResponse(user)
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회 API",
        description = "내 정보를 조회합니다. 현재 로그인 API와 응답이 같으나 추후 확장 여지가 있습니다.",
        parameters = [
            Parameter(`in`= ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
        ]
    )
    fun getUserMe(authentication: Authentication): UserInfoResponse {
        val user = (authentication.principal as User)
        return UserInfoResponse(user)
    }
}
