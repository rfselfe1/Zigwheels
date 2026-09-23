package com.hackathon.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BrowserTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void verifyZigWheelsHomePageOpens() {
        driver.get("https://www.zigwheels.com");

        String title = driver.getTitle();
        String currentUrl = driver.getCurrentUrl();

        System.out.println("Page title: " + title);
        System.out.println("Current URL: " + currentUrl);

        Assert.assertFalse(title.isBlank(),
                "The page title should not be empty");

        Assert.assertTrue(currentUrl.contains("zigwheels.com"),
                "The browser should remain on the ZigWheels website");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}