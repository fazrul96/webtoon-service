package com.webtoon_service.infrastructure.scraper.provider;

import com.webtoon_service.config.WebtoonProviderConfig;
import com.webtoon_service.infrastructure.downloader.ImageDownloader;
import com.webtoon_service.infrastructure.downloader.ZipCompressor;
import com.webtoon_service.infrastructure.scraper.WebtoonScrapingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.webtoon_service.constants.ApiConstant.WEBTOON.COMICK_BASE_URL;
import static com.webtoon_service.constants.GeneralConstants.ZIP_FILE_EXTENSION;
import static com.webtoon_service.constants.WebtoonConstants.*;
import static com.webtoon_service.constants.XPathConstants.XPATH_CHAPTER_INPUT_COMICK;

@Slf4j
public class WebtoonScrapingStrategyComick implements WebtoonScrapingStrategy {

    @Override
    public void scrape(WebDriver driver, WebtoonProviderConfig provider, String title, String chapter) {
        String webtoonUrl = provider.buildWebtoonUrl(title).trim();

        driver.get(webtoonUrl);
        log.info("Navigating to webtoon URL: {}", webtoonUrl);

        inputChapter(driver, chapter);
        findAndClickChapterLink(driver, chapter);
        scrollToBottom(driver);
    }

    @Override
    public List<String> extractImageUrls(WebDriver driver) {
        List<String> imageUrls = driver.findElements(By.tagName("img")).stream()
                .map(e -> e.getAttribute("src"))
                .filter(Objects::nonNull)
                .filter(src -> src.matches(".*\\.(jpg|jpeg|png|webp)$"))
                .collect(Collectors.toList());

        log.info("Found {} image URLs", imageUrls.size());
        return imageUrls;
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

        ImageDownloader.downloadImages(imageUrls, downloadDir, baseName, MIN_IMAGE_SIZE_BYTES_COMICK, COMICK_BASE_URL);
        ZipCompressor.zipFolder(downloadDir, zipFile);

        log.info("Created zip file at: {}", zipFile.toAbsolutePath());
        return zipFile;
    }

    private void inputChapter(WebDriver driver, String chapter) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement chapterInput = wait.until(d -> d.findElement(By.xpath(XPATH_CHAPTER_INPUT_COMICK)));

        chapterInput.clear();
        chapterInput.sendKeys(chapter);
        chapterInput.sendKeys(Keys.RETURN);
        sleepSeconds(10);
    }

    private void findAndClickChapterLink(WebDriver driver, String chapter) {
        String xpath = "//a[.//span[@title='Chapter " + chapter + "']]";
        WebElement chapterLink = driver.findElement(By.xpath(xpath));
        chapterLink.click();
        sleepSeconds(3);
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