package com.example.bookmanage.data.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * 著者作成リクエスト
 * @param name 著者名
 */
data class CreateAuthorRequest(
    @field:Length(max = 100)
    @field:NotNull
    @field:NotBlank
    val name: String
)
