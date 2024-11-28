package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.logging.Logger;

public class NasaWebTest {

    private static final Logger log = Logger.getLogger(NasaWebTest.class.getName());

    // Constants for WebDriver setup and URLs
    private static final String DRIVER_PATH = "C:\\Users\\vnefedencova\\chromedriver.exe";
    private static final String NASA_API_URL = "https://api.nasa.gov/";

    // CSS Selectors
    private static final String SEARCH_FIELD_SELECTOR = "#search-field-big";
    private static final String APOD_BUTTON_SELECTOR = "#apod";
    private static final String APOD_CODE_SELECTOR = "#b-a1 > p:nth-child(7) > code";
    private static final String EARTH_BUTTON_SELECTOR = "#earth";
    private static final String EARTH_CODE_SELECTOR = "#b-a4 > p:nth-child(9) > code";
    private static final String API_BROWSE_BUTTON_SELECTOR = "#browseAPI > div > div > div > form > button > span";

    @Test
    public void testWebPageInteractions() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);

        WebDriver driver = new ChromeDriver();
        Actions actions = new Actions(driver);

        try {
            // Maximize browser window
            driver.manage().window().fullscreen();
            log.info("Browser window maximized.");

            // Navigate to NASA API page
            driver.get(NASA_API_URL);
            log.info("Navigated to NASA API website: " + NASA_API_URL);


            // Maximize browser window
            driver.manage().window().fullscreen();
            log.info("Browser window maximized.");

            // Interact with the search field and input "APOD"
            WebElement searchField = driver.findElement(By.cssSelector(SEARCH_FIELD_SELECTOR));
            searchField.sendKeys("APOD");
            Thread.sleep(2550);

            // Click the APOD button
            WebElement apodButton = driver.findElement(By.cssSelector(APOD_BUTTON_SELECTOR));
            apodButton.click();

            // Scroll the page
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450);
            actions.sendKeys(Keys.PAGE_DOWN).perform();

            // Fetch and validate APOD URL
            validateUrl(driver, APOD_CODE_SELECTOR, "APOD");

            // Interact with API Browse button
            WebElement apiBrowseButton = driver.findElement(By.cssSelector(API_BROWSE_BUTTON_SELECTOR));
            actions.moveToElement(apiBrowseButton).perform();
            Thread.sleep(450);

            // Clear the search field and input "EARTH"
            searchField.clear();
            searchField.sendKeys("EARTH");
            Thread.sleep(2550);

            // Click the Earth button
            WebElement earthButton = driver.findElement(By.cssSelector(EARTH_BUTTON_SELECTOR));
            earthButton.click();

            // Scroll the page
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2150);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2150);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450);
            actions.sendKeys(Keys.PAGE_DOWN).perform();

            // Fetch and validate Earth URL
            validateUrl(driver, EARTH_CODE_SELECTOR, "EARTH");

        } finally {
            // Close the browser
            driver.quit();
            log.info("Browser closed.");
        }
    }

    private void validateUrl(WebDriver driver, String selector, String type) {
        try {
            WebElement codeElement = driver.findElement(By.cssSelector(selector));
            String fullUrl = codeElement.getText().trim();
            log.info(type + " Code: " + fullUrl);

            URL url = new URL(fullUrl.split(" ")[1]); // Extract the second part (URL)
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String path = url.getPath();

            log.info(type + " Base URL: " + baseUrl);
            log.info(type + " Path: " + path);

            // Compare with the global base URL
            if (baseUrl.equals(NasaApiBase.BASE_URL)) {
                log.info(type + " URL matches the base URL.");
            } else {
                log.warning(type + " URL does not match the base URL.");
            }
        } catch (Exception e) {
            log.severe("Error processing " + type + " URL: " + e.getMessage());
        }
    }
}
