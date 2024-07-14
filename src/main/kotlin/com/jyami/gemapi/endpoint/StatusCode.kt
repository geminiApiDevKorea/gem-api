package com.jyami.gemapi.endpoint

enum class StatusCode(val code: Int) {
    OK(-200),
    BAD_REQUEST(-400),
    UNAUTHORIZED(-401),
    USER_NOT_FOUND(-404),
    INVALID_TOKEN(-402),
    INTERNAL_SERVER_ERROR(-500),
}
