@accessibility
Feature: ZigWheels accessibility audit

  As a test automation engineer
  I want to inspect the ZigWheels home page for accessibility issues
  So that accessibility findings can be recorded and reviewed

  Scenario: Audit the ZigWheels home page
    Given I open the ZigWheels home page for accessibility testing
    When I run the accessibility audit
    Then the accessibility audit should produce valid results
    And the accessibility findings should be displayed