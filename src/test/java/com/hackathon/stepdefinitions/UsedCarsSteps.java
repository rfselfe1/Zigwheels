package com.hackathon.stepdefinitions;

import java.util.HashSet;
import java.util.List;

import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.pages.UsedCarsPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UsedCarsSteps {

    private UsedCarsPage usedCarsPage;
    private List<String> popularModels;

    @Given("I am on the used cars page for {string}")
    public void openUsedCarsPage(String city) {
        usedCarsPage =
                new UsedCarsPage(DriverManager.getDriver());

        usedCarsPage.openForCity(city);
    }

    @When("I collect the popular used-car models")
    public void collectPopularModels() {
        popularModels = usedCarsPage.getPopularModels();

        System.out.println(
                "Popular used-car models collected: "
                        + popularModels.size()
        );

        popularModels.forEach(
                model -> System.out.println(
                        "Popular model: " + model
                )
        );
    }

    @Then("at least one popular model should be displayed")
    public void verifyPopularModelsExist() {
        Assert.assertFalse(
                popularModels.isEmpty(),
                "No popular used-car models were found"
        );
    }

    @Then("the popular model list should not contain duplicates")
    public void verifyNoDuplicateModels() {
        int originalSize = popularModels.size();
        int uniqueSize =
                new HashSet<>(popularModels).size();

        Assert.assertEquals(
                uniqueSize,
                originalSize,
                "The popular model list contains duplicates"
        );
    }
}