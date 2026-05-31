Feature: Book Search
  As a Smart Book Finder user
  I want to search for books by title or author
  So that I can find books that interest me

  Background:
    Given I am on the Smart Book Finder home page

  Scenario: See recommended books on page load
    Then I should see the section "Libros Recomendados"
    And I should see at least one book in the grid

  Scenario: Search for a book by title
    When I type "Harry Potter" in the title field
    And I click the "Buscar" button
    Then I should see the section "Resultados"
    And I should see at least one book in the grid

  Scenario: Search for a book by author
    When I type "Garcia Marquez" in the author field
    And I click the "Buscar" button
    Then I should see the section "Resultados"
    And I should see at least one book in the grid

  Scenario: Show error when searching without criteria
    When I click the "Buscar" button
    Then I should see the error message "Ingresa al menos título o autor"

  Scenario: Clear the search form
    When I type "Harry Potter" in the title field
    And I click the "Limpiar" button
    Then the title field should be empty