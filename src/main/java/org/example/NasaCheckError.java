package org.example;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpPost;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;

abstract class NasaCheckError extends NasaApiBase {

    @Test
    public void testErrorHandling() {
        System.out.println("NasaCheckError test executed.");
        Assert.assertTrue(true);
    }

    @Override
    protected String buildUrl() {
        // Полный URL для API-запроса, используя глобальные переменные
        return BASE_URL + "/planetary/apod?api_key=" + API_KEY;
    }

    @Override
    protected String extractUrlFromResponse(String response) {
        // метод переопределяется, но не используется
        return null;  //  null, так как метод не нужен для данной задачи
    }

    public static void check405Error() throws IOException {
        String fullUrl = BASE_URL + "/planetary/apod?api_key=" + API_KEY;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(fullUrl);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                if (statusCode == 405) {
                    System.out.println("" +
                            "-----------------------------------------------------------" +
                            "Ошибка 405: Метод POST не поддерживается для этого ресурса" +
                            "-----------------------------------------------------------.");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        check405Error();
    }
}
