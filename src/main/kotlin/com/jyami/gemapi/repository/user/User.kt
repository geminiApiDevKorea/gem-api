package com.jyami.gemapi.repository.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

// null 처리는 firebase 를 사용함에 따라 어쩔 수 없이 발생한 문제이다.
data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val agreement: Boolean = false
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return name.orEmpty()
    }
}
