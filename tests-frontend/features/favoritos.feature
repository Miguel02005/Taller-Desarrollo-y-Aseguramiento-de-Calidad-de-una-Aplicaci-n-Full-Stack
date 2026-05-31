Feature: Favorites Management
  As a Smart Book Finder user
  I want to save books to my favorites
  So that I can access them easily later

  Background:
    Given I am on the Smart Book Finder home page

  Scenario: Add a book to favorites from the main grid
    When I click the heart button of the first book
    Then I should see the message "Agregado a favoritos"
    And the favorites counter in the header should be "1"

  Scenario: Open the favorites panel
    Given I have at least one book in favorites
    When I click the favorites button in the header
    Then I should see the panel "Mis Favoritos"

  Scenario: Empty panel when there are no favorites
    When I click the favorites button in the header
    Then I should see the panel "Mis Favoritos"
    And I should see the message "Sin favoritos"

  Scenario: Remove a book from the favorites panel
    Given I have at least one book in favorites
    And the favorites panel is open
    When I click the remove button of the first favorite
    Then the book should disappear from the favorites list

  Scenario: Close the favorites panel
    When I click the favorites button in the header
    And I click the close button of the panel
    Then the favorites panel should be closed