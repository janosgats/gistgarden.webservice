package com.gistgarden.gistgardenwebservice.config.session

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class SessionRepoConfig {

    @Primary
    @Bean
    fun secureRandomSessionIdGenerator(): SecureRandomSessionIdGenerator{
        return SecureRandomSessionIdGenerator()
    }
}