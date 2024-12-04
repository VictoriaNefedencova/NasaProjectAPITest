Feature: NASA API
  As a guest
  I want to check various NASA APIs

  Scenario: Successfully open the current day's image via APOD API
    Given the current day's image is available on the APOD API
    When I send a valid request to the APOD API on NASA's site
    Then the APOD API image is opened in the browser

  Scenario: Successfully open the current day's image via Earth API
    Given the current day's image is available on the Earth API
    When I send a valid request to the Earth API on NASA's site
    Then the Earth API image is opened in the browser

  Scenario: Successfully check the 405 answer for POST request to NASA Earth API
    Given The Earth API only accepts GET parameters on the NASA API page
    When I send a valid POST request to the Earth API on NASA's site
    Then the Earth API response contains a 405 error

  Scenario: Validate APOD and Earth URLs on NASA API page
    Given I navigate to the NASA API homepage
    When I search for "APOD"
    Then I should see the APOD URL displayed and it matches the built URL
    When I search for "EARTH"
    Then I should see the Earth URL displayed and it matches the built URL

