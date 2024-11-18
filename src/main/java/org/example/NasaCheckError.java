package org.example;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;
import java.util.logging.Logger;

abstract class NasaCheckError extends NasaApiBase {

    private static final Logger log = Logger.getLogger(NasaCheckError.class.getName());

    @Test
    public void testErrorHandling() {
        log.info("Executing NasaCheckError test...");
        Assert.assertTrue(true);  // Add meaningful assertions or remove if not needed
    }

    @Override
    protected String buildUrl() {
        // Ensure that BASE_URL, Constants.APOD_ENDPOINT, and API_KEY are set correctly
        return BASE_URL + Constants.APOD_ENDPOINT + "?api_key=" + API_KEY;
    }

    @Override
    protected String extractUrlFromResponse(String response) {
        // This method is overridden but not used in this context
        return null;  // Returning null as this method is not required for this task
    }

    public static void check405Error() throws IOException {
        // Construct the full URL using Constants
        String fullUrl = BASE_URL + Constants.APOD_ENDPOINT + "?api_key=" + API_KEY;

        log.info("Checking for HTTP 405 error using URL: " + fullUrl);

        // Create a POST request to the endpoint, which should trigger a 405 error
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(fullUrl);  // Using POST instead of GET

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // Ensure correct method for response code retrieval
                int statusCode = response.getCode();  // Get status code from response

                // Log and handle the status code
                if (statusCode == Constants.HTTP_METHOD_NOT_ALLOWED) {
                    log.warning("-----------------------------------------------------------\n" +
                            "Error 405: POST method is not allowed for this resource.\n" +
                            "-----------------------------------------------------------");
                } else {
                    log.info("Response received with status code: " + statusCode);
                }
            }
        } catch (IOException e) {
            log.severe("Error occurred while checking 405 error: " + e.getMessage());
            throw e;  // Rethrow exception after logging
        }
    }

    public static void main(String[] args) throws IOException {
        log.info("Starting the NasaCheckError application...");
        check405Error();  // Call the method to check for 405 error
        log.info("Application finished execution.");
    }
}
