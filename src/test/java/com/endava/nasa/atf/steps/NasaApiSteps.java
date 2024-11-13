package com.endava.nasa.atf.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.NasaApi;
import org.example.NasaEarthImagery;

import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;

import static org.junit.Assert.*;

public class NasaApiSteps extends NasaApi {

    private String apiUrl;      // URL для API-запроса
    private String apiResponse; // Ответ API
    private String imageUrl;    // URL изображения

    // Создание вложенного класса-наследника для Earth API
    private final EarthImageryProxy earthImagery = new EarthImageryProxy();

    // Вложенный класс для доступа к защищённым методам Earth API
    private static class EarthImageryProxy extends NasaEarthImagery {
        public String publicBuildUrl() {
            return buildUrl(); // Доступ к защищённому методу
        }

        public String publicFetchData(String url) throws IOException {
            return fetchData(url); // Доступ к защищённому методу
        }

        public String publicExtractUrlFromResponse(String response) {
            return extractUrlFromResponse(response); // Доступ к защищённому методу
        }
    }

    // APOD API
    @Given("the current day's image is available on the APOD API")
    public void the_current_day_image_exists_on_apod_api() {
        apiUrl = this.buildUrl(); // Доступ через наследование
        assertNotNull("URL для APOD API не должен быть null", apiUrl);
    }

    @When("I send a valid request to the APOD API on NASA's site")
    public void i_send_a_request_to_apod_api_on_nasa_site() {
        try {
            apiResponse = this.fetchData(apiUrl); // Доступ через наследование
            assertNotNull("Ответ от APOD API не должен быть null", apiResponse);
        } catch (IOException e) {
            fail("Ошибка при выполнении запроса к APOD API: " + e.getMessage());
        }
    }

    @Then("the APOD API image is opened in the browser")
    public void the_image_is_opened_in_the_browser() {
        openImageInBrowser(apiResponse, "APOD");
    }

    // Earth API
    @Given("the current day's image is available on the Earth API")
    public void the_current_day_image_exists_on_earth_api() {
        apiUrl = earthImagery.publicBuildUrl(); // Доступ через публичный метод обёртки
        assertNotNull("URL для Earth API не должен быть null", apiUrl);
    }

    @When("I send a valid request to the Earth API on NASA's site")
    public void i_send_a_request_to_earth_api_on_nasa_site() {
        try {
            apiResponse = earthImagery.publicFetchData(apiUrl); // Доступ через публичный метод обёртки
            assertNotNull("Ответ от Earth API не должен быть null", apiResponse);
        } catch (IOException e) {
            fail("Ошибка при выполнении запроса к Earth API: " + e.getMessage());
        }
    }

    @Then("the Earth API image is opened in the browser")
    public void the_image_should_be_opened_in_the_browser() {
        openImageInBrowser(apiResponse, "Earth");
    }

    // Приватный метод для открытия изображения
    private void openImageInBrowser(String response, String apiType) {
        try {
            if ("APOD".equals(apiType)) {
                imageUrl = this.extractUrlFromResponse(response); // Доступ через наследование
            } else if ("Earth".equals(apiType)) {
                imageUrl = earthImagery.publicExtractUrlFromResponse(response); // Доступ через публичный метод обёртки
            }

            assertNotNull("URL изображения не должен быть null", imageUrl);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(imageUrl));
                System.out.println("Изображение открыто в браузере: " + imageUrl);
            } else {
                fail("Открытие браузера не поддерживается на этой системе.");
            }
        } catch (Exception e) {
            fail("Ошибка при открытии изображения в браузере: " + e.getMessage());
        }
    }
}
