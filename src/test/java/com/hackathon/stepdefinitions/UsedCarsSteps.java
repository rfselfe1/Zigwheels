package com.hackathon.stepdefinitions;

import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.pages.UsedCarsPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UsedCarsSteps {

    private static final Logger logger =
            LogManager.getLogger(UsedCarsSteps.class);

    private UsedCarsPage usedCarsPage;
    private List<String> popularModels;

    @Given("I am on the used cars page for {string}")
    public void openUsedCarsPage(String city) {

        logger.info(
                "Opening the used-cars page for city: {}",
                city
        );

        usedCarsPage =
                new UsedCarsPage(
                        DriverManager.getDriver()
                );

        usedCarsPage.openForCity(city);
    }

    @When("I collect the popular used-car models")
    public void collectPopularModels() {

        popularModels =
                usedCarsPage.getPopularModels();

        logger.info(
                "Popular used-car models collected: {}",
                popularModels.size()
        );

        popularModels.forEach(
                model -> logger.info(
                        "Popular model: {}",
                        model
                )
        );
    }

    @Then("at least one popular model should be displayed")
    public void verifyPopularModelsExist() {

        Assert.assertFalse(
                popularModels.isEmpty(),
                "No popular used-car models were found"
        );

        logger.info(
                "Verified that the popular-model list is not empty"
        );
    }

    @Then("the popular model list should not contain duplicates")
    public void verifyNoDuplicateModels() {

        int originalSize =
                popularModels.size();

        int uniqueSize =
                new HashSet<>(popularModels).size();

        Assert.assertEquals(
                uniqueSize,
                originalSize,
                "The popular model list contains duplicates"
        );

        logger.info(
                "Verified {} popular models contain no duplicates",
                originalSize
        );
    }
}