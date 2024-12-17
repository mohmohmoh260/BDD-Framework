@Regression
Feature: Demo Feature

  @Test2
  Scenario: Verify Statistics section in web browser
    Given launch the browser and navigate to Google page
    When send key text "covid-19 stats malaysia" into the google search bar and press enter
    Then assert page title is "covid-19 stats malaysia - Google Search"
    Then assert statistics section is displayed

  @Test2
  Scenario: Verify Statistics section in web browser
    Given launch the browser and navigate to Google page
    When send key text "Heyyyyyyyyyyyyyyy" into the google search bar and press enter
    Then assert page title is "covid-19 stats malaysia - Google Search"
    Then assert statistics section is displayed

#    @Test1
#  Scenario: To Test IOS Simulator with Page Factory Approach
#    Given launch the iOS Simulator
#    Then click Allow Access

  @Test1
  Scenario: To Test Android Simulator with Page Factory Approach
    Given launch the Android Simulator "Android Device 1"

  @Test6
  Scenario: To Test Android Simulator with Page Factory Approach
    Given launch the Android Simulator "Android Device 2"
#    Then click Allow Access
#
#  @Test1
#  Scenario Outline: Verify notepad in mobile is able to write
#    Given launch the mobile apps and open notepad
#    And create new note
#    And type "<Note Title>" note title
#    And type "<Note Body>" note body
#    And save the note
#    When click back button
#    Then assert note "<Note Title>" is displayed
#    And Quit
#    Examples:
#    |Note Title  | Note Body                           |
#    |Hello World!| Don't forget to be happy everyday :)|
#
#  @Test1
#  Scenario: Verify notepad in mobile is able to write
#    Given launch the mobile apps and open notepad
#    And create new note
#    And type note title
#    |Selamat|Hari|Raya|
#    And type note body
#    |First Word|Second Word|
#    |Gong Xi   | Fa Cai    |
#    And save the note
#    When click back button
#    And Quit
#
#
#  @Test1
#  Scenario: Verify Browser & App Login
#    Given launch the browser and navigate to Google page
#    When send key text "covid-19 stats malaysia" into the google search bar and press enter
#    Then assert page title is "covid-19 stats malaysia - Google Search"
#    Then assert statistics section is displayed
#    Given launch the mobile apps and open notepad
#    And create new note
#    And type note title
#      |Selamat|Hari|Raya|
#    And type note body
#      |First Word|Second Word|
#      |Gong Xi   | Fa Cai    |
#    And save the note
#    When click back button
#    And Quit




