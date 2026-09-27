@security
Feature: Passive ZigWheels security observations

  As a test automation engineer
  I want to record passive security observations
  So that browser-visible security controls can be reviewed safely

  Scenario: Inspect browser-visible security controls
    Given I open ZigWheels for passive security testing
    When I inspect the browser-visible security controls
    Then the ZigWheels page should use HTTPS
    And the passive security observations should be displayed