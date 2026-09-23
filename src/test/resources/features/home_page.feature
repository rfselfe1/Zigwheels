@smoke
Feature: ZigWheels home page

  As a website visitor
  I want to open the ZigWheels website
  So that I know it is available

  Scenario: Open the ZigWheels home page
    Given I open the ZigWheels website
    Then the page title should not be empty
    And the current URL should contain "zigwheels.com"