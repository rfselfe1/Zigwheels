package com.hackathon.pages;

import java.time.Duration;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PassiveSecurityPage {

    private static final Logger logger =
            LogManager.getLogger(
                    PassiveSecurityPage.class
            );

    private static final String HOME_URL =
            "https://www.zigwheels.com/";

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PassiveSecurityPage(WebDriver driver) {

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(20)
                );
    }

    public void open() {

        driver.get(HOME_URL);

        wait.until(currentDriver ->
                "complete".equals(
                        ((JavascriptExecutor)
                                currentDriver)
                                .executeScript(
                                        "return document.readyState"
                                )
                )
        );

        logger.info(
                "Opened ZigWheels for passive security inspection: {}",
                driver.getCurrentUrl()
        );
    }

    public boolean isUsingHttps() {

        return driver.getCurrentUrl()
                .toLowerCase()
                .startsWith("https://");
    }

    public Set<Cookie> getVisibleCookies() {

        return driver.manage()
                .getCookies();
    }

    public long countCookiesWithoutSecureFlag() {

        return getVisibleCookies()
                .stream()
                .filter(cookie ->
                        !cookie.isSecure()
                )
                .count();
    }

    public long countCookiesWithoutHttpOnlyFlag() {

        return getVisibleCookies()
                .stream()
                .filter(cookie ->
                        !cookie.isHttpOnly()
                )
                .count();
    }

    public long countInsecureResourceReferences() {

        Object result =
                ((JavascriptExecutor) driver)
                        .executeScript(
                                """
                                return Array.from(
                                    document.querySelectorAll(
                                        '[src], [href]'
                                    )
                                )
                                .map(element =>
                                    element.getAttribute('src')
                                    || element.getAttribute('href')
                                )
                                .filter(value =>
                                    value
                                    && value.toLowerCase()
                                        .startsWith('http://')
                                )
                                .length;
                                """
                        );

        return ((Number) result).longValue();
    }

    public long countInsecureFormActions() {

        Object result =
                ((JavascriptExecutor) driver)
                        .executeScript(
                                """
                                return document.querySelectorAll(
                                    "form[action^='http://']"
                                ).length;
                                """
                        );

        return ((Number) result).longValue();
    }
}