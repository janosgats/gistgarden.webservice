package com.gistgarden.gistgardenwebservice.config

import com.sendgrid.SendGrid
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmailConfig(
    @param:Value("\${email.sendGrid.apiKey}")
    private val sendGridApiKey: String,
) {

    @Bean
    fun sendGrid(): SendGrid {
        return SendGrid(sendGridApiKey)
    }
}