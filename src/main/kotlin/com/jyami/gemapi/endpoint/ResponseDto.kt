package com.jyami.gemapi.endpoint


open class ResponseDto(
    open val code: StatusCode = StatusCode.OK
)

data class EmptyResponse(override val code: StatusCode) : ResponseDto(code = code)
