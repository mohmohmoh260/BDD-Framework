Feature: Snippet Demo Feature

  Scenario Outline: User login to M2U
    Then set text "<username>" into "M2U_Username_Input"
    And click "M2U_Button_Login"
    And get text from "M2U_SecurityPhrase" and set into variable "security"
    Then verify text "<securityPhrase>" is equals to variable "security"
    And take screenshot
    When click "M2U_Yes_btn"
    And set text "<password>" into "M2U_Password_input"
    And click "M2U_Button_Login"
    And take screenshot
    Examples:
    | username   |      password|     securityPhrase        |
    |autouat1     |    Maybank@1|      automation ONLY?     |

  Scenario Outline: User login to MAE
    Then click "Agree_button"
    When if "Proceed Anyway_Button" is visible within 10 seconds
      And click "Proceed Anyway_Button"
      When if "Proceed Anyway_Button" is visible within 10 seconds
        And click "Agree & Proceed_button"
      And end statement
    And end statement
    And click "Next_Button"
    And click "Next_Button"
    And click "Next_Button"
    And click "Next_Button"
    And click "Log In Here_button"
    Examples:
      | Subject Title   |  Body Text|
      |  samuat04   |    Maybank@1|


  Scenario Outline: User login to BIZ
    When if "BIZ_Login" is visible within 30 seconds
      And take screenshot
      And click "BIZ_Login"
    And end statement
    And set text "<username>" into "BIZ_Username_Input"
    And click "BIZ_Next_Button"
#    Then verify actual text of "BIZ_Security Phrase_Text" is equals to expected text "<security>"
#    And click "BIZ_Yes_Button"
    Examples:
    | username | password | security |
    | hrmaker1 | Mbbtest@1| hrmaker1 |


  Scenario Outline: User login to MAE sass
    Then click "Agree_button"
    When if "Proceed Anyway_Button" is visible within 10 seconds
    And click "Proceed Anyway_Button"
    When if "Proceed Anyway_Button" is visible within 10 seconds
    And click "Agree & Proceed_button"
    And end statement
    And end statement
    And click "Next_Button"
    And click "Next_Button"
    And click "Next_Button"
    And click "Next_Button"
    And click "Log In Here_button"
    Examples:
      | Subject Title   |  Body Text    |
      |  samuat04   |    Maybank@1|
