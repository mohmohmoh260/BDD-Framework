
Feature: Snippet Demo Feature

  Scenario: Verify Statistics section in web browser 1
   Then print string "abc" 123.00 true


  Scenario: Verify Statistics section in web browser
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





