package com.jyami.gemapi.service

import com.google.firebase.auth.FirebaseToken
import com.jyami.gemapi.endpoint.UserSignupRequest
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.repository.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun saveUser(firebaseToken: FirebaseToken, userSignupRequest: UserSignupRequest): User {
        val user = User(
            id = firebaseToken.uid,
            name = firebaseToken.name,
            email = firebaseToken.email,
            agreement = false,
            gender = userSignupRequest.gender,
            nickname = userSignupRequest.nickname
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

    fun deleteUser(user: User) {
        val success = userRepository.deleteUser(user)
        if (!success) {
            throw RuntimeException("fail to delete user")
        }
    }

}
