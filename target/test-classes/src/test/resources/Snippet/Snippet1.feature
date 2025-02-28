
Feature: Snippet Demo Feature

  Scenario: Verify Statistics section in web browser 1
    Given I launch the browser and navigate to Google page with "firefox"



  Scenario: Verify Statistics section in web browser
    Given I launch the browser and navigate to Google page with "chrome"
    When send key text "Heyyyyyyyyyyyyyyy" into the google search bar and press enter
    Then assert page title is "covid-19 stats malaysia - Google Search"
    Then assert statistics section is displayed





