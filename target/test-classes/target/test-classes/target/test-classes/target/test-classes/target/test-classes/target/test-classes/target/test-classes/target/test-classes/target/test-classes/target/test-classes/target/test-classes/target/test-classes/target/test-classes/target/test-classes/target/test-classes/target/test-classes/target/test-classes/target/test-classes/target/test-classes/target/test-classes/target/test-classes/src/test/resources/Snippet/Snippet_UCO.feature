Feature: UCO

  Scenario: To Test Search function and verify the user
    When click element "UCO_SearchCustomer_Button"
    Then set text "IP0277344X" into element "UCO_IDNumber_Input"
    And click element "UCO_Search_Button"