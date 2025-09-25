package com.webtoon_service.component;

import com.webtoon_service.constants.ApiConstant;
import com.webtoon_service.domain.data.WebtoonSuffixResolver;
import com.webtoon_service.util.ChromeDriverFactory;
import com.webtoon_service.util.WebtoonSlugUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.webtoon_service.constants.ModelConstants.ASURA;

@Slf4j
@Component
public class SuffixResolverAsura implements WebtoonSuffixResolver {
    @Override
    public boolean supports(String provider) {
        return ASURA.equalsIgnoreCase(provider);
    }

    @Override
    public Map<String, String> resolve(String provider, String title, boolean headlessMode) {
        WebDriver driver = ChromeDriverFactory.createDriver(headlessMode);
        try {
            String searchUrl = ApiConstant.WEBTOON.getAsuraSearchUrl(URLEncoder.encode(title, StandardCharsets.UTF_8));
            driver.get(searchUrl);
            log.info("🌐 Navigating to: {}", searchUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            closeModalIfPresent(wait);
            String normalizedInput = normalize(title);

            List<WebElement> links = driver.findElements(By.cssSelector("a[href^='/series/']"));
            for (WebElement link : links) {
                String href = link.getAttribute("href");
                if (href == null || !href.contains("/series/")) continue;

                String slug = href.substring(href.lastIndexOf("/") + 1);
                String linkText = link.getText().trim();

                if (linkText.isEmpty()) continue;
                String normalizedLinkText = normalize(linkText);

                log.debug("🔍 Comparing [{}] with [{}]", normalizedInput, normalizedLinkText);

                if (normalizedLinkText.contains(normalizedInput)) {
                    log.info("✅ Match found: {} => {}", linkText, slug);
                    String suffix = WebtoonSlugUtil.extractAsuraSuffix(slug);
                    String cleanTitle = WebtoonSlugUtil.extractAsuraTitle(slug);

                    Map<String, String> result = new HashMap<>();
                    result.put("provider", provider);
                    result.put("title", cleanTitle);
                    result.put("suffix", suffix);
                    return result;
                }
            }

            log.warn("❌ No matching title found for: {}", title);
            return Collections.emptyMap(); // no match found

        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve Asura suffix", e);
        } finally {
            driver.quit();
        }
    }

    private void closeModalIfPresent(WebDriverWait wait) {
        try {
            WebElement closeButton = wait.until(d -> d.findElement(By.cssSelector("button[aria-label='Close modal']")));
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

    private static String normalize(String str) {
        return str == null ? "" : str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
}