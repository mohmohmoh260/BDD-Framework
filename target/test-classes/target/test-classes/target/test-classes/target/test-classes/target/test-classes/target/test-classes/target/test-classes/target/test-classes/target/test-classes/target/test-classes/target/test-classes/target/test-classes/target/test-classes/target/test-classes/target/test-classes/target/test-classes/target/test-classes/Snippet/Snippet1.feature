
Feature: Snippet Demo Feature

  Scenario: Verify Statistics section in web browser 1
   Then print string "abc" 123 true


  Scenario: Verify Statistics section in web browser
    Given I launch the browser and navigate to Google page with "chrome"
    When send key text "Heyyyyyyyyyyyyyyy" into the google search bar and press enter
    Then assert page title is "covid-19 stats malaysia - Google Search"
    Then assert statistics section is displayed
    Then print from data table without header below
      |"123"|"abc"|
      |"456"|"def"|
    Then print from data table without header below
      |"111"|"zzz"|
      |"444"|"fff"|
    Then print from data table with header below
      |"name"|"age"|
      | "Ali"|12   |
      | "Abu"|14   |





