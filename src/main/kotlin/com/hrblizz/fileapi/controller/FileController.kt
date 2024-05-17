package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.rest.request.FileMetasRequest
import com.hrblizz.fileapi.rest.request.FileUploadRequest
import com.hrblizz.fileapi.rest.response.FileMetasResponse
import com.hrblizz.fileapi.rest.response.FileUploadResponse
import com.hrblizz.fileapi.service.FileService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FileController(
    private val fileService: FileService
) {

    @PostMapping("/files")
    fun uploadFile(
        fileUploadRequest: FileUploadRequest
    ): ResponseEntity<FileUploadResponse> {
        val tokenResponse = fileService.saveFile(fileUploadRequest)
        return ResponseEntity(tokenResponse, HttpStatus.CREATED)
    }

    @PostMapping("/files/metas")
    fun getMetas(
        @RequestBody fileMetasRequest: FileMetasRequest
    ): ResponseEntity<FileMetasResponse> {
        val metasResponse = fileService.getMetas(fileMetasRequest)
        return ResponseEntity(metasResponse, HttpStatus.OK)
    }

    @GetMapping("/file/{token}", MediaType.APPLICATION_OCTET_STREAM_VALUE)
    fun downloadFile(
        @PathVariable token: String
    ): ResponseEntity<ByteArray> {
        val file = fileService.getFile(token)
        val headers = HttpHeaders().apply {
            add("X-Filename", file.fileName)
            add("X-Filesize", file.fileSize.toString())
            add("X-CreateTime", file.createTime.toString())
            add(HttpHeaders.CONTENT_TYPE, file.contentType)
        }

        return ResponseEntity(file.content, headers, HttpStatus.OK)
    }

    @DeleteMapping("/file/{token}")
    fun deleteFile(
        @PathVariable token: String
    ): ResponseEntity<HttpStatus> {
        fileService.deleteFile(token)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
