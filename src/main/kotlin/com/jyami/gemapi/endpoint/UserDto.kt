package com.jyami.gemapi.endpoint

import com.jyami.gemapi.repository.user.User

// TODO:  현재 사용하는 추가 필드정보 없음. 있으면 추가
data class AgreementRequest(
    val agreement: Boolean,
)

data class UserInfoResponse(
    val name: String, val email: String, val agreement: Boolean
) : ResponseDto() {
    constructor(user: User) : this(user.name ?: "", user.email ?: "", user.agreement)
}
