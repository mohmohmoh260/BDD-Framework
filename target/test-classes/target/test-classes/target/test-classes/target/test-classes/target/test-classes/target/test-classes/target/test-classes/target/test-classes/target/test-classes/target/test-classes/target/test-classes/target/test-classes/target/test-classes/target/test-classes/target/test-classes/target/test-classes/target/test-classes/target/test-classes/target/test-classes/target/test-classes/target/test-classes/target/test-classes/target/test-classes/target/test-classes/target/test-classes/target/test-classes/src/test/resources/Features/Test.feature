@regression
Feature: Reuse Scenarios

  #options for browser types are: - chrome - firefox - edge - chromium - ie - safari
  Background: User to Open Safari Browser IOS Device "iOS Device 1"
    #Given I launch "chrome" browser and navigate to "M2U_RSA"

   @test
  Scenario: Print Device Driver 2
    When run snippet scenario "User login to M2U"











