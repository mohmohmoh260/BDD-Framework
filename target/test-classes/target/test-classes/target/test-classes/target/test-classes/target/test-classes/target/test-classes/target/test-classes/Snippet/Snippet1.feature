Feature: Snippet Demo Feature

  Scenario Outline: User login to M2U
    Then set text "<username>" into "Username_Input"
    And click "Button_Login"
    And wait for element "SecurityPhrase" to be visible within 30 seconds
    And get text from "SecurityPhrase" and set into variable "security"
    Then verify text "<securityPhrase>" is equals to variable "security"
    And take screenshot
    When click "Yes_btn"
    And set text "<password>" into "Password_input"
    And click "Button_Login"
    Examples:
    |username|password|securityPhrase|
    |autouat1   | Maybank@1    | Automation Only ?       |

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





