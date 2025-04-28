Feature: Snippet Demo Feature

  Scenario Outline: User login to M2U
    Then set text "<username>" into element "M2U_Username_Input"
    And click element "M2U_Button_Login"
    And get text from element "M2U_SecurityPhrase" and set into variable "security"
    Then verify text "<securityPhrase>" is equals to variable "security"
    When click element "M2U_Yes_btn"
    And set text "<password>" into element "M2U_Password_input"
    And click element "M2U_Button_Login"
    Examples:
    | username   |      password|     securityPhrase        |
    |autouat1     |    Maybank@1|      automation ONLY??     |

Scenario: Navigate to Transfer Page
  And click text "PAY & TRANSFER"
  And run snippet scenario "test"
  And if text "x" is visible within 20 seconds
  And click text "x"
  And end if statement

Scenario Outline: Perform Own Transfer
  And click text "TRANSFER"
  And click element "M2U_TransferFrom_Dropdown"
  And click text "<Account From>"
  And click text "<Account To>"
  Examples:
  |Account From|Account To| Amount | Status |
  |Savings Account-i|1122212122    |1.00  |Accept |

  Scenario: test
    Then print "abc"
    Then run snippet scenario "123"

  Scenario: 123
    Then print "123"

