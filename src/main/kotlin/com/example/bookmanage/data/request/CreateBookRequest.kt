package com.example.bookmanage.data.request

import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

/**
 * 書籍作成リクエスト
 * @param title 書籍名
 * @param isbn ISBN
 * @param authorId 著者ID
 */
data class CreateBookRequest(
        @field:Length(max = 255)
        val title: String,

        @field:Length(max = 13)
        @field:Pattern(
                regexp = "^(978|979)\\d{10}$",
                message = "ISBNは、978または979で始まる有効な13桁の数字である必要があります")
        val isbn: String,

        val authorId: Int
)
