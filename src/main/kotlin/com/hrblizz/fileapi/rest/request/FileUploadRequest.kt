package com.hrblizz.fileapi.rest.request

import org.springframework.web.multipart.MultipartFile

data class FileUploadRequest(
    val name: String,
    val contentType: String,
    val meta: String,
    val source: String,
    val expireTime: String?,
    val content: MultipartFile
)
