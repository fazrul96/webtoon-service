package com.webtoon_service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "webtoon.scraping")
public class WebtoonScrapingProperties {
    private boolean headless;
}
