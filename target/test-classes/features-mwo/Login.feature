@BottomNavigation
Feature: Bottom Navigation Menu
  As a user
  I want to be able to traverse to different screens from the Bottom Navigation menu 
  So that i can effectively use the various features of the apps

  @BottomNavigation-Normal
  Scenario: Validate Bottom Navigation Menu
  	Given the user is on home page
    And he sees the bottom navigation menu
    Then he sees the Home Menu
    And he sees the Extras Menu
    And he clicks on Extras Menu to navigate to the Extras page
    Then he should be taken to the Extras page

