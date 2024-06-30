package com.example.bookmanage.repository

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.Tables.BOOKS
import com.example.bookmanage.data.request.CreateBookRequest
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class BookRepository(private val dsl: DSLContext) {
	/**
	 * 書籍をIDで取得する
	 * @param id 書籍ID
	 * @return 書籍情報
	 */	 
    fun findById(id: Int): Record? {
        return dsl.select().from(BOOKS)
                .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
                .where(BOOKS.ID.eq(id))
                .fetchOne()
    }

	/**
	 * 書籍一覧を取得する
	 * @return 書籍一覧
	 */
    fun findAll(): List<Record> {
        return dsl.select().from(BOOKS)
            .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
            .fetch()
    }

	/**
	 * 書籍を作成する
	 * @param book 書籍情報
	 * @return 作成した書籍情報
	 */
    fun save(book: CreateBookRequest): Record {
        return dsl.insertInto(BOOKS)
            .set(BOOKS.TITLE, book.title)
			.set(BOOKS.ISBN, book.isbn)
            .set(BOOKS.AUTHOR_ID, book.authorId)
            .returning()
            .fetchOne() ?: throw IllegalStateException("Failed to insert the book record")
    }

	/**
	 * 書籍を更新する
	 * @param id 書籍ID
	 * @param book 更新情報
	 * @return 更新結果
	 */
    fun update(id: Int, book: CreateBookRequest): Int {
        return dsl.update(BOOKS)
            .set(BOOKS.TITLE, book.title)
            .set(BOOKS.ISBN, book.isbn)
            .set(BOOKS.AUTHOR_ID, book.authorId)
            .where(BOOKS.ID.eq(id))
            .execute()
    }

	/**
	 * 書籍をIDで削除する
	 * @param id 書籍ID
	 */
    fun deleteById(id: Int) {
        dsl.deleteFrom(BOOKS)
            .where(BOOKS.ID.eq(id))
            .execute()
    }
}
