package com.example.bookmanage.repository

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.Tables.BOOKS
import com.example.bookmanage.data.response.AuthorResponse
import com.example.bookmanage.tables.Authors
import org.jooq.DSLContext
import org.jooq.Record
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class AuthorRepository(private val dsl: DSLContext) {
    companion object {
        private val log = LoggerFactory.getLogger(AuthorRepository::class.java)
    }

    /**
     * 著者をIDで取得する
     * @param id 著者ID
     * @return 著者情報
     */
    fun findById(id: Int): AuthorResponse? {
        return dsl.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .fetchOne()
            ?.let { AuthorResponse(it.getValue(AUTHORS.ID), it.getValue(AUTHORS.NAME)) }
    }

    /**
     * 著者をIDで取得する(ロック付き)
     * @param id 著者ID
     * @return 著者情報
     */
    fun findByIdLock(id: Int): AuthorResponse? {
        return dsl.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .forUpdate()
            .fetchOne()
            ?.let { AuthorResponse(it.getValue(AUTHORS.ID), it.getValue(AUTHORS.NAME)) }
    }

    /**
     * 著者一覧を取得する
     * @return 著者一覧
     */
    fun findAll(): List<AuthorResponse> {
        return dsl.selectFrom(AUTHORS)
            .fetch()
            .map { AuthorResponse(it.getValue(AUTHORS.ID), it.getValue(AUTHORS.NAME)) }
    }

    /**
     * 著者を作成する
     * @param name 著者名
     * @return 作成した著者情報
     */
    fun save(name: String): AuthorResponse {
        return dsl.insertInto(AUTHORS)
            .set(Authors.AUTHORS.NAME, name)
            .returning()
            .fetchOne()
            ?.let { AuthorResponse(it.getValue(AUTHORS.ID), it.getValue(AUTHORS.NAME)) }
            ?: throw IllegalStateException("Failed to insert the author record")
    }

    /**
     * 著者を更新する
     * @param id 著者ID
     * @param name 著者名
     * @return 更新結果
     */
    fun update(id: Int, name: String): Int {
        return dsl.update(AUTHORS)
            .set(Authors.AUTHORS.NAME, name)
            .where(AUTHORS.ID.eq(id))
            .execute()
    }

    /**
     * 著者をIDで削除する
     * @param id 著者ID
     * @return 削除結果
     */
    fun deleteById(id: Int): Int {
        // 著者に関連する書籍が存在するかチェック
        val bookCount = dsl.selectCount()
            .from(BOOKS)
            .where(BOOKS.AUTHOR_ID.eq(id))
            .fetchOne(0, Int::class.java)

        // 著者に関連する書籍が存在する場合は削除不可
        if ((bookCount ?: 0) > 0) {
            log.error("Author cannot be deleted until book is deleted. authorId: $id")
            throw IllegalStateException("Author cannot be deleted until book is deleted")
        }

        return dsl.deleteFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .execute()
    }
}
