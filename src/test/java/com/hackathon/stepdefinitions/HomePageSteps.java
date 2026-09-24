package com.hackathon.stepdefinitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.pages.HomePage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class HomePageSteps {

    private static final Logger logger =
            LogManager.getLogger(HomePageSteps.class);

    private HomePage homePage;

    @Given("I open the ZigWheels website")
    public void openZigWheelsWebsite() {

        logger.info("Opening the ZigWheels home page");

        homePage =
                new HomePage(DriverManager.getDriver());

        homePage.open();
    }

    @Then("the page title should not be empty")
    public void verifyPageTitle() {

        String title = homePage.getPageTitle();

        logger.info(
                "Page title: {}",
                title
        );

        Assert.assertFalse(
                title.isBlank(),
                "The page title should not be empty"
        );
    }

    @Then("the current URL should contain {string}")
    public void verifyCurrentUrl(String expectedText) {

        String currentUrl = homePage.getCurrentUrl();

        logger.info(
                "Current URL: {}",
                currentUrl
        );

        Assert.assertTrue(
                currentUrl.contains(expectedText),
                "Expected URL to contain: " + expectedText
        );
    }
}