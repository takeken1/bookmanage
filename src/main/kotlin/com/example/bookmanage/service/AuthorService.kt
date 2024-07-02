package com.example.bookmanage.service

import com.example.bookmanage.Tables.AUTHORS
import com.example.bookmanage.data.response.AuthorResponse
import com.example.bookmanage.repository.AuthorRepository
import org.jooq.Record
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    /**
     * 著者をIDで取得する
     * @param id 著者ID
     * @return 著者情報
     */
    @Transactional(readOnly = true)
    fun getAuthorById(id: Int): AuthorResponse? {
        return authorRepository.findById(id)?.let { authorRecord ->
            createResponse(authorRecord)
        }
    }

    /**
     * 著者一覧を取得する
     * @return 著者一覧
     */
    @Transactional(readOnly = true)
    fun getAllAuthors(): List<AuthorResponse> {
        val records = authorRepository.findAll()
        return createResponseList(records)
    }

    /**
     * 著者を作成する
     * @param name 著者名
     * @return 作成した著者情報
     */
    @Transactional
    fun createAuthor(name: String): AuthorResponse {
        val newAuthor = authorRepository.save(name)
        return createResponse(newAuthor)
    }

    /**
     * 著者を更新する
     * @param id 著者ID
     * @param name 著者名
     * @return 更新結果
     */
    @Transactional
    fun updateAuthor(id: Int, name: String): Unit {
        // 更新対象の著者が存在しない場合は例外をスローする
        if (authorRepository.findByIdLock(id) == null) {
            throw IllegalArgumentException("The author does not exist")
        }
        authorRepository.update(id, name)
    }

    /**
     * 著者を削除する
     * @param id 著者ID
     * @return 削除結果
     */
    @Transactional
    fun deleteAuthorById(id: Int): Unit {
        // 削除対象の著者が存在しない場合は例外をスローする
        if (authorRepository.findByIdLock(id) == null) {
            throw IllegalArgumentException("The author does not exist")
        }
        authorRepository.deleteById(id)
    }

    private fun createResponseList(records: List<Record>): List<AuthorResponse> {
        return records.map { record -> createResponse(record) }
    }

    private fun createResponse(record: Record): AuthorResponse {
        return AuthorResponse(record.getValue(AUTHORS.ID), record.getValue(AUTHORS.NAME))
    }
}
