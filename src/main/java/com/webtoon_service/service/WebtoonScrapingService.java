package com.webtoon_service.service;

import com.webtoon_service.infrastructure.storage.S3StorageClient;
import com.webtoon_service.config.WebtoonProviderConfig;
import com.webtoon_service.infrastructure.scraper.WebtoonScrapingStrategy;
import com.webtoon_service.domain.dto.WebtoonProviderDto;
import com.webtoon_service.infrastructure.scraper.WebtoonScrapingStrategyFactory;
import com.webtoon_service.util.ChromeDriverFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static com.webtoon_service.constants.ApiConstant.WEBTOON.S3_WEBTOON_BUCKET;
import static com.webtoon_service.constants.GeneralConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebtoonScrapingService {
    private final S3StorageClient storageClient;

    public List<String> uploadWebtoonChapter(
            WebtoonProviderDto webtoonProviderDto,
            String title, String chapter,
            boolean headless
    ) {
        WebDriver driver = ChromeDriverFactory.createDriver(headless);

        try {
            WebtoonProviderConfig provider = new WebtoonProviderConfig(
                    webtoonProviderDto.getId(),
                    webtoonProviderDto.getSuffix()
            );

            WebtoonScrapingStrategy strategy = WebtoonScrapingStrategyFactory.getStrategy(webtoonProviderDto.getId());
            strategy.scrape(driver, provider, title, chapter);

            List<String> imageUrls = strategy.extractImageUrls(driver);
            if (imageUrls.isEmpty()) {
                throw new IllegalArgumentException("⚠️ No valid images found for chapter " + chapter);
            }

            Path zipFile = strategy.downloadAndZipImages(imageUrls, title, chapter);
            String s3Prefix = buildS3PathPrefix(title);
            List<String> uploadedUrl = storageClient.uploadFiles(zipFile, s3Prefix)
                    .blockOptional()
                    .orElseThrow(() -> new IllegalStateException("Upload failed"));

            deleteDirectory(zipFile.getParent());
            return uploadedUrl;
        } catch (Exception e) {
            throw new RuntimeException("Webtoon scraping failed", e);
        } finally {
            driver.quit();
        }
    }

    private String buildS3PathPrefix(String title) {
        return S3_WEBTOON_BUCKET + title.toLowerCase().replace(SINGLE_SPACE, DASH) + SLASH;
    }

    public static void deleteDirectory(Path directory) {
        if (Files.notExists(directory)) return;

        try (var paths = Files.walk(directory)) {
            paths
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception e) {
                            log.warn("❌ Failed to delete: {}", path, e);
                        }
                    });
            log.info("🧹 Deleted folder and contents: {}", directory);
        } catch (Exception e) {
            log.error("❌ Failed to recursively delete folder: {}", directory, e);
        }
    }
}
