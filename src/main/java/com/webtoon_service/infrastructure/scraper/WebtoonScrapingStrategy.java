package com.webtoon_service.infrastructure.scraper;

import com.webtoon_service.config.WebtoonProviderConfig;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.util.List;

public interface WebtoonScrapingStrategy {
    /**
     * Scrapes the webtoon provider's page, navigates, and inputs the chapter.
     * @param driver WebDriver to navigate the page.
     * @param provider The provider's configuration.
     * @param title The webtoon title.
     * @param chapter The chapter to be scraped.
     */
    void scrape(WebDriver driver, WebtoonProviderConfig provider, String title, String chapter);

    /**
     * Extracts the image URLs from the webtoon page.
     * @param driver WebDriver to extract image URLs from the page.
     * @return A list of image URLs.
     */
    List<String> extractImageUrls(WebDriver driver);

    /**
     * Downloads and zips the images.
     * @param imageUrls The URLs of the images.
     * @param title The title of the webtoon.
     * @param chapter The chapter of the webtoon.
     * @return The path to the created zip file.
     * @throws Exception If any error occurs while downloading or zipping.
     */
    Path downloadAndZipImages(List<String> imageUrls, String title, String chapter) throws Exception;
}
