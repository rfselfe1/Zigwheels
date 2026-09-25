package com.hackathon.stepdefinitions;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.hackathon.driver.DriverManager;
import com.hackathon.pages.AccessibilityPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccessibilitySteps {

    private static final Logger logger =
            LogManager.getLogger(
                    AccessibilitySteps.class
            );

    private AccessibilityPage accessibilityPage;
    private Results auditResults;
    private List<Rule> violations;

    @Given("I open the ZigWheels home page for accessibility testing")
    public void openZigWheelsForAccessibilityTesting() {

        accessibilityPage =
                new AccessibilityPage(
                        DriverManager.getDriver()
                );

        accessibilityPage.open();
    }

    @When("I run the accessibility audit")
    public void runAccessibilityAudit() {

        auditResults =
                accessibilityPage.runAudit();

        violations =
                auditResults.getViolations();
    }

    @Then("the accessibility audit should produce valid results")
    public void verifyAuditResults() {

        Assert.assertNotNull(
                auditResults,
                "The accessibility audit returned no results"
        );

        Assert.assertNotNull(
                violations,
                "The accessibility violation list was null"
        );

        logger.info(
                "Accessibility audit returned valid results"
        );
    }

    @Then("the accessibility findings should be displayed")
    public void displayAccessibilityFindings() {

        logger.info(
                "Accessibility violations found: {}",
                violations.size()
        );

        if (violations.isEmpty()) {

            logger.info(
                    "No automated accessibility violations were detected"
            );

            return;
        }

        for (Rule violation : violations) {

            logger.warn(
                    "Accessibility finding"
                            + " | rule={}"
                            + " | impact={}"
                            + " | description={}"
                            + " | affectedNodes={}"
                            + " | helpUrl={}",
                    violation.getId(),
                    violation.getImpact(),
                    violation.getDescription(),
                    violation.getNodes().size(),
                    violation.getHelpUrl()
            );
        }

        logger.info(
                "Accessibility findings were recorded for review; "
                        + "they do not automatically fail this third-party-site audit"
        );
    }
}