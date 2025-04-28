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

  @RunUCO
  Scenario Outline: Login to UCO and test BRN/ID search function
    Given launch "chrome" browser and navigate to "UCO_Checker"
    Then set text "<username>" into element "UCO_Username_Input"
    Then set text "<password>" into element "UCO_Password_Input"
    And click element "UCO_Login_Button"
    Then run snippet scenario "To Test Search function and verify the user"
    Then click element "UCO_Yes_Button"
    Then verify text "ALLY PARK" is visible within 30 seconds
    Examples:
    | username | password |
    | C0101001 | Maybank1234|














