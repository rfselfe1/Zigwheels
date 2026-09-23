package com.hackathon.hooks;

import com.hackathon.driver.DriverManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before
    public void setUp() {
        DriverManager.createDriver();
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
}