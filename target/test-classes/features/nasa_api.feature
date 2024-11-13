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
