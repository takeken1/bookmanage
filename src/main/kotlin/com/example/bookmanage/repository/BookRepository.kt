package com.example.bookmanage.repository

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.Tables.BOOKS
import com.example.bookmanage.data.CreateBookRequest
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class BookRepository(private val dsl: DSLContext) {
    fun findById(id: Int): Optional<Record> {
        return Optional.ofNullable(
            dsl.select().from(BOOKS)
                .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
                .where(BOOKS.ID.eq(id))
                .fetchOne()
        )
    }

    fun findAll(): List<Record> {
        return dsl.select().from(BOOKS)
            .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
            .fetch()
    }

    fun save(book: CreateBookRequest): Record {
        return dsl.insertInto(BOOKS)
            .set(BOOKS.TITLE, book.title)
			.set(BOOKS.ISBN, book.isbn)
            .set(BOOKS.AUTHOR_ID, book.authorId)
            .returning()
            .fetchOne() ?: throw IllegalStateException("Failed to insert the book record")
    }

    fun update(id: Int, book: CreateBookRequest): Int {
        return dsl.update(BOOKS)
            .set(BOOKS.TITLE, book.title)
            .set(BOOKS.ISBN, book.isbn)
            .set(BOOKS.AUTHOR_ID, book.authorId)
            .where(BOOKS.ID.eq(id))
            .execute()
    }

    fun deleteById(id: Int) {
        dsl.deleteFrom(BOOKS)
            .where(BOOKS.ID.eq(id))
            .execute()
    }
}
