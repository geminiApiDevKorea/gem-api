package com.jyami.gemapi.repository.user

interface UserRepository {

    fun findUserById(id: String): User

    fun findUserByEmail(email: String): User

    fun saveUser(user: User)
}
