@login
Feature: Validate an invalid Google login

  As a ZigWheels visitor
  I want to see validation for an invalid Google account
  So that incorrect account details are handled safely

  Scenario: Capture the error for an invalid Google account
    Given I open the ZigWheels login modal
    When I choose Google sign-in
    And I submit the invalid Google account "invalid.automation.account.example@gmail.com"
    Then a Google account validation error should be displayed