package com.example.bookmanage.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException)
            : ResponseEntity<Map<String, String>> {
        // バリデーションエラーをMapに変換してクライアントに返す
        val errors = e.bindingResult.fieldErrors
            .associate { fieldError: FieldError ->
                fieldError.field to (fieldError.defaultMessage ?: "Unknown error")
            }
        // HTTPステータス400(Bad Request)とともにエラーメッセージを返す
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }
}
