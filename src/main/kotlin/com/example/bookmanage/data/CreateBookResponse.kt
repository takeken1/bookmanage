package com.example.bookmanage.data

data class CreateBookResponse(
    val id: Int,
    val title: String,
    val isbn: String,
    val authorId: Int
)
