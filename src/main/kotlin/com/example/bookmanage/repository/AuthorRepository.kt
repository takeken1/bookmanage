package com.example.bookmanage.repository

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.Tables.BOOKS
import com.example.bookmanage.tables.Authors
import org.jooq.DSLContext
import org.jooq.Record
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AuthorRepository(private val dsl: DSLContext) {
    companion object {
        private val log = LoggerFactory.getLogger(AuthorRepository::class.java)
    }

    fun findById(id: Int): Optional<Record> {
        return Optional.ofNullable(
            dsl.selectFrom(AUTHORS)
                .where(AUTHORS.ID.eq(id))
                .fetchOne()
        )
    }

    fun findAll(): List<Record> {
        return dsl.selectFrom(AUTHORS)
            .fetch()
    }

    fun save(name: String): Record {
        return dsl.insertInto(AUTHORS)
            .set(Authors.AUTHORS.NAME, name)
            .returning()
            .fetchOne() ?: throw IllegalStateException("Failed to insert the book record")
    }

    fun update(id: Int, name: String): Int {
        return dsl.update(AUTHORS)
            .set(Authors.AUTHORS.NAME, name)
            .where(AUTHORS.ID.eq(id))
            .execute()
    }

    fun deleteById(id: Int) {
        // 著者に関連する書籍が存在するかチェック
        val bookCount = dsl.selectCount()
            .from(BOOKS)
            .where(BOOKS.AUTHOR_ID.eq(id))
            .fetchOne(0, Int::class.java)

        if ((bookCount ?: 0) > 0) {
            log.error("Author cannot be deleted until book is deleted. authorId: $id")
            throw IllegalStateException("Author cannot be deleted until book is deleted")
        }

        dsl.deleteFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .execute()
    }
}
