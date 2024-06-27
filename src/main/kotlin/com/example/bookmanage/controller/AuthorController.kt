package com.example.bookmanage.controller

import com.example.bookmanage.data.request.CreateAuthorRequest
import com.example.bookmanage.service.AuthorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/authors")
class AuthorController(private val authorService: AuthorService) {

	/**
	 * 著者を取得する
	 * @param id 著者ID
	 * @return 著者情報
	 */
    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable id: Int): ResponseEntity<Any> {
        val authorRecord = authorService.getAuthorById(id)
        return if (authorRecord.isPresent) {
            ResponseEntity.ok(authorRecord.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

	/**
	 * 著者一覧を取得する
	 * @return 著者一覧
	 */
    @GetMapping
    fun getAllAuthors(): ResponseEntity<List<Any>> {
        val authors = authorService.getAllAuthors()
        return ResponseEntity.ok(authors)
    }

	/**
	 * 著者を作成する
	 * @param request 著者情報
	 * @return 作成した著者情報
	 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(@RequestBody @Valid request: CreateAuthorRequest): ResponseEntity<Any> {
        val newAuthor = authorService.createAuthor(request.name)
        return ResponseEntity.ok(newAuthor)
    }

	/**
	 * 著者を更新する
	 * @param id 著者ID
	 * @param request 更新情報
	 * @return 更新結果
	 */
    @PutMapping("/{id}")
    fun updateAuthor(@PathVariable id: Int, @RequestBody @Valid request: CreateAuthorRequest): ResponseEntity<Any> {
        val updatedRows = authorService.updateAuthor(id, request.name)
        return if (updatedRows > 0) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

	/**
	 * 著者を削除する
	 * @param id 著者ID
	 * @return 削除結果
	 */
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
