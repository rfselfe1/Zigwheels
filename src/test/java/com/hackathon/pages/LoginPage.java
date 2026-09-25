package com.hackathon.pages;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.StaleElementReferenceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPage {
	
	private static final Logger logger =
	        LogManager.getLogger(LoginPage.class);

    private static final String HOME_URL =
            "https://www.zigwheels.com/";

    private static final By LOGIN_BUTTON =
            By.id("des_lIcon");

    private static final By LOGIN_MODAL =
            By.id("myModal3");

    private static final By GOOGLE_SIGN_IN =
            By.cssSelector("#myModal3 .googleSignIn");

    private static final By GOOGLE_EMAIL =
            By.name("identifier");

    private static final By GOOGLE_NEXT =
            By.xpath(
                    "//span[normalize-space()='Next']"
                    + "/ancestor::*"
                    + "[self::button or @role='button'][1]"
            );

    private static final By GOOGLE_SPECIFIC_ERROR =
            By.cssSelector("[jsname='B34EJ']");

    private static final By KNOWN_GOOGLE_ERRORS =
            By.xpath(
                    "//*[self::div or self::span "
                    + "or self::h1 or self::h2]"
                    + "[contains(normalize-space(.), "
                    + "'find your Google Account') "
                    + "or contains(normalize-space(.), "
                    + "'browser or app may not be secure') "
                    + "or contains(normalize-space(.), "
                    + "'sign you in')]"
            );

    private static final By CONSENT_BUTTON =
            By.cssSelector("button.fc-cta-consent");

    private static final By CONSENT_OVERLAY =
            By.cssSelector(".fc-dialog-overlay");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }

    public void openLoginModal() {
        driver.get(HOME_URL);

        acceptConsentIfDisplayed();

        wait.until(
                ExpectedConditions
                        .elementToBeClickable(LOGIN_BUTTON)
        ).click();

        wait.until(
                ExpectedConditions
                        .visibilityOfElementLocated(LOGIN_MODAL)
        );
    }

    public void chooseGoogleSignIn() {
        Set<String> windowsBeforeClick =
                driver.getWindowHandles();

        wait.until(
                ExpectedConditions
                        .elementToBeClickable(GOOGLE_SIGN_IN)
        ).click();

        wait.until(
                ExpectedConditions.numberOfWindowsToBe(
                        windowsBeforeClick.size() + 1
                )
        );

        String googleWindow =
                driver.getWindowHandles()
                        .stream()
                        .filter(window ->
                                !windowsBeforeClick.contains(window))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Google window did not open"
                                )
                        );

        driver.switchTo().window(googleWindow);

        wait.until(
                ExpectedConditions
                        .visibilityOfElementLocated(GOOGLE_EMAIL)
        );
    }

    public void submitInvalidAccount(String email) {
        WebElement emailInput = wait.until(
                ExpectedConditions
                        .visibilityOfElementLocated(GOOGLE_EMAIL)
        );

        emailInput.clear();
        emailInput.sendKeys(email);

        wait.until(
                ExpectedConditions
                        .elementToBeClickable(GOOGLE_NEXT)
        ).click();
    }

    public String getValidationError() {

        return wait.until(currentDriver -> {

            try {

                String message =
                        getAriaDescribedError();

                if (isRecognizedGoogleError(message)) {
                    return message;
                }

                message =
                        getShortestRecognizedText(
                                GOOGLE_SPECIFIC_ERROR
                        );

                if (isRecognizedGoogleError(message)) {
                    return message;
                }

                message =
                        getShortestRecognizedText(
                                KNOWN_GOOGLE_ERRORS
                        );

                return isRecognizedGoogleError(message)
                        ? message
                        : null;

            } catch (StaleElementReferenceException exception) {

                logger.debug(
                        "Google refreshed the validation element; retrying"
                );

                return null;
            }
        });
    }

    private String getAriaDescribedError() {
        List<WebElement> emailInputs =
                driver.findElements(GOOGLE_EMAIL);

        if (emailInputs.isEmpty()) {
            return "";
        }

        WebElement emailInput = emailInputs.get(0);

        if (!"true".equals(
                emailInput.getAttribute("aria-invalid"))) {
            return "";
        }

        String describedBy =
                emailInput.getAttribute("aria-describedby");

        if (describedBy == null
                || describedBy.isBlank()) {
            return "";
        }

        for (String elementId :
                describedBy.trim().split("\\s+")) {

            String message = getShortestRecognizedText(
                    By.id(elementId)
            );

            if (isRecognizedGoogleError(message)) {
                return message;
            }
        }

        return "";
    }

    private String getShortestRecognizedText(
            By locator) {

        return driver.findElements(locator)
                .stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .map(String::trim)
                .filter(this::isRecognizedGoogleError)
                .min(Comparator.comparingInt(String::length))
                .orElse("");
    }

    private boolean isRecognizedGoogleError(
            String message) {

        if (message == null || message.isBlank()) {
            return false;
        }

        String normalized =
                message.toLowerCase(Locale.ROOT);

        return normalized.contains(
                "find your google account"
        )
        || normalized.contains(
                "sign you in"
        )
        || normalized.contains(
                "browser or app may not be secure"
        );
    }

    private void acceptConsentIfDisplayed() {

        WebDriverWait shortWait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(5)
                );

        try {

            shortWait.until(
                    ExpectedConditions
                            .elementToBeClickable(
                                    CONSENT_BUTTON
                            )
            ).click();

            wait.until(
                    ExpectedConditions
                            .invisibilityOfElementLocated(
                                    CONSENT_OVERLAY
                            )
            );

            logger.info(
                    "Consent dialog was accepted"
            );

        } catch (TimeoutException exception) {

            logger.debug(
                    "Consent dialog was not displayed"
            );
        }
    }
}