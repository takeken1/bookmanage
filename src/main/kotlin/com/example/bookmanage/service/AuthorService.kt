package com.example.bookmanage.service

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.data.AuthorResponse
import com.example.bookmanage.repository.AuthorRepository
import org.jooq.Record
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun getAuthorById(id: Int): Optional<AuthorResponse> {
        val authorRecord = authorRepository.findById(id)
        return if (authorRecord.isPresent) {
            Optional.ofNullable(createResponse(authorRecord.get()))
        } else {
            Optional.empty<AuthorResponse>()
        }
    }

    fun getAllAuthors(): List<AuthorResponse> {
        val records = authorRepository.findAll()
        return createResponseList(records)
    }

    fun createAuthor(name: String?): AuthorResponse {
        val newAuthor = authorRepository.save(name)
        return createResponse(newAuthor)
    }

    fun updateAuthor(id: Int, name : String?): Int {
        return authorRepository.update(id, name)
    }

    @DeleteMapping("/{id}")
    fun deleteAuthorById(@PathVariable id: Int) {
        authorRepository.deleteById(id)
    }

    private fun createResponseList(records: List<Record>) : List<AuthorResponse> {
        val authors = records.map { record ->
            AuthorResponse(record.getValue(AUTHORS.ID), record.getValue(AUTHORS.NAME))
        }
        return authors;
    }

    private fun createResponse(record: Record) : AuthorResponse {
        return AuthorResponse(record.getValue(AUTHORS.ID), record.getValue(AUTHORS.NAME))
    }
}
