package com.jyami.gemapi.controller

import com.google.firebase.auth.UserInfo
import com.jyami.gemapi.endpoint.EmptyResponse
import com.jyami.gemapi.endpoint.RegisterInfoRequest
import com.jyami.gemapi.endpoint.StatusCode
import com.jyami.gemapi.endpoint.UserInfoResponse
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.service.AuthService
import com.jyami.gemapi.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController


@RestController("user")
class UserController(
    val userService: UserService,
    val authService: AuthService

) {

    @PostMapping
    fun registerUser(
        @RequestHeader("Authorization") authorization: String,
        @RequestBody userDto: RegisterInfoRequest
    ): EmptyResponse {
        val validateAndGetUserToken = authService.validateAndGetUserToken(authorization)
        userService.saveUser(userDto, validateAndGetUserToken)
        return EmptyResponse(StatusCode.OK)
    }

    @GetMapping("/me")
    fun getUserMe(authentication: Authentication): UserInfoResponse {
        val user = (authentication.getPrincipal() as User)
        return UserInfoResponse(user)
    }
}
