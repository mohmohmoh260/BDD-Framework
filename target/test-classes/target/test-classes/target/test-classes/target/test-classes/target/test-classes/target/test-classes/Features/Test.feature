
Feature: Reuse Scenarios


 # Background: User to Open Safari Browser IOS Device "iOS Device 1"
  #  Given I launch the Mobile Simulator "iOS Device 1"

  Scenario: User To Test Search Bar Function
    Given I launch the browser and navigate to Google page with "chrome"
    Then take screenshot

  Scenario: Print Device Driver 1
    Then print driver instance ID
    When if 3 is bigger than 2
    Then print driver instance ID
    When if 2 is bigger than 3
    Then print driver instance ID
    Then print driver instance ID
    And end statement
    And end statement
    When print driver instance ID


@test
  Scenario: Print Device Driver 2
  When run snippet scenario "Verify Statistics section in web browser 1"
  Then print driver instance ID








