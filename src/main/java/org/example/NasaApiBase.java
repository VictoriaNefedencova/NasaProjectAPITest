
package org.example;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;

public abstract class NasaApiBase {

        @Test
        public void testBaseFunctionality() {
        System.out.println("NasaApiBase test executed.");
        Assert.assertTrue(true);
    }

    // Чтение значений из конфигурационного файла
    protected static final String API_KEY = ConfigLoader.getProperty("api.key");
    protected static final String BASE_URL = ConfigLoader.getProperty("base.url");
    protected static final double LON = ConfigLoader.getDoubleProperty("longitude");  // Долгота
    protected static final double LAT = ConfigLoader.getDoubleProperty("latitude");  // Широта
    protected static final String DATE = ConfigLoader.getCurrentDate();  // Текущая дата
    protected static final double DIM = ConfigLoader.getDoubleProperty("dimension");  // Размер области

    // Абстрактные методы: реализованы в дочерних классах
    protected abstract String buildUrl();
    protected abstract String extractUrlFromResponse(String response);

    /**
     * Метод для получения данных по URL через GET-запрос.
     *
     * @param url URL для запроса
     * @return Ответ API в виде строки
     * @throws IOException В случае ошибки сети
     */
    protected String fetchData(String url) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getCode() != 200) {
                    throw new IOException("Ошибка HTTP: код " + response.getCode());
                }
                return new String(response.getEntity().getContent().readAllBytes());
            }
        }
    }
}
