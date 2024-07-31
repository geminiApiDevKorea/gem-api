package com.jyami.gemapi.service

import com.google.firebase.auth.FirebaseToken
import com.jyami.gemapi.endpoint.UserInfoRequest
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.repository.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun saveUser(firebaseToken: FirebaseToken, userInfoRequest: UserInfoRequest): User {
        val user = User(
            id = firebaseToken.uid,
            name = firebaseToken.name,
            email = firebaseToken.email,
            agreement = false,
            gender = userInfoRequest.gender,
            nickname = userInfoRequest.nickname
        )
        val success = userRepository.saveUser(user)
        if (success) {
            return user
        }
        throw throw RuntimeException("fail to save user server")
    }

    fun agreementUpdate(user: User, agreement: Boolean) {
        val success = userRepository.updateAgreement(user, agreement)
        if (!success) {
            throw RuntimeException("fail to update user info")
        }
    }

    fun loadUserByUserEmail(email: String): User? {
        return userRepository.findUserByEmail(email)
    }

    fun loadUserById(id: String): User? {
        return userRepository.findUserById(id)
    }

}
