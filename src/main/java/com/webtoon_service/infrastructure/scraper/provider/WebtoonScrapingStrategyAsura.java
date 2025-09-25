package com.webtoon_service.infrastructure.scraper.provider;

import com.webtoon_service.config.WebtoonProviderConfig;
import com.webtoon_service.infrastructure.downloader.ImageDownloader;
import com.webtoon_service.infrastructure.downloader.ZipCompressor;
import com.webtoon_service.infrastructure.scraper.WebtoonScrapingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.webtoon_service.constants.ApiConstant.WEBTOON.ASURA_BASE_URL;
import static com.webtoon_service.constants.CssSelectorsConstants.CSS_CLOSE_MODAL_BUTTON;
import static com.webtoon_service.constants.GeneralConstants.ZIP_FILE_EXTENSION;
import static com.webtoon_service.constants.RegexConstants.ASURA_IMAGE_SEQUENCE_REGEX;
import static com.webtoon_service.constants.WebtoonConstants.*;
import static com.webtoon_service.constants.XPathConstants.*;

@Slf4j
@RequiredArgsConstructor
public class WebtoonScrapingStrategyAsura implements WebtoonScrapingStrategy {
    @Override
    public void scrape(WebDriver driver, WebtoonProviderConfig provider, String title, String chapter) {
        String webtoonUrl = provider.buildWebtoonUrl(title).trim();

        driver.get(webtoonUrl);
        log.info("Navigating to webtoon URL: {}", webtoonUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        closeModalIfPresent(wait);

        inputChapter(driver, chapter);
        findAndClickChapterLink(driver, chapter);
        scrollToBottom(driver);
    }

    @Override
    public List<String> extractImageUrls(WebDriver driver) {
        List<WebElement> images = driver.findElements(By.xpath(XPATH_IMAGE_ELEMENT));

        List<ImageWithSequence> collected = new ArrayList<>();

        for (WebElement img : images) {
            String src = Optional.ofNullable(img.getAttribute("data-src"))
                    .filter(s -> !s.isEmpty())
                    .orElse(img.getAttribute("src"));

            if (src == null || !src.endsWith("-optimized.webp") || src.contains("logo")) {
                continue;
            }

            Integer sequence = extractSequenceFromUrl(src);
            if (sequence != null) {
                collected.add(new ImageWithSequence(sequence, src));
            } else {
                log.warn("❓ Could not extract sequence from image: {}", src);
            }
        }

        collected.sort(Comparator.comparingInt(ImageWithSequence::sequence));

        List<String> sortedUrls = collected.stream()
                .map(ImageWithSequence::url)
                .collect(Collectors.toList());

        log.info("✅ Collected and sorted {} images for Asura", sortedUrls.size());
        return sortedUrls;
    }

    @Override
    public Path downloadAndZipImages(List<String> imageUrls, String title, String chapter) throws Exception {
        String baseName = String.format(WEBTOON_CHAPTER_FILENAME_FORMAT, title, chapter);

        // Temporary directory is created under the OS temp folder, e.g.,
        // Windows: C:\Users\<User>\AppData\Local\Temp
        Path tempDir = Files.createTempDirectory(TEMP_DIR_PREFIX + baseName);
        Path downloadDir = tempDir.resolve(IMAGES_DIR_NAME);
        Files.createDirectories(downloadDir);

        Path zipFile = tempDir.resolve(baseName + ZIP_FILE_EXTENSION);

        ImageDownloader.downloadImages(imageUrls, downloadDir, baseName, MIN_IMAGE_SIZE_BYTES_ASURA, ASURA_BASE_URL);
        ZipCompressor.zipFolder(downloadDir, zipFile);

        log.info("Created zip file at: {}", zipFile.toAbsolutePath());
        return zipFile;
    }

    private void closeModalIfPresent(WebDriverWait wait) {
        try {
            WebElement closeButton = wait.until(d -> d.findElement(By.cssSelector(CSS_CLOSE_MODAL_BUTTON)));
            log.info("🧯 Popup detected, closing...");
            closeButton.click();
            Thread.sleep(2000);
        } catch (TimeoutException e) {
            log.info("✅ No popup appeared.");
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while waiting for popup to close", ie);
        }
    }

    // Helper record for image ordering
    private record ImageWithSequence(int sequence, String url) {}

    // Allows leading zero sequences like 00-optimized.webp
    private Integer extractSequenceFromUrl(String url) {
        Matcher matcher = Pattern.compile(ASURA_IMAGE_SEQUENCE_REGEX).matcher(url);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    private void inputChapter(WebDriver driver, String chapter) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement chapterInput = wait.until(d -> d.findElement(By.xpath(XPATH_CHAPTER_INPUT_ASURA)));

        chapterInput.clear();
        chapterInput.sendKeys(chapter);
        chapterInput.sendKeys(Keys.RETURN);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format(XPATH_CHAPTER_LINK_TEMPLATE, chapter))));
    }

    private void findAndClickChapterLink(WebDriver driver, String chapter) {
        String xpath = "//a[contains(., 'Chapter " + chapter + "')]";

        try {
            WebElement chapterLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> d.findElement(By.xpath(xpath)));

            String chapterHref = chapterLink.getAttribute("href");
            log.info("Found chapter link: {}", chapterHref);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", chapterLink);
            Thread.sleep(500); // short wait for any animation or overlay

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chapterLink);


            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> !d.getCurrentUrl().equals("about:blank"));

            String currentUrl = driver.getCurrentUrl();
            log.info("Navigated to: {}", currentUrl);

            sleepSeconds(3);
        } catch (NoSuchElementException | TimeoutException e) {
            log.error("❌ Could not find chapter link for Chapter {}", chapter);
        } catch (Exception e) {
            log.error("🔥 Error while finding and clicking chapter link for Chapter {}", chapter, e);
        }
    }

    private void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        sleepSeconds(2);
    }

    private void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}