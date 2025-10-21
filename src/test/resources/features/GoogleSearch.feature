Feature: Google Search
  As a Google user
  I want to be able to perform searches
  So that I can find relevant information

  @GoogleSearch @Smoke
  Scenario: Perform a basic Google search
    Given the user is on the Google home page
    When the user searches for "Selenium WebDriver"
    Then search results are displayed
    And the page title contains the search term

  @GoogleSearch @Regression
  Scenario Outline: Perform multiple Google searches
    Given the user is on the Google home page
    When the user searches for "<search_term>"
    Then search results are displayed
    And the search term "<search_term>" appears in the search box

    Examples:
      | search_term             |
      | Cucumber BDD            |
      | TestNG Java             |
      | Page Object Model       |
      | Selenium Automation     |
