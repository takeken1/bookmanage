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
        val record = bookRepository.findById(id)
        return record?.let { bookRecord ->
            createGetResponse(record)
        }
    }

	/**
	 * 書籍一覧を取得する
	 * @return 書籍一覧
	 */
    @Transactional(readOnly = true)
    fun getAllBooks(): List<GetBookResponse> {
        val records = bookRepository.findAll()
        return createGetResponseList(records)
    }

	/**
	 * 書籍を作成する
	 * @param book 書籍情報
	 * @return 作成した書籍情報
	 */
    @Transactional
    fun createBook(book: CreateBookRequest): CreateBookResponse {
        val newBook = bookRepository.save(book)
        return createResponse(newBook)
    }

	/**
	 * 書籍を更新する
	 * @param id 書籍ID
	 * @param book 更新情報
	 * @return 更新結果
	 */
    @Transactional
    fun updateBook(id: Int, book: CreateBookRequest): Int {
        return bookRepository.update(id, book)
    }

	/**
	 * 書籍を削除する
	 * @param id 書籍ID
	 */
    @Transactional
    fun deleteBookById(id: Int) {
        bookRepository.deleteById(id)
    }

    private fun createGetResponseList(records: List<Record>) : List<GetBookResponse> {
        val books = records.map { record -> createGetResponse(record) }
        return books
    }

    private fun createGetResponse(record: Record) : GetBookResponse {
        return GetBookResponse(record.getValue(BOOKS.ID),
            record.getValue(BOOKS.TITLE),
            record.getValue(BOOKS.ISBN),
            record.getValue(BOOKS.AUTHOR_ID),
            record.getValue(AUTHORS.NAME))
    }

    private fun createResponse(record: Record) : CreateBookResponse {
        return CreateBookResponse(record.getValue(BOOKS.ID),
            record.getValue(BOOKS.TITLE),
            record.getValue(BOOKS.ISBN),
            record.getValue(BOOKS.AUTHOR_ID))
    }
}
