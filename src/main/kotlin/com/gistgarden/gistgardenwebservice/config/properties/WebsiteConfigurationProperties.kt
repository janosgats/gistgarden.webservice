package com.gistgarden.gistgardenwebservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("website")
class WebsiteConfigurationProperties {
    final var mainSiteBaseUrl: String? = null
}