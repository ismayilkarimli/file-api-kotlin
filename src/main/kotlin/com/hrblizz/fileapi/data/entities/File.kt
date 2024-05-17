package com.hrblizz.fileapi.data.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import java.time.Instant

data class File(
    @Id
    val id: String? = null,
    var name: String,
    var contentType: String,
    var meta: Map<String, Any>,
    var source: String,
    var expireTime: Instant?,
    var content: ByteArray,
    val token: String,
    @CreatedDate
    var createTime: Instant = Instant.now(),
    var fileSize: Long
)
