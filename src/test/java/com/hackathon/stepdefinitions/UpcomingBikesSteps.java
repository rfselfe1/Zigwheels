package com.hackathon.stepdefinitions;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.models.Bike;
import com.hackathon.pages.UpcomingBikesPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpcomingBikesSteps {

    private static final Logger logger =
            LogManager.getLogger(UpcomingBikesSteps.class);

    private UpcomingBikesPage upcomingBikesPage;
    private List<Bike> allBikes;
    private List<Bike> matchingBikes;

    @Given("I am on the upcoming bikes page")
    public void openUpcomingBikesPage() {

        logger.info("Opening the upcoming bikes page");

        upcomingBikesPage =
                new UpcomingBikesPage(
                        DriverManager.getDriver()
                );

        upcomingBikesPage.open();
    }

    @When("I collect upcoming {string} bikes priced below {int} rupees")
    public void collectMatchingBikes(
            String manufacturer,
            int maximumPrice) {

        allBikes =
                upcomingBikesPage.getAllUpcomingBikes();

        matchingBikes = allBikes.stream()
                .filter(bike -> bike.getName()
                        .toLowerCase(Locale.ROOT)
                        .contains(
                                manufacturer.toLowerCase(
                                        Locale.ROOT
                                )
                        )
                )
                .filter(bike ->
                        bike.getPrice() < maximumPrice
                )
                .toList();

        logger.info(
                "Total upcoming bikes collected: {}",
                allBikes.size()
        );

        logger.info(
                "Matching {} bikes below {} rupees: {}",
                manufacturer,
                maximumPrice,
                matchingBikes.size()
        );

        matchingBikes.forEach(
                bike -> logger.info(
                        "Matching bike: {}",
                        bike
                )
        );
    }

    @Then("at least one matching bike should be displayed")
    public void verifyMatchingBikesExist() {

        Assert.assertFalse(
                matchingBikes.isEmpty(),
                "No matching Honda bikes were found"
        );
    }

    @Then("each matching bike should have a name, price and expected launch date")
    public void verifyBikeDetails() {

        for (Bike bike : matchingBikes) {

            Assert.assertFalse(
                    bike.getName().isBlank(),
                    "Bike name should not be empty"
            );

            Assert.assertTrue(
                    bike.getPrice() > 0,
                    "Bike price should be greater than zero"
            );

            Assert.assertFalse(
                    bike.getExpectedLaunchDate().isBlank(),
                    "Expected launch date should not be empty"
            );
        }

        logger.info(
                "Verified details for {} matching bikes",
                matchingBikes.size()
        );
    }

    @Then("each matching bike should cost less than {int} rupees")
    public void verifyBikePrices(int maximumPrice) {

        boolean allPricesAreBelowLimit =
                matchingBikes.stream()
                        .allMatch(bike ->
                                bike.getPrice() < maximumPrice
                        );

        Assert.assertTrue(
                allPricesAreBelowLimit,
                "At least one bike exceeded the price limit"
        );

        logger.info(
                "Verified that every matching bike costs below {} rupees",
                maximumPrice
        );
    }
}
