package com.example.bookmanage.service

import com.example.bookmanage.data.response.AuthorResponse
import com.example.bookmanage.repository.AuthorRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AuthorServiceTest : StringSpec({
    lateinit var authorService: AuthorService
    lateinit var authorRepository: AuthorRepository

    beforeTest {
        authorRepository = mockk()
        authorService = AuthorService(authorRepository)
    }

    "getAuthorById should return author when valid ID is provided" {
        val record = AuthorResponse(1, "芥川龍之介")

        every { authorRepository.findById(any()) } returns record

        val author = authorService.getAuthorById(1)

        author shouldBe AuthorResponse(1, "芥川龍之介")
    }

    "getAuthorById should return null when invalid ID is provided" {
        every { authorRepository.findById(any()) } returns null

        val author = authorService.getAuthorById(1)

        author shouldBe null
    }

    "getAllAuthors should return all authors" {
        val records = listOf(
            AuthorResponse(1, "芥川龍之介"),
            AuthorResponse(2, "太宰治")
        )

        every { authorRepository.findAll() } returns records

        val authors = authorService.getAllAuthors()

        authors shouldBe records
    }

    "createAuthor should create a new author" {
        val record = AuthorResponse(1, "芥川龍之介")

        every { authorRepository.save(any()) } returns record

        val author = authorService.createAuthor("芥川龍之介")

        author shouldBe AuthorResponse(1, "芥川龍之介")
    }

    "updateAuthor should update the author" {
        every { authorRepository.findByIdLock(any()) } returns AuthorResponse(1, "芥川龍之介")
        every { authorRepository.update(any(), any()) } returns 1

        authorService.updateAuthor(1, "太宰治") shouldBe Unit
    }

    "updateAuthor should throw an exception when the author does not exist" {
        every { authorRepository.findByIdLock(any()) } returns null

        try {
            authorService.updateAuthor(1, "太宰治")
        } catch (e: IllegalArgumentException) {
            e.message shouldBe "The author does not exist"
        }
    }

    "deleteAuthorById should delete the author" {
        every { authorRepository.findByIdLock(any()) } returns AuthorResponse(1, "芥川龍之介")
        every { authorRepository.deleteById(any()) } returns 1

        authorService.deleteAuthorById(1) shouldBe Unit
    }

    "deleteAuthorById should throw an exception when the author does not exist" {
        every { authorRepository.findByIdLock(any()) } returns null

        try {
            authorService.deleteAuthorById(1)
        } catch (e: IllegalArgumentException) {
            e.message shouldBe "The author does not exist"
        }
    }
})
