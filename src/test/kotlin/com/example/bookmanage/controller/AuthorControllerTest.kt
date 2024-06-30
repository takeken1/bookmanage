package com.example.bookmanage.controller

import com.example.bookmanage.data.request.CreateAuthorRequest
import com.example.bookmanage.data.response.AuthorResponse
import com.example.bookmanage.service.AuthorService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

class AuthorControllerTest: StringSpec({
    lateinit var authorController: AuthorController
    lateinit var authorService: AuthorService

    beforeTest {
        authorService = mockk()
        authorController = AuthorController(authorService)
    }

    "getAuthorById should return author when valid ID is provided" {
        val body = AuthorResponse(1,"芥川龍之介")
        every { authorService.getAuthorById(any()) } returns body

        val response = authorController.getAuthorById(1)

		response.statusCode shouldBe HttpStatus.OK
		response.body shouldBe body
    }

	"getAuthorById should return 404 when invalid ID is provided" {
		every { authorService.getAuthorById(any()) } returns null

		val response = authorController.getAuthorById(1)

		response.statusCode shouldBe HttpStatus.NOT_FOUND
	}

	"getAllAuthors should return all authors" {
		val body = listOf(
			AuthorResponse(1, "芥川龍之介"),
			AuthorResponse(2, "太宰治")
		)
		every { authorService.getAllAuthors() } returns body

		val response = authorController.getAllAuthors()

		response.statusCode shouldBe HttpStatus.OK
		response.body shouldBe body
	}

	"createAuthor should create a new author" {
		val request = CreateAuthorRequest("芥川龍之介")
		val body = AuthorResponse(1, "芥川龍之介")
		every { authorService.createAuthor(any()) } returns body

		val response = authorController.createAuthor(request)

		response.statusCode shouldBe HttpStatus.CREATED
		response.body shouldBe body
	}

	"updateAuthor should update an existing author" {
		val request = CreateAuthorRequest("芥川龍之介")
		every { authorService.updateAuthor(any(), any()) } returns 1

		val response = authorController.updateAuthor(1, request)

		response.statusCode shouldBe HttpStatus.OK
	}

	"updateAuthor should return 404 when updating a non-existing author" {
		every { authorService.updateAuthor(any(), any()) } returns 0

		val response = authorController.updateAuthor(1, CreateAuthorRequest("芥川龍之介"))

		response.statusCode shouldBe HttpStatus.NOT_FOUND
	}

	"deleteAuthorById should delete an existing author" {
		every { authorService.deleteAuthorById(any()) } returns 1

		val result = authorController.deleteAuthorById(1)

		result.statusCode shouldBe HttpStatus.NO_CONTENT
	}

	"deleteAuthorById should return 400 when deleting a non-existing author" {
		every { authorService.deleteAuthorById(any()) } throws Exception()

		val result = authorController.deleteAuthorById(1)

		result.statusCode shouldBe HttpStatus.BAD_REQUEST
	}
})
