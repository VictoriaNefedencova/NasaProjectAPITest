package org.example;

import java.net.URL;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class NasaWebTest {
    private static final Logger log = Logger.getLogger(NasaWebTest.class.getName());
    private static final String DRIVER_PATH = "C://Users//vnefedencova//chromedriver.exe";
    private static final String NASA_API_URL;
    private static final String SEARCH_FIELD_SELECTOR = "#search-field-big";
    private static final String APOD_BUTTON_SELECTOR = "#apod";
    private static final String APOD_CODE_SELECTOR = "#b-a1 > p:nth-child(7) > code";
    private static final String EARTH_BUTTON_SELECTOR = "#earth";
    private static final String EARTH_CODE_SELECTOR = "#b-a4 > p:nth-child(9) > code";
    private static final String API_BROWSE_BUTTON_SELECTOR = "#browseAPI > div > div > div > form > button > span";

    public NasaWebTest() {
    }

    @Test
    public void testWebPageInteractions() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C://Users//vnefedencova//chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        Actions actions = new Actions(driver);

        try {
            driver.manage().window().fullscreen();
            log.info("Browser window maximized.");
            driver.get(NASA_API_URL);
            log.info("Navigated to NASA API website: " + NASA_API_URL);
            driver.manage().window().fullscreen();
            log.info("Browser window maximized.");
            WebElement searchField = driver.findElement(By.cssSelector("#search-field-big"));
            searchField.sendKeys(new CharSequence[]{"APOD"});
            Thread.sleep(2550L);
            WebElement apodButton = driver.findElement(By.cssSelector("#apod"));
            apodButton.click();
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            Thread.sleep(2450L);
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            Thread.sleep(2450L);
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            Thread.sleep(2450L);
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            this.validateUrl(driver, "#b-a1 > p:nth-child(7) > code", "APOD");
            WebElement apiBrowseButton = driver.findElement(By.cssSelector("#browseAPI > div > div > div > form > button > span"));
            actions.moveToElement(apiBrowseButton).perform();
            Thread.sleep(450L);
            searchField.clear();
            searchField.sendKeys(new CharSequence[]{"EARTH"});
            Thread.sleep(2550L);
            WebElement earthButton = driver.findElement(By.cssSelector("#earth"));
            earthButton.click();
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            Thread.sleep(2150L);
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            Thread.sleep(2150L);
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            Thread.sleep(2450L);
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            Thread.sleep(2450L);
            actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
            this.validateUrl(driver, "#b-a4 > p:nth-child(9) > code", "EARTH");
        } finally {
            driver.quit();
            log.info("Browser closed.");
        }

    }

    private void validateUrl(WebDriver driver, String selector, String type) {
        try {
            WebElement codeElement = driver.findElement(By.cssSelector(selector));
            String fullUrl = codeElement.getText().trim();
            log.info(type + " Code: " + fullUrl);
            URL url = new URL(fullUrl.split(" ")[1]);
            String var10000 = url.getProtocol();
            String baseUrl = var10000 + "://" + url.getHost();
            String path = url.getPath();
            log.info(type + " Base URL: " + baseUrl);
            log.info(type + " Path: " + path);
            if (baseUrl.equals(NasaApiBase.BASE_URL)) {
                log.info(type + " URL matches the base URL.");
            } else {
                log.warning(type + " URL does not match the base URL.");
            }
        } catch (Exception var9) {
            Exception e = var9;
            log.severe("Error processing " + type + " URL: " + e.getMessage());
        }

    }

    static {
        NASA_API_URL = Constants.BASE_URL;
    }
}

