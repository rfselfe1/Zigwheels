package com.hackathon.pages;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hackathon.models.Bike;

public class UpcomingBikesPage {

    private static final Logger logger =
            LogManager.getLogger(UpcomingBikesPage.class);

    private static final String UPCOMING_BIKES_URL =
            "https://www.zigwheels.com/upcoming-bikes";

    private static final By BIKE_CARDS =
            By.cssSelector(
                    "#modelList li.modelItem"
            );

    private static final By BIKE_IMAGE =
            By.cssSelector(
                    "img[data-track-label='model-image']"
            );

    private final WebDriver driver;
    private final WebDriverWait wait;

    public UpcomingBikesPage(WebDriver driver) {

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(15)
                );
    }

    public void open() {

        driver.get(UPCOMING_BIKES_URL);

        logger.info(
                "Opened upcoming-bikes page: {}",
                driver.getCurrentUrl()
        );
    }

    public List<Bike> getAllUpcomingBikes() {

        List<WebElement> cards =
                wait.until(
                        ExpectedConditions
                                .presenceOfAllElementsLocatedBy(
                                        BIKE_CARDS
                                )
                );

        logger.info(
                "Upcoming-bike cards found: {}",
                cards.size()
        );

        List<Bike> bikes =
                new ArrayList<>();

        for (WebElement card : cards) {

            try {

                String name =
                        card.findElement(BIKE_IMAGE)
                                .getAttribute("alt")
                                .trim();

                String priceText =
                        card.getAttribute(
                                "data-price"
                        );

                String launchTimestamp =
                        card.getAttribute(
                                "data-explaunch"
                        );

                if (name.isBlank()
                        || priceText.isBlank()
                        || launchTimestamp.isBlank()) {

                    logger.warn(
                            "Skipping a bike card because required data is blank"
                    );

                    continue;
                }

                int price =
                        Integer.parseInt(
                                priceText
                        );

                String launchDate =
                        convertTimestampToDate(
                                launchTimestamp
                        );

                bikes.add(
                        new Bike(
                                name,
                                price,
                                launchDate
                        )
                );

            } catch (RuntimeException exception) {

                logger.warn(
                        "Skipped a bike card because its data was incomplete: {}",
                        exception.getMessage()
                );
            }
        }

        logger.info(
                "Successfully extracted {} upcoming bikes",
                bikes.size()
        );

        return bikes;
    }

    private String convertTimestampToDate(
            String timestamp) {

        long epochSeconds =
                Long.parseLong(timestamp);

        return Instant.ofEpochSecond(
                        epochSeconds
                )
                .atZone(
                        ZoneId.of(
                                "Asia/Kolkata"
                        )
                )
                .toLocalDate()
                .format(
                        DateTimeFormatter.ofPattern(
                                "dd MMM yyyy"
                        )
                );
    }
}



