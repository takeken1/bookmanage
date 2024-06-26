package com.example.bookmanage.service

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.Tables.BOOKS
import com.example.bookmanage.data.CreateBookRequest
import com.example.bookmanage.data.CreateBookResponse
import com.example.bookmanage.data.GetBookResponse
import com.example.bookmanage.repository.BookRepository
import org.jooq.Record
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository) {

    @Transactional(readOnly = true)
    fun getBookById(id: Int): Optional<GetBookResponse> {
        val record = bookRepository.findById(id)
        return if (record.isPresent) {
            Optional.ofNullable(createGetResponse(record.get()))
        } else {
            Optional.empty<GetBookResponse>()
        }
    }

    @Transactional(readOnly = true)
    fun getAllBooks(): List<GetBookResponse> {
        val records = bookRepository.findAll()
        return createGetResponseList(records)
    }

    @Transactional
    fun createBook(book: CreateBookRequest): CreateBookResponse {
        val newBook = bookRepository.save(book)
        return createResponse(newBook)
    }

    @Transactional
    fun updateBook(id: Int, book: CreateBookRequest): Int {
        return bookRepository.update(id, book)
    }

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
