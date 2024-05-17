package com.hrblizz.fileapi.data.repository

import com.hrblizz.fileapi.data.entities.File
import org.springframework.data.mongodb.repository.MongoRepository

interface FileRepository : MongoRepository<File, String> {
    fun findByTokenIn(tokens: List<String>): List<File>

    fun findByToken(token: String): File?

    fun deleteByToken(token: String)
}
