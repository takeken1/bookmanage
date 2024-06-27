package com.example.bookmanage.data.response

/**
 * 書籍作成レスポンス
 * @param id 書籍ID
 * @param title 書籍名
 * @param isbn ISBN
 * @param authorId 著者ID
 */
data class CreateBookResponse(
    val id: Int,
    val title: String,
    val isbn: String,
    val authorId: Int
)
