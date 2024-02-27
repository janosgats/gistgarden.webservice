package com.gistgarden.gistgardenwebservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.security.SecureRandom

@Configuration
class AppConfig {

    @Primary
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(11, SecureRandom())
    }
}