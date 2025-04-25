Feature: MAE Snippet

  Scenario Outline: User login to MAE sample
    Then click element "Agree_button"
    When if element "Proceed Anyway_Button" is visible within 10 seconds
    And click element "Proceed Anyway_Button"
    When if element "Proceed Anyway_Button" is visible within 10 seconds
    And click element "Agree & Proceed_button"
    And end if statement
    And end if statement
    And click element "Next_Button"
    And click element "Next_Button"
    And click element "Next_Button"
    And click element "Next_Button"
    And click element "Log In Here_button"

    Examples:
      | Subject Title   |  Body Text|
      |  samuat04   |    Maybank@1|
