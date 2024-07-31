package com.jyami.gemapi.endpoint

import com.jyami.gemapi.repository.user.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class AgreementRequest(
    @Schema(description = "유저의 약관 동의 여부")
    val agreement: Boolean,
)

data class UserSignupRequest(
    @Size(max = 10, message = "Nickname must be less than 10 characters")
    @Schema(description = "유저의 닉네임", maxLength = 10, required = true)
    val nickname: String,

    @field:Pattern(regexp = "MALE|FEMALE", message = "Role must be either 'MALE' or 'FEMALE'")
    @Schema(description = "유저의 성별", pattern = "MALE|FEMALE", required = true)
    val gender: String
)

data class UserInfoResponse(
    @Schema(description = "유저의 oauth2 이름", required = true)
    val name: String,
    @Schema(description = "유저의 oauth2 이메일", required = true)
    val email: String,
    @Schema(description = "유저의 약관 동의 여부", required = true)
    val agreement: Boolean,
    @Schema(description = "유저의 닉네임: 최근 업데이트 된 스펙으로 인해 현재는 nullable 로 처리되어있음.")
    val nickname: String?,
    @Schema(description = "유저의 성별: 최근 업데이트 된 스펙으로 인해 현재는 nullable 로 처리되어있음.")
    val gender: String?
) : ResponseDto() {
    constructor(user: User) : this(user.name ?: "", user.email ?: "", user.agreement, user.nickname, user.gender)
}
