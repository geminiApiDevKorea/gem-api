package com.jyami.gemapi.service

import com.google.firebase.auth.FirebaseToken
import com.jyami.gemapi.endpoint.RegisterInfoRequest
import com.jyami.gemapi.repository.user.User
import com.jyami.gemapi.repository.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun saveUser(userDto: RegisterInfoRequest, firebaseToken: FirebaseToken){
        val user = User(id = firebaseToken.uid, name = firebaseToken.name, email = firebaseToken.email)
        userRepository.saveUser(user)
    }

    fun loadUserByUserEmail(email: String): User {
        return userRepository.findUserByEmail(email)
    }

    fun loadUserById(id: String): User {
        return userRepository.findUserById(id)
    }

}
