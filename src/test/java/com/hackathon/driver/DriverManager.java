package com.hackathon.driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER =
            new ThreadLocal<>();

    private DriverManager() {
        // Prevents this utility class from being instantiated.
    }

    public static void createDriver() {
        if (DRIVER.get() != null) {
            throw new IllegalStateException(
                    "WebDriver already exists for this test thread"
            );
        }

        String execution = System.getProperty(
                "execution",
                "local"
        );

        ChromeOptions options = createChromeOptions();

        WebDriver driver;

        if ("remote".equalsIgnoreCase(execution)) {
            driver = createRemoteDriver(options);
        } else {
            driver = new ChromeDriver(options);
        }

        driver.manage()
                .window()
                .setSize(new Dimension(1920, 1080));

        DRIVER.set(driver);
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--disable-notifications",
                "--disable-popup-blocking",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--window-size=1920,1080"
        );

        boolean headless = Boolean.parseBoolean(
                System.getProperty(
                        "headless",
                        "false"
                )
        );

        if (headless) {
            options.addArguments("--headless=new");
        }

        return options;
    }

    private static WebDriver createRemoteDriver(
            ChromeOptions options) {

        String seleniumUrl = System.getProperty(
                "selenium.url",
                "http://localhost:4444/wd/hub"
        );

        try {
            return new RemoteWebDriver(
                    new URL(seleniumUrl),
                    options
            );
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException(
                    "Invalid Selenium Grid URL: "
                            + seleniumUrl,
                    exception
            );
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been created "
                            + "for this test thread"
            );
        }

        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();

        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            DRIVER.remove();
        }
    }
}