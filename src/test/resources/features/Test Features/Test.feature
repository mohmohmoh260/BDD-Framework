@Test1
Feature: Using IOS 1

  Background: User to Open Safari Browser IOS Device "iOS Device 1"
    Given I launch the Mobile Simulator "iOS Device 1"

  Scenario: User To Test Search Bar Function
    Given I launch the browser and navigate to Google page with "chrome"
    Then take screenshot

  Scenario: Print Device Driver 1
    Then print driver instance ID

  Scenario: Print Device Driver 2
    Then print driver instance ID
