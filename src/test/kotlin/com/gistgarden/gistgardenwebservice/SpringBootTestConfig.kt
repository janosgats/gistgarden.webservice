package com.gistgarden.gistgardenwebservice

import com.zaxxer.hikari.HikariDataSource
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.session.jdbc.JdbcIndexedSessionRepository

@TestConfiguration
class SpringBootTestConfig {
    @Bean
    fun dataSource(): HikariDataSource {
        return Mockito.mock(HikariDataSource::class.java)
    }

    @Bean
    fun jdbcIndexedSessionRepository(): JdbcIndexedSessionRepository {
        return Mockito.mock(JdbcIndexedSessionRepository::class.java)
    }
}