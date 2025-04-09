@regression
Feature: Reuse Scenarios

  #options for browser types are: - chrome - firefox - edge - chromium - ie - safari
  Background: User to Open Safari Browser IOS Device "iOS Device 1"
   # Given launch web remote browser and navigate to "M2U_RSA"


  @test
  Scenario: Print Device Driver 2
    #And run snippet scenario "testing"
    Given launch "chrome" browser and navigate to "M2U_RSA"
    When run snippet scenario "User login to M2U"

  @test
  Scenario: Print Device Driver 3
    Given launch the Mobile Simulator "Android Device 1"
    And print "def"
    When run snippet scenario "User login to BIZ"













