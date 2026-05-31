Feature: General Navigation
  As a Smart Book Finder user
  I want to navigate the interface
  So that I have a good user experience

  Background:
    Given I am on the Smart Book Finder home page

  Scenario: See the application title in the header
    Then I should see the application title in the header

  Scenario: See the search form fields
    Then I should see the form field "Título"
    And I should see the form field "Autor"
    And I should see the language selector
    And I should see the minimum year field

  Scenario: Collapse and expand the search panel
    When I click the search panel header
    Then the search form should be hidden
    When I click the search panel header
    Then the search form should be visible

  Scenario: See the footer with copyright
    Then I should see the footer with the text "Smart Book Finder"