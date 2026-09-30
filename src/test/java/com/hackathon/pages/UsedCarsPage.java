package com.hackathon.pages;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsedCarsPage {

    private static final Logger logger =
            LogManager.getLogger(UsedCarsPage.class);

    private static final String USED_CARS_URL =
            "https://www.zigwheels.com/used-car/";

    private static final By POPULAR_MODEL_LINKS =
            By.cssSelector(
                    "#models-table tbody tr "
                            + "td:first-child a"
            );

    private final WebDriver driver;
    private final WebDriverWait wait;

    public UsedCarsPage(WebDriver driver) {
        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );
    }

    public void openForCity(String city) {
        String cityPath = city.trim()
                .replace(" ", "-");

        driver.get(
                USED_CARS_URL + cityPath
        );

        logger.info(
                "Opened used-cars URL: {}",
                driver.getCurrentUrl()
        );
    }

    public List<String> getPopularModels() {
        List<String> popularModels =
                wait.until(currentDriver -> {
                    try {
                        List<WebElement> modelLinks =
                                currentDriver.findElements(
                                        POPULAR_MODEL_LINKS
                                );

                        if (modelLinks.isEmpty()) {
                            return null;
                        }

                        List<String> models =
                                modelLinks.stream()
                                        .map(element ->
                                                element.getAttribute(
                                                        "textContent"
                                                )
                                        )
                                        .map(String::trim)
                                        .filter(model ->
                                                !model.isBlank()
                                        )
                                        .toList();

                        if (models.isEmpty()) {
                            return null;
                        }

                        return models;

                    } catch (
                            StaleElementReferenceException
                                    exception) {

                        logger.debug(
                                "Used-car table changed "
                                        + "during extraction; "
                                        + "re-locating elements"
                        );

                        return null;
                    }
                });

        logger.info(
                "Popular used-car models collected: {}",
                popularModels.size()
        );

        return popularModels;
    }
}