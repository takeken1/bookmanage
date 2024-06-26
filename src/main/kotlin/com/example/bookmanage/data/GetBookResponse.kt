package com.example.bookmanage.data

data class GetBookResponse(
    val id: Int,
    val title: String,
    val isbn: String,
    val authorId: Int,
    val authorName: String
)
