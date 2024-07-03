package com.example.bookmanage.controller

import com.example.bookmanage.data.request.CreateBookRequest
import com.example.bookmanage.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(private val bookService: BookService) {

    /**
     * 書籍を取得する
     * @param id 書籍ID
     * @return 書籍情報
     */
    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Int): ResponseEntity<Any> {
        return bookService.getBookById(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    /**
     * 書籍一覧を取得する
     * @return 書籍一覧
     */
    @GetMapping
    fun getAllBooks(): ResponseEntity<List<Any>> {
        bookService.getAllBooks().let {
            return ResponseEntity.ok(it)
        }
    }

    /**
     * 著者に紐づく書籍一覧を取得する
     * @param authorId 著者ID
     * @return 書籍一覧
     */
    @GetMapping("/author/{authorId}")
    fun getBooksByAuthorId(@PathVariable authorId: Int): ResponseEntity<List<Any>> {
        bookService.getBooksByAuthorId(authorId).let {
            return ResponseEntity.ok(it)
        }
    }

    /**
     * 書籍を作成する
     * @param request 書籍情報
     * @return 作成した書籍情報
     */
    @PostMapping
    fun createBook(@RequestBody @Valid request: CreateBookRequest): ResponseEntity<Any> {
        return bookService.createBook(request).let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        }
    }

    /**
     * 書籍を更新する
     * @param id 書籍ID
     * @param request 更新情報
     * @return 更新結果
     */
    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Int, @RequestBody @Valid request: CreateBookRequest): ResponseEntity<Any> {
        bookService.updateBook(id, request)
        return ResponseEntity.ok().build()
    }

    /**
     * 書籍を削除する
     * @param id 書籍ID
     * @return 削除結果
     */
    @DeleteMapping("/{id}")
    fun deleteBookById(@PathVariable id: Int): ResponseEntity<Any> {
        bookService.deleteBookById(id)
        return ResponseEntity.noContent().build()
    }
}
