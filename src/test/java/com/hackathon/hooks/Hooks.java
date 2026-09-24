package com.hackathon.hooks;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.hackathon.driver.DriverManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void setUp() {
        DriverManager.createDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
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

                System.out.println(
                        "Screenshot attached for failed scenario: "
                                + scenario.getName()
                );
            }
        } finally {
            DriverManager.quitDriver();
        }
    }
}
