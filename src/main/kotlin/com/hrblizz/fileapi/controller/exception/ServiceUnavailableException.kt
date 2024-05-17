package com.hrblizz.fileapi.controller.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class ServiceUnavailableException : RuntimeException {
    constructor(message: String) : super(message)
}
