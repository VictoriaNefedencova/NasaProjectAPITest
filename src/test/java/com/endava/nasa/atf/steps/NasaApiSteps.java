package com.endava.nasa.atf.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.NasaApi;
import org.example.NasaEarthImagery;

import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class NasaApiSteps extends NasaApi {

    private static final Logger log = Logger.getLogger(NasaApiSteps.class.getName());

    private String apiUrl;      // URL for the API request
    private String apiResponse; // API response
    private String imageUrl;    // URL of the image

    // Creating a nested proxy class for Earth API
    private final EarthImageryProxy earthImagery = new EarthImageryProxy();

    // Nested class to access protected methods of Earth API
    private static class EarthImageryProxy extends NasaEarthImagery {
        public String publicBuildUrl() {
            return buildUrl(); // Access to protected method
        }

        public String publicFetchData(String url) throws IOException {
            return fetchData(url); // Access to protected method
        }

        public String publicExtractUrlFromResponse(String response) {
            return extractUrlFromResponse(response); // Access to protected method
        }
    }

    // APOD API
    @Given("the current day's image is available on the APOD API")
    public void the_current_day_image_exists_on_apod_api() {
        apiUrl = this.buildUrl(); // Access via inheritance
        assertNotNull("URL for APOD API should not be null", apiUrl);
        log.info("APOD API URL generated successfully: " + apiUrl);
    }

    @When("I send a valid request to the APOD API on NASA's site")
    public void i_send_a_request_to_apod_api_on_nasa_site() {
        try {
            apiResponse = this.fetchData(apiUrl); // Access via inheritance
            assertNotNull("Response from APOD API should not be null", apiResponse);
            log.info("APOD API response received successfully.");
        } catch (IOException e) {
            log.severe("Error while making request to APOD API: " + e.getMessage());
            fail("Error while making request to APOD API: " + e.getMessage());
        }
    }

    @Then("the APOD API image is opened in the browser")
    public void the_image_is_opened_in_the_browser() {
        openImageInBrowser(apiResponse, "APOD");
    }

    // Earth API
    @Given("the current day's image is available on the Earth API")
    public void the_current_day_image_exists_on_earth_api() {
        apiUrl = earthImagery.publicBuildUrl(); // Access via public wrapper method
        assertNotNull("URL for Earth API should not be null", apiUrl);
        log.info("Earth API URL generated successfully: " + apiUrl);
    }

    @When("I send a valid request to the Earth API on NASA's site")
    public void i_send_a_request_to_earth_api_on_nasa_site() {
        try {
            apiResponse = earthImagery.publicFetchData(apiUrl); // Access via public wrapper method
            assertNotNull("Response from Earth API should not be null", apiResponse);
            log.info("Earth API response received successfully.");
        } catch (IOException e) {
            log.severe("Error while making request to Earth API: " + e.getMessage());
            fail("Error while making request to Earth API: " + e.getMessage());
        }
    }

    @Then("the Earth API image is opened in the browser")
    public void the_image_should_be_opened_in_the_browser() {
        openImageInBrowser(apiResponse, "Earth");
    }

    // Private method to open an image in the browser
    private void openImageInBrowser(String response, String apiType) {
        try {
            if ("APOD".equals(apiType)) {
                imageUrl = this.extractUrlFromResponse(response); // Access via inheritance
            } else if ("Earth".equals(apiType)) {
                imageUrl = earthImagery.publicExtractUrlFromResponse(response); // Access via public wrapper method
            }

            assertNotNull("Image URL should not be null", imageUrl);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(imageUrl));
                log.info("Image opened in browser: " + imageUrl);
            } else {
                log.severe("Desktop operations are not supported on this system.");
                fail("Desktop operations are not supported on this system.");
            }
        } catch (Exception e) {
            log.severe("Error while opening the image in browser: " + e.getMessage());
            fail("Error while opening the image in browser: " + e.getMessage());
        }
    }
}
