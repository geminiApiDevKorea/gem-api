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
    @Operation(
        summary = "Sign-Up / Login API",
        description = "Checks the token and proceeds with sign-up if the user does not exist.\n\n" +
                "If the user is already registered, it returns the user's existing information (Login).",
        parameters = [
            Parameter(`in` = ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
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
    @Operation(
        summary = "Terms of Service Agreement API",
        description = "Updates the terms of service agreement. If the user's agreement is false, the client should display the terms of service agreement page.",
        parameters = [
            Parameter(`in` = ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
        ]
    )
    fun agreementTrue(
        authentication: Authentication,
        @RequestBody agreementRequest: AgreementRequest
    ): UserInfoResponse {
        val user = (authentication.principal as User)
        userService.agreementUpdate(user, agreementRequest.agreement)
        return UserInfoResponse(user)
    }

    @GetMapping("/me")
    @Operation(
        summary = "Retrieve My Information API",
        description = "Retrieves my information. Currently, the response is the same as the login API, but there is potential for future expansion.",
        parameters = [
            Parameter(`in` = ParameterIn.HEADER, name = "Authorization", description = "Bearer Token", required = true),
        ]
    )
    fun getUserMe(authentication: Authentication): UserInfoResponse {
        val user = (authentication.principal as User)
        return UserInfoResponse(user)
    }
}
