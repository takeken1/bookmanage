package com.example.bookmanage.controller

import com.example.bookmanage.data.request.CreateBookRequest
import com.example.bookmanage.data.response.CreateBookResponse
import com.example.bookmanage.data.response.GetBookResponse
import com.example.bookmanage.service.BookService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

class BookControllerTest : StringSpec({
    lateinit var bookController: BookController
    lateinit var bookService: BookService

    beforeTest {
        bookService = mockk()
        bookController = BookController(bookService)
    }

    "getBookById should return book when valid ID is provided" {
        val body = GetBookResponse(1, "吾輩は猫である", "9784101010137", 1, "夏目漱石")
        every { bookService.getBookById(any()) } returns body

        val response = bookController.getBookById(1)

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe body
    }

    "getBookById should return 404 when invalid ID is provided" {
        every { bookService.getBookById(any()) } returns null

        val response = bookController.getBookById(1)

        response.statusCode shouldBe HttpStatus.NOT_FOUND
    }

    "getAllBooks should return all books" {
        val body = listOf(
            GetBookResponse(1, "吾輩は猫である", "9784101010014", 1, "夏目漱石"),
            GetBookResponse(2, "こころ", "9784101010137", 2, "夏目漱石")
        )
        every { bookService.getAllBooks() } returns body

        val response = bookController.getAllBooks()

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe body
    }

    "getBooksByAuthorId should return books by author" {
        val body = listOf(
            GetBookResponse(1, "吾輩は猫である", "9784101010014", 1, "夏目漱石"),
            GetBookResponse(2, "こころ", "9784101010137", 1, "夏目漱石")
        )
        every { bookService.getBooksByAuthorId(any()) } returns body

        val response = bookController.getBooksByAuthorId(1)

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe body
    }

    "createBook should create a new book" {
        val request = CreateBookRequest("こころ", "9784101010137", 2)
        val body = CreateBookResponse(1, "こころ", "9784101010137", 2)
        every { bookService.createBook(any()) } returns body

        val response = bookController.createBook(request)

        response.statusCode shouldBe HttpStatus.CREATED
        response.body shouldBe body
    }

    "updateBook should update a book" {
        val request = CreateBookRequest("こころ", "9784101010137", 2)
        every { bookService.updateBook(any(), any()) } returns Unit

        val response = bookController.updateBook(1, request)

        response.statusCode shouldBe HttpStatus.OK
    }

    "deleteBookById should delete a book" {
        every { bookService.deleteBookById(any()) } returns Unit

        val response = bookController.deleteBookById(1)

        response.statusCode shouldBe HttpStatus.NO_CONTENT
    }
})
