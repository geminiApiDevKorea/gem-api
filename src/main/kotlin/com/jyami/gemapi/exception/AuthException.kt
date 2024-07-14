package com.jyami.gemapi.exception

import com.jyami.gemapi.endpoint.StatusCode

class AuthException(
    val statusCode: StatusCode,
    cause: Throwable?,
    message: String?,
) : RuntimeException(message, cause){
    constructor(statusCode: StatusCode, message: String?) : this(statusCode, null, message)
    constructor(statusCode: StatusCode) : this(statusCode, null, null)
}
