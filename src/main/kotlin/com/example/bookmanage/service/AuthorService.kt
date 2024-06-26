package com.example.bookmanage.service

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.data.AuthorResponse
import com.example.bookmanage.repository.AuthorRepository
import org.jooq.Record
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    @Transactional(readOnly = true)
    fun getAuthorById(id: Int): Optional<AuthorResponse> {
        val authorRecord = authorRepository.findById(id)
        return if (authorRecord.isPresent) {
            Optional.ofNullable(createResponse(authorRecord.get()))
        } else {
            Optional.empty<AuthorResponse>()
        }
    }

    @Transactional(readOnly = true)
    fun getAllAuthors(): List<AuthorResponse> {
        val records = authorRepository.findAll()
        return createResponseList(records)
    }

    @Transactional
    fun createAuthor(name: String): AuthorResponse {
        val newAuthor = authorRepository.save(name)
        return createResponse(newAuthor)
    }

    @Transactional
    fun updateAuthor(id: Int, name: String): Int {
        return authorRepository.update(id, name)
    }

    @Transactional
    fun deleteAuthorById(id: Int) {
        authorRepository.deleteById(id)
    }

    private fun createResponseList(records: List<Record>) : List<AuthorResponse> {
        return records.map { record -> createResponse(record) }
    }

    private fun createResponse(record: Record) : AuthorResponse {
        return AuthorResponse(record.getValue(AUTHORS.ID), record.getValue(AUTHORS.NAME))
    }
}
