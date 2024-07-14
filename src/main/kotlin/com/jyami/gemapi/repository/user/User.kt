package com.jyami.gemapi.repository.user

import com.fasterxml.jackson.module.kotlin.convertValue
import com.jyami.gemapi.utils.MapperUtil
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val id: String,
    val name: String,
    val email: String
) : UserDetails {

    fun toMap(): Map<String, Any> {
        return MapperUtil.MAPPER.convertValue<Map<String, Any>>(this)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return name
    }
}
