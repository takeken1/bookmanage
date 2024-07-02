package com.example.bookmanage.service

import com.example.bookmanage.data.request.CreateBookRequest
import com.example.bookmanage.data.response.CreateBookResponse
import com.example.bookmanage.data.response.GetBookResponse
import com.example.bookmanage.repository.BookRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class BookServiceTest: StringSpec({
    lateinit var bookService: BookService
    lateinit var bookRepository: BookRepository

    beforeTest {
        bookRepository = mockk()
        bookService = BookService(bookRepository)
    }

    "getBookById should return book when valid ID is provided" {
        val record = GetBookResponse(1, "吾輩は猫である", "9784101010137", 1, "夏目漱石")

        every { bookRepository.findById(any()) } returns record

        val book = bookService.getBookById(1)

        book shouldBe GetBookResponse(1, "吾輩は猫である", "9784101010137", 1, "夏目漱石")
    }

    "getBookById should return null when invalid ID is provided" {
        every { bookRepository.findById(any()) } returns null

        val book = bookService.getBookById(1)

        book shouldBe null
    }

    "getAllBooks should return all books" {
        val records = listOf(
            GetBookResponse(1, "吾輩は猫である", "9784101010014", 1, "夏目漱石"),
            GetBookResponse(2, "こころ", "9784101010137", 2, "夏目漱石")
        )

        every { bookRepository.findAll() } returns records

        val books = bookService.getAllBooks()

        books shouldBe records
    }

    "getBooksByAuthorId should return books by author" {
        val records = listOf(
            GetBookResponse(1, "吾輩は猫である", "9784101010014", 1, "夏目漱石"),
            GetBookResponse(2, "こころ", "9784101010137", 1, "夏目漱石")
        )

        every { bookRepository.findByAuthorId(any()) } returns records

        val books = bookService.getBooksByAuthorId(1)

        books shouldBe records
    }

    "createBook should create a new book" {
        every { bookRepository.findByIsbn(any()) } returns null
        val record = CreateBookResponse(1, "吾輩は猫である", "9784101010137", 1)
        every { bookRepository.save(any()) } returns record

        val book = bookService.createBook(CreateBookRequest("吾輩は猫である", "9784101010137", 1))

        book shouldBe CreateBookResponse(1, "吾輩は猫である", "9784101010137", 1)
    }

    "createBook should throw an exception when the ISBN already exists" {
        every { bookRepository.findByIsbn(any()) } returns GetBookResponse(1, "吾輩は猫である", "9784101010137", 1, "夏目漱石")

        try {
            bookService.createBook(CreateBookRequest("こころ", "9784101010137", 2))
        } catch (e: IllegalArgumentException) {
            e.message shouldBe "The ISBN already exists"
        }
    }

    "updateBook should update the book" {
        every { bookRepository.findByIsbn(any()) } returns null
        every { bookRepository.findByIdLock(any()) } returns GetBookResponse(1, "吾輩は猫である", "9784101010137", 1, "夏目漱石")
        every { bookRepository.update(any(), any()) } returns 1

        bookService.updateBook(1, CreateBookRequest("こころ", "9784101010137", 2)) shouldBe Unit
    }

    "updateBook should throw an exception when the ISBN already exists" {
        every { bookRepository.findByIsbn(any()) } returns GetBookResponse(2, "こころ", "9784101010137", 2, "夏目漱石")

        try {
            bookService.updateBook(1, CreateBookRequest("こころ", "9784101010137", 2))
        } catch (e: IllegalArgumentException) {
            e.message shouldBe "The ISBN already exists"
        }
    }

    "updateBook should throw an exception when the book does not exist" {
        every { bookRepository.findByIsbn(any()) } returns null
        every { bookRepository.findByIdLock(any()) } returns null

        try {
            bookService.updateBook(1, CreateBookRequest("こころ", "9784101010137", 2))
        } catch (e: IllegalArgumentException) {
            e.message shouldBe "The book does not exist"
        }
    }

    "deleteBookById should delete the book" {
        every { bookRepository.findByIdLock(any()) } returns GetBookResponse(1, "吾輩は猫である", "9784101010137", 1, "夏目漱石")
        every { bookRepository.deleteById(any()) } returns 1

        bookService.deleteBookById(1) shouldBe Unit
    }

    "deleteBookById should throw an exception when the book does not exist" {
        every { bookRepository.findByIdLock(any()) } returns null

        try {
            bookService.deleteBookById(1)
        } catch (e: IllegalArgumentException) {
            e.message shouldBe "The book does not exist"
        }
    }
})
