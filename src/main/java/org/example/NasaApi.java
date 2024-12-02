package org.example;

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class NasaApi extends NasaApiBase {
    private static final Logger log = Logger.getLogger(NasaApi.class.getName());
    private static final String APOD_ENDPOINT = "/planetary/apod?api_key=";

    public NasaApi() {
    }

    public String buildUrl() {
        return BASE_URL + "/planetary/apod?api_key=" + API_KEY + "&date=" + DATE;
    }

    public String extractUrlFromResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.optString("url");
        } catch (JSONException jsonException) {
            log.severe("Error parsing JSON response: " + jsonException.getMessage());
            return null;
        }
    }

    public void openImageInBrowser() {
        try {
            String apiUrl = this.buildUrl();
            String response = this.fetchData(apiUrl);
            String imageUrl = this.extractUrlFromResponse(response);
            if (imageUrl != null) {
                log.severe("Image URL fetched: " + imageUrl);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(imageUrl));
                    log.severe("Image opened in browser: " + imageUrl);
                } else {
                    log.severe("Desktop browsing is not supported on this system.");
                }
            } else {
                log.severe("No URL found in the API response.");
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
