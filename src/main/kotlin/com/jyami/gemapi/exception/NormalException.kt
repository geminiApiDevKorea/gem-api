package com.jyami.gemapi.exception

import com.jyami.gemapi.endpoint.StatusCode

class NormalException(
    val statusCode: StatusCode,
    cause: Throwable?,
    message: String?
) : RuntimeException(message, cause) {

    constructor(statusCode: StatusCode, errorMessage: String) : this(statusCode, null as Throwable?, errorMessage)
    constructor(statusCode: StatusCode, cause: Throwable) : this(statusCode, cause, cause.message)
    constructor(statusCode: StatusCode, errorMessage: String, cause: Throwable) : this(statusCode, cause, errorMessage)
    constructor(statusCode: StatusCode) : this(statusCode, null as Throwable?, null as String?)
}

