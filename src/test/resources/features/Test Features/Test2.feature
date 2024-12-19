@Test2
Feature: Using IOS 1

  Background: User to Open Safari Browser IOS Device "iOS Device 1"
    Given I launch the Android Simulator "iOS Device 1"

  Scenario: User To Test Search Bar Function
    When I type "covid" into searchbar