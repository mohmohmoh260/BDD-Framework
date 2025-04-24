Feature: BIZ Snippet

  Scenario Outline: User login to BIZ
    When if "BIZ_Login" is visible within 30 seconds
    And take screenshot
    And click element "BIZ_Login"
    And end statement
    And set text "<username>" into "BIZ_Username_Input"
    And click element "BIZ_Next_Button"
#    Then verify actual text of "BIZ_Security Phrase_Text" is equals to expected text "<security>"
#    And click "BIZ_Yes_Button"
    Examples:
      | username | password | security |
      | hrmaker1 | Mbbtest@1| hrmaker1 |