package com.jyami.gemapi.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import com.jyami.gemapi.endpoint.StatusCode
import com.jyami.gemapi.exception.AuthException
import com.jyami.gemapi.utils.RequestUtil
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(val firebaseAuth: FirebaseAuth) {

    fun validateAndGetUserToken(authorization: String): FirebaseToken {
        return try {
            val token = RequestUtil.getAuthorizationToken(authorization)
            firebaseAuth.verifyIdToken(token)
        } catch (e: IllegalArgumentException) {
            throw AuthException( StatusCode.INVALID_TOKEN, e.message)
        } catch (e: FirebaseAuthException) {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.message + "\"}"
            )
        }
    }
}
