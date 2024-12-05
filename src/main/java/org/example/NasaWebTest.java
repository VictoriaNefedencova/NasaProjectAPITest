package org.example;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
//import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NasaWebTest {
    //private static final Logger log = Logger.getLogger(NasaWebTest.class.getName());
    private static final Logger log = LogManager.getLogger(NasaWebTest.class);
    private static final String NASA_API_URL;

    private static String webdriverPath;

    public NasaWebTest() {
    }

    // Load the driver path from config.properties
    static {
        loadDriverPath();
        NASA_API_URL = Constants.BASE_URL;  // Assuming BASE_URL is predefined in Constants class.
    }

    @Test
    public void testWebPageInteractions() throws InterruptedException {
        // Use the driver path loaded from the config file
        if (webdriverPath != null && !webdriverPath.isEmpty()) {
            System.setProperty("webdriver.chrome.driver", webdriverPath);
        } else {
            log.warn("Driver path is not set. Using default path.");
            // System.setProperty("webdriver.chrome.driver", "C://path_to_chromedriver");
        }

        WebDriver driver = new ChromeDriver();
        Actions actions = new Actions(driver);

        try {
            driver.manage().window().fullscreen();
            log.warn("Browser window maximized.");
            driver.get(NASA_API_URL);
            log.warn("Navigated to NASA API website: " + NASA_API_URL);
            WebElement searchField = driver.findElement(By.cssSelector("#search-field-big"));
            searchField.sendKeys(new CharSequence[]{"APOD"});
            Thread.sleep(2550L);
            WebElement apodButton = driver.findElement(By.cssSelector("#apod"));
            apodButton.click();
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450L);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450L);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450L);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            this.validateUrl(driver, "#b-a1 > p:nth-child(7) > code", "APOD");
            WebElement apiBrowseButton = driver.findElement(By.cssSelector("#browseAPI > div > div > div > form > button > span"));
            actions.moveToElement(apiBrowseButton).perform();
            Thread.sleep(450L);
            searchField.clear();
            searchField.sendKeys(new CharSequence[]{"EARTH"});
            Thread.sleep(2550L);
            WebElement earthButton = driver.findElement(By.cssSelector("#earth"));
            earthButton.click();
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2150L);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2150L);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450L);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            Thread.sleep(2450L);
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            this.validateUrl(driver, "#b-a4 > p:nth-child(9) > code", "EARTH");
        } finally {
            driver.quit();
            log.warn("Browser closed.");
        }
    }

    public void validateUrl(WebDriver driver, String selector, String type) {
        try {
            WebElement codeElement = driver.findElement(By.cssSelector(selector));
            String fullUrl = codeElement.getText().trim();
            log.warn(type + " Code: " + fullUrl);
            URL url = new URL(fullUrl.split(" ")[1]);
            String protocolUrl = url.getProtocol();
            String baseUrl = protocolUrl + "://" + url.getHost();
            String path = url.getPath();
            log.warn(type + " Base URL: " + baseUrl);
            log.warn(type + " Path: " + path);
            if (baseUrl.equals(NasaApiBase.BASE_URL)) {
                log.info(type + " URL matches the base URL.");
            } else {
                log.info(type + " URL does not match the base URL.");
            }
        } catch (Exception exep) {
            log.warn("Error processing " + type + " URL: " + exep.getMessage());
        }
    }

    // Method to load the WebDriver path from config.properties
    private static void loadDriverPath() {
        Properties properties = new Properties();
        try (InputStream inputStream = NasaWebTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                log.warn("Sorry, unable to find config.properties");
                return;
            }
            properties.load(inputStream);
            webdriverPath = properties.getProperty("webdriver.chrome.driver");
            if (webdriverPath != null) {
                log.warn("WebDriver path loaded from config: " + webdriverPath);
            } else {
                log.warn("webdriver.chrome.driver not found in config.properties.");
            }
        } catch (Exception e) {
            log.warn("Error loading WebDriver path from config.properties: " + e.getMessage());
        }
    }
}
