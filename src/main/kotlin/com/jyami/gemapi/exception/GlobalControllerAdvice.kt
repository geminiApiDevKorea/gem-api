package com.jyami.gemapi.exception

import com.jyami.gemapi.endpoint.ErrorResponse
import com.jyami.gemapi.endpoint.StatusCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalControllerAdvice {

//    @ExceptionHandler(Exception::class)
//    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
//        val errorDetails = ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, ex.message, request.getDescription(false))
//        return ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
//    }

    @Override
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationCustomExceptions(ex: MethodArgumentNotValidException, response: WebRequest): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        val errorResponse = ErrorResponse(StatusCode.BAD_REQUEST, "Validation Error", errors.toString())
        return ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST)
    }

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
