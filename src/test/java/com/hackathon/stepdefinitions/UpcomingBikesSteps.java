package com.hackathon.stepdefinitions;

import java.util.List;
import java.util.Locale;

import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.models.Bike;
import com.hackathon.pages.UpcomingBikesPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpcomingBikesSteps {

    private UpcomingBikesPage upcomingBikesPage;
    private List<Bike> allBikes;
    private List<Bike> matchingBikes;

    @Given("I am on the upcoming bikes page")
    public void openUpcomingBikesPage() {
        upcomingBikesPage =
                new UpcomingBikesPage(DriverManager.getDriver());

        upcomingBikesPage.open();
    }

    @When("I collect upcoming {string} bikes priced below {int} rupees")
    public void collectMatchingBikes(
            String manufacturer,
            int maximumPrice) {

        allBikes = upcomingBikesPage.getAllUpcomingBikes();

        matchingBikes = allBikes.stream()
                .filter(bike -> bike.getName()
                        .toLowerCase(Locale.ROOT)
                        .contains(manufacturer.toLowerCase(Locale.ROOT)))
                .filter(bike -> bike.getPrice() < maximumPrice)
                .toList();

        System.out.println(
                "Total upcoming bikes collected: " + allBikes.size()
        );

        System.out.println(
                "Matching " + manufacturer + " bikes: "
                        + matchingBikes.size()
        );

        matchingBikes.forEach(System.out::println);
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
    }

    @Then("each matching bike should cost less than {int} rupees")
    public void verifyBikePrices(int maximumPrice) {
        boolean allPricesAreBelowLimit = matchingBikes.stream()
                .allMatch(bike -> bike.getPrice() < maximumPrice);

        Assert.assertTrue(
                allPricesAreBelowLimit,
                "At least one bike exceeded the price limit"
        );
    }
}
