package com.jyami.gemapi.endpoint


open class ResponseDto(
    open val code: StatusCode
)

data class EmptyResponse(override val code: StatusCode) : ResponseDto(code = code)
