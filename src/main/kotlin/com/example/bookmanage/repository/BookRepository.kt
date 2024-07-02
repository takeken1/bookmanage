package com.example.bookmanage.repository

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.Tables.BOOKS
import com.example.bookmanage.data.request.CreateBookRequest
import com.example.bookmanage.data.response.CreateBookResponse
import com.example.bookmanage.data.response.GetBookResponse
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
    fun findById(id: Int): GetBookResponse? {
        return dsl.select().from(BOOKS)
            .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
            .where(BOOKS.ID.eq(id))
            .fetchOne()
            ?.let {
                GetBookResponse(
                    it.getValue(BOOKS.ID),
                    it.getValue(BOOKS.TITLE),
                    it.getValue(BOOKS.ISBN),
                    it.getValue(AUTHORS.ID),
                    it.getValue(AUTHORS.NAME)
                )
            }
    }

    /**
     * 書籍をIDで取得する(ロック付き)
     * @param id 書籍ID
     * @return 書籍情報
     */
    fun findByIdLock(id: Int): GetBookResponse? {
        return dsl.select().from(BOOKS)
            .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
            .where(BOOKS.ID.eq(id))
            .forUpdate()
            .fetchOne()
            ?.let {
                GetBookResponse(
                    it.getValue(BOOKS.ID),
                    it.getValue(BOOKS.TITLE),
                    it.getValue(BOOKS.ISBN),
                    it.getValue(AUTHORS.ID),
                    it.getValue(AUTHORS.NAME)
                )
            }
    }

    /**
     * 書籍をISBNで取得する
     * @param isbn ISBN
     * @return 書籍情報
     */
    fun findByIsbn(isbn: String): GetBookResponse? {
        return dsl.select().from(BOOKS)
            .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
            .where(BOOKS.ISBN.eq(isbn))
            .fetchOne()
            ?.let {
                GetBookResponse(
                    it.getValue(BOOKS.ID),
                    it.getValue(BOOKS.TITLE),
                    it.getValue(BOOKS.ISBN),
                    it.getValue(AUTHORS.ID),
                    it.getValue(AUTHORS.NAME)
                )
            }
    }

    /**
     * 書籍一覧を取得する
     * @return 書籍一覧
     */
    fun findAll(): List<GetBookResponse> {
        return dsl.select().from(BOOKS)
            .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
            .fetch()
            .map {
                GetBookResponse(
                    it.getValue(BOOKS.ID),
                    it.getValue(BOOKS.TITLE),
                    it.getValue(BOOKS.ISBN),
                    it.getValue(AUTHORS.ID),
                    it.getValue(AUTHORS.NAME)
                )
            }
    }

    /**
     * 著者に紐づく書籍一覧を取得する
     * @param authorId 著者ID
     * @return 書籍一覧
     */
    fun findByAuthorId(authorId: Int): List<GetBookResponse> {
        return dsl.select().from(BOOKS)
            .join(AUTHORS).on(AUTHORS.ID.eq(BOOKS.AUTHOR_ID))
            .where(BOOKS.AUTHOR_ID.eq(authorId))
            .fetch()
            .map {
                GetBookResponse(
                    it.getValue(BOOKS.ID),
                    it.getValue(BOOKS.TITLE),
                    it.getValue(BOOKS.ISBN),
                    it.getValue(AUTHORS.ID),
                    it.getValue(AUTHORS.NAME)
                )
            }
    }

    /**
     * 書籍を作成する
     * @param book 書籍情報
     * @return 作成した書籍情報
     */
    fun save(book: CreateBookRequest): CreateBookResponse {
        return dsl.insertInto(BOOKS)
            .set(BOOKS.TITLE, book.title)
            .set(BOOKS.ISBN, book.isbn)
            .set(BOOKS.AUTHOR_ID, book.authorId)
            .returning()
            .fetchOne()
            ?.let {
                CreateBookResponse(
                    it.getValue(BOOKS.ID),
                    it.getValue(BOOKS.TITLE),
                    it.getValue(BOOKS.ISBN),
                    it.getValue(BOOKS.AUTHOR_ID)
                )
            } ?: throw IllegalStateException("Failed to insert the book record")
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
     * @return 削除結果
     */
    fun deleteById(id: Int): Int {
        return dsl.deleteFrom(BOOKS)
            .where(BOOKS.ID.eq(id))
            .execute()
    }
}
