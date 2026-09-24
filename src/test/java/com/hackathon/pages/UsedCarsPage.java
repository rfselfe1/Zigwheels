package com.hackathon.pages;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsedCarsPage {

    private static final Logger logger =
            LogManager.getLogger(UsedCarsPage.class);

    private static final String USED_CARS_URL =
            "https://www.zigwheels.com/used-car/";

    private static final By POPULAR_MODEL_LINKS =
            By.cssSelector(
                    "#models-table tbody tr td:first-child a"
            );

    private final WebDriver driver;
    private final WebDriverWait wait;

    public UsedCarsPage(WebDriver driver) {

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(15)
                );
    }

    public void openForCity(String city) {

        String cityPath =
                city.trim().replace(" ", "-");

        driver.get(
                USED_CARS_URL + cityPath
        );

        logger.info(
                "Opened used-cars URL: {}",
                driver.getCurrentUrl()
        );
    }

    public List<String> getPopularModels() {

        List<WebElement> modelLinks =
                wait.until(
                        ExpectedConditions
                                .presenceOfAllElementsLocatedBy(
                                        POPULAR_MODEL_LINKS
                                )
                );

        logger.info(
                "Popular-model elements found: {}",
                modelLinks.size()
        );

        return modelLinks.stream()
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
    }
}