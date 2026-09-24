package com.hackathon.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.hackathon.driver.DriverManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    private static final Logger logger =
            LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {

        logger.info(
                "Starting scenario: {}",
                scenario.getName()
        );

        DriverManager.createDriver();

        logger.info(
                "Browser created for scenario: {}",
                scenario.getName()
        );
    }

    @After
    public void tearDown(Scenario scenario) {

        try {

            if (scenario.isFailed()) {

                logger.error(
                        "Scenario failed: {}",
                        scenario.getName()
                );

                byte[] screenshot =
                        ((TakesScreenshot)
                                DriverManager.getDriver())
                                .getScreenshotAs(
                                        OutputType.BYTES
                                );

                scenario.attach(
                        screenshot,
                        "image/png",
                        "Failure screenshot"
                );

                logger.info(
                        "Failure screenshot attached: {}",
                        scenario.getName()
                );

            } else {

                logger.info(
                        "Scenario passed: {}",
                        scenario.getName()
                );
            }

        } catch (RuntimeException exception) {

            logger.error(
                    "Could not capture the failure screenshot for scenario: {}",
                    scenario.getName(),
                    exception
            );

        } finally {

            DriverManager.quitDriver();

            logger.info(
                    "Browser closed for scenario: {}",
                    scenario.getName()
            );
        }
    }
}