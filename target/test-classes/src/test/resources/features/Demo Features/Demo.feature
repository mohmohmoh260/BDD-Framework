@Regression
Feature: Demo Feature

  @Test2
  Scenario: Verify Statistics section in web browser
    Given I launch the browser and navigate to Google page with "firefox"
    When send key text "covid-19 stats malaysia" into the google search bar and press enter
    Then assert page title is "covid-19 stats malaysia - Google Search"
    Then assert statistics section is displayed

  @Test2
  Scenario: Verify Statistics section in web browser
    Given I launch the browser and navigate to Google page with "chrome"
    When send key text "Heyyyyyyyyyyyyyyy" into the google search bar and press enter
    Then assert page title is "covid-19 stats malaysia - Google Search"
    Then assert statistics section is displayed





