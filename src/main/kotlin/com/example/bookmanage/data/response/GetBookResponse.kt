package com.example.bookmanage.data.response

/**
 * 書籍情報レスポンス
 * @param id 書籍ID
 * @param title 書籍名
 * @param isbn ISBN
 * @param authorId 著者ID
 * @param authorName 著者名
 */
data class GetBookResponse(
    val id: Int,
    val title: String,
    val isbn: String,
    val authorId: Int,
    val authorName: String
)
