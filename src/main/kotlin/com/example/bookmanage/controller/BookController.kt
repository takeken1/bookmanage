package com.example.bookmanage.controller

import com.example.bookmanage.data.CreateBookRequest
import com.example.bookmanage.service.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(private val bookService: BookService) {

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Int): ResponseEntity<Any> {
        val bookRecord = bookService.getBookById(id)
        return if (bookRecord.isPresent) {
            ResponseEntity.ok(bookRecord.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllBooks(): ResponseEntity<List<Any>> {
        val books = bookService.getAllBooks()
        return ResponseEntity.ok(books)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody book: CreateBookRequest): ResponseEntity<Any> {
        val newBook = bookService.createBook(book)
        return ResponseEntity.ok(newBook)
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Int, @RequestBody book: CreateBookRequest): ResponseEntity<Any> {
        val updatedRows = bookService.updateBook(id, book)
        return if (updatedRows > 0) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteBookById(@PathVariable id: Int): ResponseEntity<Any> {
        bookService.deleteBookById(id)
        return ResponseEntity.noContent().build()
    }

}