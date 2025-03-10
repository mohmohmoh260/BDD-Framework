Feature: Snippet Demo Feature

  @test
  Scenario: Navigate to Practice Automation Website and Login
    Given I navigate browser to "url1"
    When I set text "abc" into "GooglePage_Username_Input"
    | username |
    | student  |
    When I set text {string} into {string}
    And take screenshot

  Scenario: Verify Snippet Code Is Running
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





