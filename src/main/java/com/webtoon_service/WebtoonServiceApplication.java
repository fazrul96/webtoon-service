package com.webtoon_service;

import com.webtoon_service.properties.StorageServiceProperties;
import com.webtoon_service.properties.WebtoonScrapingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({
        StorageServiceProperties.class,
        WebtoonScrapingProperties.class
})
@SuppressWarnings({"PMD.UseUtilityClass"})
public class WebtoonServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebtoonServiceApplication.class, args);
    }
}
