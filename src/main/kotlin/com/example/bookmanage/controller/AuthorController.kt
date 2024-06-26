package com.example.bookmanage.controller

import com.example.bookmanage.data.CreateAuthorRequest
import com.example.bookmanage.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/authors")
class AuthorController(private val authorService: AuthorService) {

    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable id: Int): ResponseEntity<Any> {
        val authorRecord = authorService.getAuthorById(id)
        return if (authorRecord.isPresent) {
            ResponseEntity.ok(authorRecord.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllAuthors(): ResponseEntity<List<Any>> {
        val authors = authorService.getAllAuthors()
        return ResponseEntity.ok(authors)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(@RequestBody request: CreateAuthorRequest): ResponseEntity<Any> {
        val newAuthor = authorService.createAuthor(request.name)
        return ResponseEntity.ok(newAuthor)
    }

    @PutMapping("/{id}")
    fun updateAuthor(@PathVariable id: Int, @RequestBody request: CreateAuthorRequest): ResponseEntity<Any> {
        val updatedRows = authorService.updateAuthor(id, request.name)
        return if (updatedRows > 0) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteAuthorById(@PathVariable id: Int): ResponseEntity<Any> {
        try{
            authorService.deleteAuthorById(id)
            return ResponseEntity.noContent().build()
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }
}
