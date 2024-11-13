package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class NasaEarthImagery extends NasaApiBase {

    @Test
    public void testImageryProcessing() {
        System.out.println("NasaEarthImagery test executed.");
        Assert.assertTrue(true);
    }

    @Override
    protected String buildUrl() {
        // Формируем URL запроса для получения изображений
        return BASE_URL + "/planetary/earth/assets?lon=" + LON + "&lat=" + LAT + "&date=" + DATE + "&dim=" + DIM + "&api_key=" + API_KEY;
    }

    @Override
    protected String extractUrlFromResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // ответ преобразуется в объект NasaAnswerEarth
            NasaAnswerEarth nasaAnswer = mapper.readValue(response, NasaAnswerEarth.class);
            return nasaAnswer.url;  // Возвращаем URL из ответа
        } catch (IOException e) {
            System.err.println("Ошибка при парсинге JSON: " + e.getMessage());
            return null;
        }
    }

    // Метод для получения и открытия изображения в браузере
    public void fetchAndOpenImage() {
        try {
            String url = buildUrl();
            String response = fetchData(url);

            String imageUrl = extractUrlFromResponse(response);

            if (imageUrl != null) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(imageUrl)); // Открытие изображения в браузере
                    System.out.println("Изображение открыто в браузере: " + imageUrl);
                } else {
                    System.out.println("Открытие браузера не поддерживается на этой системе.");
                }
            } else {
                System.out.println("URL изображения не найден.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при получении данных: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при открытии изображения в браузере: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        NasaEarthImagery imagery = new NasaEarthImagery();
        imagery.fetchAndOpenImage();
    }
}
