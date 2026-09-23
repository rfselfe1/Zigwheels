@bikes
Feature: Identify affordable upcoming Honda bikes

  As a potential bike buyer
  I want to view affordable upcoming Honda bikes
  So that I can compare future releases

  Scenario: Display upcoming Honda bikes below four lakh rupees
    Given I am on the upcoming bikes page
    When I collect upcoming "Honda" bikes priced below 400000 rupees
    Then at least one matching bike should be displayed
    And each matching bike should have a name, price and expected launch date
    And each matching bike should cost less than 400000 rupees
    