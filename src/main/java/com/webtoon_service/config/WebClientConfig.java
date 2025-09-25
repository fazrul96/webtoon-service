package com.webtoon_service.config;

import com.webtoon_service.properties.StorageServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final StorageServiceProperties storageServiceProperties;

    @Bean
    public WebClient storageWebClient() {
        return WebClient.builder()
                .baseUrl(storageServiceProperties.getBaseUrl())
                .build();
    }
}
