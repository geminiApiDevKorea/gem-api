package com.jyami.gemapi.repository.user

interface UserRepository {

    fun findUserById(id: String): User?

    fun findUserByEmail(email: String): User?

    fun saveUser(user: User): Boolean

    fun updateAgreement(user: User, agreement: Boolean): Boolean

    fun deleteUser(user: User) : Boolean
}
