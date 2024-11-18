package org.example;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.net.URI;
import java.util.logging.Logger;

public class NasaApi extends NasaApiBase {

    private static final Logger log = Logger.getLogger(NasaApi.class.getName());
    private static final String APOD_ENDPOINT = "/planetary/apod?api_key=";

    @Override
    protected String buildUrl() {
        return BASE_URL + APOD_ENDPOINT + API_KEY + "&date=" + DATE;
    }

    @Override
    protected String extractUrlFromResponse(String response) {
        try {
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response);
            // Extract the 'url' field from the JSON object
            return jsonResponse.optString("url");
        } catch (JSONException e) {
            log.severe("Error parsing JSON response: " + e.getMessage());
            return null;
        }
    }

    public void openImageInBrowser() {
        try {
            String apiUrl = buildUrl();
            // Call the inherited fetchData method from NasaApiBase
            String response = fetchData(apiUrl);

            // Extract the image URL from the response
            String imageUrl = extractUrlFromResponse(response);

            if (imageUrl != null) {
                log.info("Image URL fetched: " + imageUrl);

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(imageUrl));
                    log.info("Image opened in browser: " + imageUrl);
                } else {
                    log.warning("Desktop browsing is not supported on this system.");
                }
            } else {
                log.warning("No URL found in the API response.");
            }
        } catch (Exception e) {
            log.severe("Error opening image: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        NasaApi api = new NasaApi();
        api.openImageInBrowser();
    }
}
