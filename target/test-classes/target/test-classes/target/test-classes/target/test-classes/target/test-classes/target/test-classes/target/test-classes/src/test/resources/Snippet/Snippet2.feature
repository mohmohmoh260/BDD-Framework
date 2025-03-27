Feature: Snippet Demo Feature

  Scenario Outline: User login to M2U nnn
    Then set text "<username>" into "Username_Input"
    And click "Button_Login"
    And get text from "SecurityPhrase" and set into variable "security"
    Then verify text "<securityPhrase>" is equals to variable "security"
    And take screenshot
    When click "Yes_btn"
    And set text "<password>" into "Password_input"
    And click "Button_Login"
    And take screenshot
    Examples:
    | username   |      password|     securityPhrase       |
    |autouat1    |    Maybank@1|      automation ONLY?     |
