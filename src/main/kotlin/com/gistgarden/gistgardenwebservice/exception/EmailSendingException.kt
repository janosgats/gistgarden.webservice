package com.gistgarden.gistgardenwebservice.exception

class EmailSendingException constructor(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)