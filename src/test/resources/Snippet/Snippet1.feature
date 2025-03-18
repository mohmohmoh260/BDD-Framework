Feature: Snippet Demo Feature

  Scenario Outline: testing
    Then print "<data>" and "<data2>"
    Then print "<data3>" and "<data4>"
    Examples:
      |data  |data2 |data3  |data4 |
      |123   |abc   |aaaa   |bbbb  |
      |456   |def   |cccc   |dddd  |

  Scenario Outline: User login to M2U
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
    | username   |      password|     securityPhrase        |
    |autouat1     |    Maybank@1|      automation ONLY?     |

  Scenario Outline: testing
    Then print "<data>" and "<data2>"
    Then print "<data3>" and "<data4>"
    Examples:
      |data  |data2 |data3  |data4 |
      |123   |abc   |aaaa   |bbbb  |
      |456   |def   |cccc   |dddd  |

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
      | Subject Title   |  Body Text    |
      |  samuat04   |    Maybank@1|

  Scenario Outline: User login to BIZ
    When if "BIZ_Login to M2U Biz_Button" is visible within 30 seconds
      And take screenshot
      And click "BIZ_Login to M2U Biz_Button"
    And end statement
    And set text "<username>" into "BIZ_Username_Input"
    And click "BIZ_Next_Button"
    Then verify actual text of "BIZ_Security Phrase_Text" is equals to expected text "<security>"
    And click "BIZ_Yes_Button"
    Examples:
    | username | password | security |
    | hrmaker1 | Mbbtest@1| hrmaker1 |
