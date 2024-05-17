package com.hrblizz.fileapi.service

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hrblizz.fileapi.controller.exception.BadRequestException
import com.hrblizz.fileapi.controller.exception.NotFoundException
import com.hrblizz.fileapi.controller.exception.ServiceUnavailableException
import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.data.repository.FileRepository
import com.hrblizz.fileapi.library.log.LogItem
import com.hrblizz.fileapi.library.log.Logger
import com.hrblizz.fileapi.rest.request.FileMetasRequest
import com.hrblizz.fileapi.rest.request.FileUploadRequest
import com.hrblizz.fileapi.rest.response.FileMetaResponse
import com.hrblizz.fileapi.rest.response.FileMetasResponse
import com.hrblizz.fileapi.rest.response.FileResponse
import com.hrblizz.fileapi.rest.response.FileUploadResponse
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class FileService(
    private val fileRepository: FileRepository,
    private val mapper: ObjectMapper = jacksonObjectMapper(),
    private val logger: Logger
) {

    fun saveFile(fileUploadRequest: FileUploadRequest): FileUploadResponse {
        val token = UUID.randomUUID().toString()
        val file = File(
            name = fileUploadRequest.name,
            contentType = fileUploadRequest.contentType,
            meta = parseMeta(fileUploadRequest.meta),
            source = fileUploadRequest.source,
            expireTime = fileUploadRequest.expireTime?.let { Instant.parse(it) },
            content = fileUploadRequest.content.bytes,
            fileSize = fileUploadRequest.content.size,
            token = token
        )

        try {
            fileRepository.save(file)
        } catch (e: Exception) {
            logger.crit(LogItem("Exception while saving file " + fileUploadRequest.name))
            throw ServiceUnavailableException("Exception while saving file " + fileUploadRequest.name)
        }

        return FileUploadResponse(token)
    }

    fun getMetas(fileMetasRequest: FileMetasRequest): FileMetasResponse {
        val files = fileRepository.findByTokenIn(fileMetasRequest.tokens)
        return files.toFileMetasResponse()
    }

    fun getFile(token: String): FileResponse {
        val file = fileRepository.findByToken(token) ?: throw NotFoundException("No file with token $token")
        return FileResponse(
            content = file.content,
            fileName = file.name,
            fileSize = file.fileSize,
            contentType = file.contentType,
            createTime = file.createTime
        )
    }

    fun deleteFile(token: String) {
        fileRepository.findByToken(token)
            ?: throw NotFoundException("No file with token $token")

        try {
            fileRepository.deleteByToken(token)
        } catch (e: Exception) {
            logger.crit(LogItem("Exception while deleting file $token"))
            throw ServiceUnavailableException("Exception while deleting file $token")
        }
    }

    private fun parseMeta(metaJson: String): Map<String, Any> {
        try {
            return mapper.readValue(metaJson, Map::class.java) as Map<String, Any>
        } catch (e: JacksonException) {
            throw BadRequestException("Unable to parse meta field as JSON")
        }
    }

    private fun File.toFileMetaResponse(): FileMetaResponse {
        return FileMetaResponse(
            filename = this.name,
            contentType = this.contentType,
            size = this.fileSize,
            token = this.token,
            createTime = this.createTime,
            meta = this.meta,
            expireTime = this.expireTime
        )
    }

    private fun List<File>.toFileMetasResponse(): FileMetasResponse {
        val fileMetasMap = this.associateBy({ it.token }, { it.toFileMetaResponse() })
        return FileMetasResponse(fileMetasMap)
    }
}
