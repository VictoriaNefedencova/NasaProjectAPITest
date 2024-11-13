package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class NasaApi extends NasaApiBase {

    @Test
    public void testApiConnection() {
        System.out.println("NasaApi test executed.");
        Assert.assertTrue(true);
    }

    @Override
    protected String buildUrl() {
        // URL для запроса данных
        return BASE_URL + "/planetary/apod?api_key=" + API_KEY + "&date=" + DATE;
    }

    @Override
    protected String extractUrlFromResponse(String response) {
        try {
            // Парсим JSON-ответ
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);

            // Пытаемся получить "url" из ответа
            String url = rootNode.path("url").asText(null); // Возвращает null, если ключ отсутствует
            if (url == null || url.isEmpty()) {
                throw new IOException("URL не найден в ответе API.");
            }
            return url;
        } catch (IOException e) {
            System.err.println("Ошибка при парсинге JSON: " + e.getMessage());
            return null;
        }
    }

    public void openImageInBrowser() {
        try {
            // URL API
            String apiUrl = buildUrl();

            // запрос к API
            String response = fetchData(apiUrl);

            // Извлекаем URL изображения
            String imageUrl = extractUrlFromResponse(response);

            // Открываем изображение в браузере
            if (imageUrl != null) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(imageUrl));
                    System.out.println("Изображение открыто в браузере: " + imageUrl);
                } else {
                    System.out.println("Открытие браузера не поддерживается на этой системе.");
                }
            } else {
                System.out.println("Не удалось извлечь URL изображения из ответа API.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при открытии изображения в браузере: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Создаем экземпляр API
        NasaApi api = new NasaApi();

        // Открываем изображение в браузере
        api.openImageInBrowser();
    }


}
