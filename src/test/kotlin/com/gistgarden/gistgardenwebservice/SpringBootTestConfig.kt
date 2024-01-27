package com.gistgarden.gistgardenwebservice

import com.zaxxer.hikari.HikariDataSource
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class SpringBootTestConfig {
    @Bean
    fun dtaSource(): HikariDataSource {
        return Mockito.mock(HikariDataSource::class.java)
    }
}