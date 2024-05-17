package com.hrblizz.fileapi.rest.response

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FileMetaResponse(
    val token: String,
    val filename: String,
    val size: Long,
    val contentType: String,
    val createTime: Instant,
    val meta: Map<String, Any>,
    val expireTime: Instant?
)
