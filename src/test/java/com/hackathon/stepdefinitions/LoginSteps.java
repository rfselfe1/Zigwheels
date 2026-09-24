package com.hackathon.stepdefinitions;

import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.pages.LoginPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

    private LoginPage loginPage;
    private String validationError;

    @Given("I open the ZigWheels login modal")
    public void openLoginModal() {
        loginPage =
                new LoginPage(DriverManager.getDriver());

        loginPage.openLoginModal();
    }

    @When("I choose Google sign-in")
    public void chooseGoogleSignIn() {
        loginPage.chooseGoogleSignIn();
    }

    @When("I submit the invalid Google account {string}")
    public void submitInvalidGoogleAccount(String email) {
        loginPage.submitInvalidAccount(email);
    }

    @Then("a Google account validation error should be displayed")
    public void verifyValidationError() {
        validationError =
                loginPage.getValidationError();

        System.out.println(
                "Captured Google validation error: "
                        + validationError
        );

        Assert.assertFalse(
                validationError.isBlank(),
                "Google validation error should not be empty"
        );
    }
}