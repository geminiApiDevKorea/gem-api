package com.jyami.gemapi.controller

import com.jyami.gemapi.endpoint.AgreementRequest
import com.jyami.gemapi.endpoint.EmptyResponse
import com.jyami.gemapi.endpoint.StatusCode
import com.jyami.gemapi.endpoint.UserInfoResponse
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.service.AuthService
import com.jyami.gemapi.service.UserService
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
    fun registerUser(
        @RequestHeader("Authorization") authorization: String
    ): UserInfoResponse {
        val validateAndGetUserToken = authService.validateAndGetUserToken(authorization)

        val user = userService.loadUserById(validateAndGetUserToken.uid)
        if (user == null) {
            val savedUser = userService.saveUser(validateAndGetUserToken)
            return UserInfoResponse(savedUser)
        }
        return UserInfoResponse(user)
    }

    @PutMapping("/agreement")
    fun agreementTrue(
        authentication: Authentication,
        @RequestBody agreementRequest: AgreementRequest
    ): UserInfoResponse {
        val user = (authentication.principal as User)
        userService.agreementUpdate(user, agreementRequest.agreement)
        return UserInfoResponse(user)
    }

    @GetMapping("/me")
    fun getUserMe(authentication: Authentication): UserInfoResponse {
        val user = (authentication.principal as User)
        return UserInfoResponse(user)
    }
}
