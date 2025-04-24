@regression
Feature: Reuse Scenarios

  #options for browser types are: - chrome - firefox - edge - chromium - ie - safari
  Background: User to Open Safari Browser IOS Device "iOS Device 1"
   # Given launch web remote browser and navigate to "M2U_RSA"

  @test
  Scenario: Print Device Driver 2
    Given launch "chrome" browser and navigate to "M2U_UIUX2"
    When run snippet scenario "User login to M2U"
    And run snippet scenario "Navigate to Transfer Page"
    And run snippet scenario "Perform Own Transfer"

  @test1
  Scenario: Print Device Driver 3
    Given launch the Mobile Simulator "Android Device 1"
    When run snippet scenario "User login to BIZ"

    @test3
    Scenario: test123
      And print "qwe"
      Then run snippet scenario "test"
      When if 1 is smaller than 2
      Then run snippet scenario "test"
      And end if statement













