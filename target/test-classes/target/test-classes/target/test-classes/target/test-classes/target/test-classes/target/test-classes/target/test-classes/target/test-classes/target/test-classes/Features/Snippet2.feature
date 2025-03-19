Feature: dsfrff

Scenario Outline: User login to MAE 3
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