package com.hackathon.stepdefinitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.hackathon.driver.DriverManager;
import com.hackathon.pages.PassiveSecurityPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PassiveSecuritySteps {

    private static final Logger logger =
            LogManager.getLogger(
                    PassiveSecuritySteps.class
            );

    private PassiveSecurityPage securityPage;

    private boolean httpsEnabled;
    private int visibleCookieCount;
    private long cookiesWithoutSecureFlag;
    private long cookiesWithoutHttpOnlyFlag;
    private long insecureResourceReferences;
    private long insecureFormActions;
    private boolean inspectionCompleted;

    @Given("I open ZigWheels for passive security testing")
    public void openZigWheelsForPassiveSecurityTesting() {

        securityPage =
                new PassiveSecurityPage(
                        DriverManager.getDriver()
                );

        securityPage.open();
    }

    @When("I inspect the browser-visible security controls")
    public void inspectBrowserVisibleSecurityControls() {

        httpsEnabled =
                securityPage.isUsingHttps();

        visibleCookieCount =
                securityPage.getVisibleCookies()
                        .size();

        cookiesWithoutSecureFlag =
                securityPage
                        .countCookiesWithoutSecureFlag();

        cookiesWithoutHttpOnlyFlag =
                securityPage
                        .countCookiesWithoutHttpOnlyFlag();

        insecureResourceReferences =
                securityPage
                        .countInsecureResourceReferences();

        insecureFormActions =
                securityPage
                        .countInsecureFormActions();

        inspectionCompleted = true;
    }

    @Then("the ZigWheels page should use HTTPS")
    public void verifyHttps() {

        Assert.assertTrue(
                inspectionCompleted,
                "Passive security inspection did not complete"
        );

        Assert.assertTrue(
                httpsEnabled,
                "ZigWheels should use HTTPS"
        );

        logger.info(
                "Verified that ZigWheels uses HTTPS"
        );
    }

    @Then("the passive security observations should be displayed")
    public void displayPassiveSecurityObservations() {

        logger.info(
                "Passive security observations"
                        + " | visibleCookies={}"
                        + " | cookiesWithoutSecure={}"
                        + " | cookiesWithoutHttpOnly={}"
                        + " | insecureResourceReferences={}"
                        + " | insecureFormActions={}",
                visibleCookieCount,
                cookiesWithoutSecureFlag,
                cookiesWithoutHttpOnlyFlag,
                insecureResourceReferences,
                insecureFormActions
        );

        if (insecureResourceReferences > 0) {

            logger.warn(
                    "The loaded DOM contains {} HTTP resource references",
                    insecureResourceReferences
            );
        }

        if (insecureFormActions > 0) {

            logger.warn(
                    "The loaded DOM contains {} HTTP form actions",
                    insecureFormActions
            );
        }

        logger.info(
                "Observations were collected passively from the normal browser session"
        );
    }
}
