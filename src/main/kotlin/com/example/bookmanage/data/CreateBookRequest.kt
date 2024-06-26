package com.example.bookmanage.data

public final data class  CreateBookRequest(
        val title: String,
        val isbn: String,
        val authorId: Int
)