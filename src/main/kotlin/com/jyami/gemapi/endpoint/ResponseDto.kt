package com.jyami.gemapi.endpoint


open class ResponseDto(
    open val code: StatusCode = StatusCode.OK
)

data class EmptyResponse(override val code: StatusCode) : ResponseDto(code = code)

data class ErrorResponse(
    override val code: StatusCode,
    val message: String?,
    val description: String?
): ResponseDto(code = code)
