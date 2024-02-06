package com.gistgarden.gistgardenwebservice.config.session

import org.springframework.session.SessionIdGenerator
import java.security.SecureRandom
import java.util.*

class SecureRandomSessionIdGenerator : SessionIdGenerator {
    private val base64Encoder = Base64.getEncoder()
    private val secureRandom = SecureRandom()

    override fun generate(): String {
        val randomBytes = ByteArray(32)
        secureRandom.nextBytes(randomBytes)
        return String(base64Encoder.encode(randomBytes))
    }
}