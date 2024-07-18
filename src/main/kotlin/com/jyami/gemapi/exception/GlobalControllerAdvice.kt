package com.jyami.gemapi.exception

import com.jyami.gemapi.endpoint.ErrorResponse
import com.jyami.gemapi.endpoint.StatusCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalControllerAdvice : ResponseEntityExceptionHandler() {

//    @ExceptionHandler(Exception::class)
//    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
//        val errorDetails = ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, ex.message, request.getDescription(false))
//        return ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
//    }

    @ExceptionHandler(AuthException::class)
    fun handlerAuthException(ex: AuthException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorDetails = ErrorResponse(ex.statusCode, ex.message, request.getDescription(false))
        return ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(NormalException::class)
    fun handlerNormalException(ex: NormalException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorDetails = ErrorResponse(ex.statusCode, ex.message, request.getDescription(false))
        return ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.OK)
    }

    @ExceptionHandler(ServerException::class)
    fun handlerServerException(ex: ServerException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorDetails = ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, ex.message, request.getDescription(false))
        return ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handlerIllegalArgumentException(ex: ServerException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorDetails = ErrorResponse(StatusCode.BAD_REQUEST, ex.message, request.getDescription(false))
        return ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.BAD_REQUEST)
    }

}
