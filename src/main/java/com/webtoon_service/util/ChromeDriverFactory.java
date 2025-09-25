package com.webtoon_service.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverFactory {
    private ChromeDriverFactory() {}

    public static WebDriver createDriver(boolean headlessMode) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = createChromeOptions(headlessMode);
        return new ChromeDriver(options);
    }

    private static ChromeOptions createChromeOptions(boolean headlessMode) {
        ChromeOptions options = new ChromeOptions();

        if (headlessMode) {
            configureHeadlessOptions(options);
        }

        configureCommonOptions(options);
        return options;
    }

    private static void configureHeadlessOptions(ChromeOptions options) {
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
    }

    private static void configureCommonOptions(ChromeOptions options) {
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
    }
}
