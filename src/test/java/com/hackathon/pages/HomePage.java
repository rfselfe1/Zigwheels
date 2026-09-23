package com.hackathon.pages;

import org.openqa.selenium.WebDriver;

public class HomePage {

    private final WebDriver driver;
    private static final String HOME_PAGE_URL =
            "https://www.zigwheels.com/";

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(HOME_PAGE_URL);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}