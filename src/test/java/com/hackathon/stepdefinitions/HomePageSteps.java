package com.hackathon.stepdefinitions;

import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.pages.HomePage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class HomePageSteps {

    private HomePage homePage;

    @Given("I open the ZigWheels website")
    public void openZigWheelsWebsite() {
        homePage = new HomePage(DriverManager.getDriver());
        homePage.open();
    }

    @Then("the page title should not be empty")
    public void verifyPageTitle() {
        String title = homePage.getPageTitle();

        System.out.println("Page title: " + title);

        Assert.assertFalse(
                title.isBlank(),
                "The page title should not be empty"
        );
    }

    @Then("the current URL should contain {string}")
    public void verifyCurrentUrl(String expectedText) {
        String currentUrl = homePage.getCurrentUrl();

        System.out.println("Current URL: " + currentUrl);

        Assert.assertTrue(
                currentUrl.contains(expectedText),
                "Expected URL to contain: " + expectedText
        );
    }
}