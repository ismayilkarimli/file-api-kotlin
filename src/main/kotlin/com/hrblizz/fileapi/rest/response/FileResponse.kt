package com.hrblizz.fileapi.rest.response

import java.time.Instant

data class FileResponse(
    val content: ByteArray,
    val fileName: String,
    val fileSize: Long,
    val contentType: String,
    val createTime: Instant
)
