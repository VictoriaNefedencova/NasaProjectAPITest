package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class NasaEarthImagery extends NasaApiBase {

    private static final Logger log = Logger.getLogger(NasaEarthImagery.class.getName());

    @Test
    public void testImageryProcessing() {
        log.info("Executing NasaEarthImagery test...");
        Assert.assertTrue(true);
    }

    @Override
    protected String buildUrl() {
        // Use Constants for API endpoint and other parameters
        return BASE_URL + Constants.EARTH_API_ENDPOINT + "?lon=" + LON + "&lat=" + LAT + "&date=" + DATE
                + "&dim=" + DIM + "&api_key=" + API_KEY;
    }

    @Override
    protected String extractUrlFromResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Parse response into NasaAnswerEarth object
            NasaAnswerEarth nasaAnswer = mapper.readValue(response, NasaAnswerEarth.class);
            return nasaAnswer.url; // Return URL from the response
        } catch (IOException e) {
            log.severe("Error parsing JSON response: " + e.getMessage());
            return null;
        }
    }

    // Method to fetch and open image in a browser
    public void fetchAndOpenImage() {
        try {
            String url = buildUrl();
            log.info("Fetching data from URL: " + url);

            String response = fetchData(url);

            String imageUrl = extractUrlFromResponse(response);

            if (imageUrl != null) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(imageUrl));
                    log.info("Image opened in browser: " + imageUrl);
                } else {
                    log.warning("Desktop browsing is not supported on this system.");
                }
            } else {
                log.warning("Image URL not found in the response.");
            }
        } catch (IOException e) {
            log.severe("Error fetching data: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Error opening image in browser: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        log.info("Starting NasaEarthImagery application...");
        NasaEarthImagery imagery = new NasaEarthImagery();
        imagery.fetchAndOpenImage();
        log.info("Application finished execution.");
    }
}
