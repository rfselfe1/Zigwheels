@cars
Feature: Display popular used-car models in Chennai

  As a used-car buyer
  I want to see popular models available in Chennai
  So that I can identify commonly available cars

  Scenario: Extract the popular used-car models in Chennai
    Given I am on the used cars page for "Chennai"
    When I collect the popular used-car models
    Then at least one popular model should be displayed
    And the popular model list should not contain duplicates
    
    