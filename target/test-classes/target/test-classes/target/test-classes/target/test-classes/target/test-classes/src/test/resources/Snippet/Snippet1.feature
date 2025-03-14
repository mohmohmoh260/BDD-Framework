Feature: Snippet Demo Feature


  Scenario Outline: testing
    Then print "<data>" and "<data2>"
    Then print "<data3>" and "<data4>"
    Examples:
      |data|data2|data3|data4|
      |123 |abc  |aaaa |bbbb |
      |456 |def  | cccc|dddd |

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
    And take screenshot
    Examples:
    |username|password|securityPhrase|
    |autouat1|Maybank@1|automation ONLY?|

