package com.hackathon.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;

public class AccessibilityPage {

    private static final Logger logger =
            LogManager.getLogger(
                    AccessibilityPage.class
            );

    private static final String HOME_URL =
            "https://www.zigwheels.com/";

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AccessibilityPage(WebDriver driver) {

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
                "Opened ZigWheels for accessibility testing: {}",
                driver.getCurrentUrl()
        );
    }

    public Results runAudit() {

        logger.info(
                "Starting axe accessibility audit"
        );

        Results results =
                new AxeBuilder()
                        .analyze(driver);

        logger.info(
                "Accessibility audit completed"
        );

        return results;
    }
}