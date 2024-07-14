package com.jyami.gemapi.exception

class ServerException(
    cause: Throwable?,
    message: String?
) : RuntimeException(message, cause) {

    constructor(errorMessage: String) : this(null as Throwable?, errorMessage)
    constructor(cause: Throwable) : this(cause, cause.message)
    constructor(errorMessage: String, cause: Throwable) : this(cause, errorMessage)
    constructor() : this(null as Throwable?, null as String?)
}
