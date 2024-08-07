package com.jyami.gemapi.endpoint

import com.jyami.gemapi.repository.user.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class AgreementRequest(
    @Schema(description = "User's agreement to terms and conditions")
    val agreement: Boolean,
)

data class UserSignupRequest(
    @Size(max = 10, message = "Nickname must be less than 10 characters")
    @Schema(description = "User's nickname", maxLength = 10, required = true)
    val nickname: String,

    @field:Pattern(regexp = "MALE|FEMALE", message = "Gender must be either 'MALE' or 'FEMALE'")
    @Schema(description = "User's gender", pattern = "MALE|FEMALE", required = true)
    val gender: String
)

data class UserInfoResponse(
    @Schema(description = "User's OAuth2 name", required = true)
    val name: String,
    @Schema(description = "User's OAuth2 email", required = true)
    val email: String,
    @Schema(description = "User's agreement to terms and conditions", required = true)
    val agreement: Boolean,
    @Schema(description = "User's nickname: Currently nullable due to recent spec updates.")
    val nickname: String?,
    @Schema(description = "User's gender: Currently nullable due to recent spec updates.")
    val gender: String?
) : ResponseDto() {
    constructor(user: User) : this(user.name ?: "", user.email ?: "", user.agreement, user.nickname, user.gender)
}
