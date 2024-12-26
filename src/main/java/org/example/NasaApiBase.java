package org.example;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import java.io.IOException;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;




public abstract class NasaApiBase {

    private static final Logger log = Logger.getLogger(NasaApiBase.class.getName());

    protected static final String BASE_URL = ConfigLoader.getString("base.url");
    protected static final String API_KEY = ConfigLoader.getString("api.key");
    protected static final String DATE = ConfigLoader.getCurrentDate(); // Fetch the current date dynamically
    protected static final double LAT = ConfigLoader.getDouble("latitude");
    protected static final double LON = ConfigLoader.getDouble("longitude");
    protected static final double DIM = ConfigLoader.getDouble("dimension");

    // Abstract method to build the API URL.
    protected abstract String buildUrl();

    // Abstract method to extract the URL from the response.
    protected abstract String extractUrlFromResponse(String response);

    /**
     * Method to fetch data from the URL using an HTTP GET request.
     *
     * @param url URL to fetch data from
     * @return The response data as a string
     * @throws IOException if there is a network error
     */
    public String fetchData(String url) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                // Проверяем успешность ответа с помощью HttpStatus
                if (!HttpStatus.valueOf(statusCode).is2xxSuccessful()) {
                    throw new IOException("HTTP error: code " + statusCode);
                }
                return new String(response.getEntity().getContent().readAllBytes());
            }
        }
    }
}
