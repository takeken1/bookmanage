package com.example.bookmanage.service

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.Tables.BOOKS
import com.example.bookmanage.data.request.CreateBookRequest
import com.example.bookmanage.data.response.CreateBookResponse
import com.example.bookmanage.data.response.GetBookResponse
import com.example.bookmanage.repository.BookRepository
import org.jooq.Record
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(private val bookRepository: BookRepository) {
    /**
     * 書籍をIDで取得する
     * @param id 書籍ID
     * @return 書籍情報
     */
    @Transactional(readOnly = true)
    fun getBookById(id: Int): GetBookResponse? {
        return bookRepository.findById(id)
    }

    /**
     * 書籍一覧を取得する
     * @return 書籍一覧
     */
    @Transactional(readOnly = true)
    fun getAllBooks(): List<GetBookResponse> {
        return bookRepository.findAll()
    }

    /**
     * 著者に紐づく書籍一覧を取得する
     * @param authorId 著者ID
     * @return 書籍一覧
     */
    @Transactional(readOnly = true)
    fun getBooksByAuthorId(authorId: Int): List<GetBookResponse> {
        return bookRepository.findByAuthorId(authorId)
    }

    /**
     * 書籍を作成する
     * @param book 書籍情報
     * @return 作成した書籍情報
     */
    @Transactional
    fun createBook(book: CreateBookRequest): CreateBookResponse {
        // ISBNが重複している場合は例外をスローする
        if (bookRepository.findByIsbn(book.isbn) != null) {
            throw IllegalArgumentException("The ISBN already exists")
        }
        return bookRepository.save(book)
    }

    /**
     * 書籍を更新する
     * @param id 書籍ID
     * @param book 更新情報
     * @return 更新結果
     */
    @Transactional
    fun updateBook(id: Int, book: CreateBookRequest): Unit {
        // ISBNが重複している場合は例外をスローする
        if (bookRepository.findByIsbn(book.isbn) != null) {
            throw IllegalArgumentException("The ISBN already exists")
        }
        // 更新対象の書籍が存在しない場合は例外をスローする
        if (bookRepository.findByIdLock(id) == null) {
            throw IllegalArgumentException("The book does not exist")
        }
        bookRepository.update(id, book)
    }

    /**
     * 書籍を削除する
     * @param id 書籍ID
     * @return 削除結果
     */
    @Transactional
    fun deleteBookById(id: Int): Unit {
        // 削除対象の書籍が存在しない場合は例外をスローする
        if (bookRepository.findByIdLock(id) == null) {
            throw IllegalArgumentException("The book does not exist")
        }
        bookRepository.deleteById(id)
    }
}
