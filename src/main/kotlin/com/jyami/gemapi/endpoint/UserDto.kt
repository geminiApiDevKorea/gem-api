package com.jyami.gemapi.endpoint

import com.jyami.gemapi.repository.user.User
import jakarta.validation.constraints.Pattern

// TODO:  현재 사용하는 추가 필드정보 없음. 있으면 추가
data class AgreementRequest(
    val agreement: Boolean,
)

data class UserInfoRequest(
    val nickname: String,
    @field:Pattern(regexp = "MALE|FEMALE", message = "Role must be either 'MALE' or 'FEMALE'")
    val gender: String
)

data class UserInfoResponse(
    val name: String, val email: String, val agreement: Boolean, val nickname: String?, val gender: String?
) : ResponseDto() {
    constructor(user: User) : this(user.name ?: "", user.email ?: "", user.agreement, user.nickname, user.gender)
}
