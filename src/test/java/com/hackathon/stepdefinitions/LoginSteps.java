package com.hackathon.stepdefinitions;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.pages.LoginPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

    private static final Logger logger =
            LogManager.getLogger(LoginSteps.class);

    private LoginPage loginPage;
    private String validationError;

    @Given("I open the ZigWheels login modal")
    public void openLoginModal() {

        logger.info(
                "Opening the ZigWheels login modal"
        );

        loginPage =
                new LoginPage(
                        DriverManager.getDriver()
                );

        loginPage.openLoginModal();
    }

    @When("I choose Google sign-in")
    public void chooseGoogleSignIn() {

        logger.info(
                "Selecting Google sign-in"
        );

        loginPage.chooseGoogleSignIn();
    }

    @When("I submit the invalid Google account {string}")
    public void submitInvalidGoogleAccount(String email) {

        logger.info(
                "Submitting an invalid Google account"
        );

        loginPage.submitInvalidAccount(email);
    }

    @Then("a Google account validation error should be displayed")
    public void verifyValidationError() {

        validationError =
                loginPage.getValidationError();

        logger.info(
                "Captured Google validation error: {}",
                validationError
        );

        Assert.assertFalse(
                validationError.isBlank(),
                "Google validation error should not be empty"
        );

        String normalizedError =
                validationError.toLowerCase(
                        Locale.ROOT
                );

        boolean recognizedGoogleError =
                normalizedError.contains(
                        "find your google account"
                )
                || normalizedError.contains(
                        "sign you in"
                )
                || normalizedError.contains(
                        "browser or app may not be secure"
                );

        Assert.assertTrue(
                recognizedGoogleError,
                "Unexpected Google message captured: "
                        + validationError
        );

        logger.info(
                "Verified that the captured message is a recognized Google validation error"
        );
    }
}