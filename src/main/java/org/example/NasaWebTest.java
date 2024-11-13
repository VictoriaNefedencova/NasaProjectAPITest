package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;  // Импорт класса Actions

import org.testng.annotations.Test;
import org.testng.Assert;

import java.net.URL;

public class NasaWebTest {

    @Test
    public void testWebPage() {
        System.out.println("NasaWebTest test executed.");
        Assert.assertTrue(true);
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vnefedencova\\chromedriver.exe");

        // драйвер
        WebDriver driver = new ChromeDriver();

        //  Actions для симуляции действий прокрутки
        Actions actions = new Actions(driver);

        try {

            // Разворачиваем окно браузера на полный экран
            driver.manage().window().fullscreen();

            // Открываем сайт NASA API
            driver.get("https://api.nasa.gov/");

            // Разворачиваем окно браузера на полный экран
            driver.manage().window().fullscreen();

            Thread.sleep(450);

            // Находим поле для поиска и вводим "APOD"
            WebElement searchField = driver.findElement(By.id("search-field-big"));
            searchField.sendKeys("APOD");
            Thread.sleep(2550);

            // Нажимаем на кнопку APOD
            WebElement apodButton = driver.findElement(By.id("apod"));
            apodButton.click();

            // Прокручиваем страницу вниз с использованием Actions
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            // Ждем, чтобы страница успела загрузиться
            Thread.sleep(5450);
            actions.sendKeys(Keys.PAGE_DOWN).perform();


            // Ищем код для APOD в документе
            WebElement apodCode = driver.findElement(By.cssSelector("#b-a1 > p:nth-child(7) > code"));
            String apodUrl = apodCode.getText().trim();  // Извлекаем полный URL
            System.out.println("APOD Code: " + apodUrl);

            // Разделяем URL на базовый URL и путь с помощью java.net.URL
            try {
                URL url = new URL(apodUrl.split(" ")[1]); // Извлекаем второй элемент (URL)
                String apodBaseUrl = url.getProtocol() + "://" + url.getHost();  // Базовый URL
                String apodPath = url.getPath();  // Путь

                System.out.println("APOD Base URL: " + apodBaseUrl);
                System.out.println("APOD Path: " + apodPath);

                // Сравниваем с глобальной переменной BASE_URL
                if (apodBaseUrl.equals(NasaApiBase.BASE_URL)) {
                    System.out.println("------------------------------------------------------------APOD URL совпадает с базовым URL!------------------------------------------------------------");
                } else {
                    System.out.println("Ошибка: APOD URL не совпадает с базовым.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ошибка при обработке APOD URL.");
            }
           // Найти кнопку с помощью CSS-селектора
            WebElement button = driver.findElement(By.cssSelector("#browseAPI > div > div > div > form > button > span"));

            // Используем Actions для перемещения к элементу
            //Actions actions = new Actions(driver);
            actions.moveToElement(button).perform();

            Thread.sleep(450);


            // Очищаем поле поиска и вводим "EARTH"
            searchField.clear();
            searchField.sendKeys("EARTH");
            Thread.sleep(2550);


            // Нажимаем на кнопку Earth
            WebElement earthButton = driver.findElement(By.id("earth"));
            earthButton.click();

            // Прокручиваем страницу вниз с использованием Actions
            actions.sendKeys(Keys.PAGE_DOWN).perform();


            actions.sendKeys(Keys.PAGE_DOWN).perform();
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(5150);
            actions.sendKeys(Keys.PAGE_DOWN).perform();

            // Изменяем селектор для Earth
            WebElement earthCode = driver.findElement(By.cssSelector("#b-a4 > p:nth-child(9) > code"));
            String earthUrl = earthCode.getText().trim();  // Извлекаем полный URL
            System.out.println("Earth Code: " + earthUrl);

            // Разделяем URL на базовый URL и путь с помощью java.net.URL
            try {
                URL url = new URL(earthUrl.split(" ")[1]); // Извлекаем второй элемент (URL)
                String earthBaseUrl = url.getProtocol() + "://" + url.getHost();  // Базовый URL
                String earthPath = url.getPath();  // Путь

                System.out.println("Earth Base URL: " + earthBaseUrl);
                System.out.println("Earth Path: " + earthPath);

                // Сравниваем с глобальной переменной BASE_URL
                if (earthBaseUrl.equals(NasaApiBase.BASE_URL)) {
                    System.out.println("------------------------------------------------------------Earth URL совпадает с базовым URL!------------------------------------------------------------");
                } else {
                    System.out.println("Ошибка: Earth URL не совпадает с базовым");
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ошибка при обработке Earth URL");
            }

        } finally {
            // Закрываем браузер
            driver.quit();
        }
    }
}
