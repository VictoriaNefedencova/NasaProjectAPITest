package org.example;

import java.io.IOException;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

abstract class NasaCheckError extends NasaApiBase {
    private static final Logger log = Logger.getLogger(NasaCheckError.class.getName());

    NasaCheckError() {
    }

    @Test
    public void testErrorHandling() {
        log.info("Executing NasaCheckError test...");
        Assert.assertTrue(true);
    }

    protected String buildUrl() {
        return BASE_URL + "/planetary/apod?api_key=" + API_KEY;
    }

    protected String extractUrlFromResponse(String response) {
        return null;
    }

    public static void check405Error() throws IOException {
        String fullUrl = BASE_URL + "/planetary/apod?api_key=" + API_KEY;
        log.info("Checking for HTTP 405 error using URL: " + fullUrl);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            try {
                HttpPost httpPost = new HttpPost(fullUrl);
                CloseableHttpResponse response = httpClient.execute(httpPost);

                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                        log.warning("-----------------------------------------------------------\nError 405: POST method is not allowed for this resource.\n-----------------------------------------------------------");
                    } else {
                        log.info("Response received with status code: " + statusCode);
                    }
                } catch (Throwable var8) {
                    if (response != null) {
                        try {
                            response.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }

                    throw var8;
                }

                if (response != null) {
                    response.close();
                }
            } catch (Throwable var9) {
                if (httpClient != null) {
                    try {
                        httpClient.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                }

                throw var9;
            }

            if (httpClient != null) {
                httpClient.close();
            }

        } catch (IOException var10) {
            IOException e = var10;
            log.severe("Error occurred while checking 405 error: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) throws IOException {
        log.info("Starting the NasaCheckError application...");
        check405Error();
        log.info("Application finished execution.");
    }
}

