package com.endava.nasa.atf.steps;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.ConfigLoader;
import org.example.Constants;
import org.example.NasaApi;
import org.example.NasaEarthImagery;
import org.example.NasaWebTest;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import java.io.InputStream;
import java.util.Properties;
import io.github.bonigarcia.wdm.WebDriverManager;

public class NasaApiSteps {
    private final NasaApi nasaApi = new NasaApi();
    //private final NasaEarthImagery earthImagery = new NasaEarthImagery();
    private static final Logger log = Logger.getLogger(NasaApiSteps.class.getName());
    private WebDriver driver;
    private String apiUrl;
    private String apiResponse;
    private String imageUrl;
    private int statusCode;
    private final NasaWebTest nasaWebTest = new NasaWebTest();

    public NasaApiSteps() {
    }

    @Given("the current day's image is available on the APOD API")
    public void theCurrentDaySImageIsAvailableOnTheAPODAPI() {
    }

    @Then("the APOD API image is opened in the browser")
    public void theAPODAPIImageIsOpenedInTheBrowser() {
        try {
            this.imageUrl = this.callExtractUrlFromResponse(this.apiResponse);
            Assert.assertNotNull("Image URL should not be null", this.imageUrl);
            log.severe("Extracted image URL: " + this.imageUrl);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(this.imageUrl));
                log.severe("Image successfully opened in the browser: " + this.imageUrl);
            } else {
                Assert.fail("Desktop operations are not supported.");
            }
        } catch (Exception generalException) {
            log.severe("Error opening image: " + generalException.getMessage());
            Assert.fail("Error opening image: " + generalException.getMessage());
        }
    }

    @Given("the current day's image is available on the Earth API")
    public void theCurrentDaySImageIsAvailableOnTheEarthAPI() {
    }

    @Then("the Earth API image is opened in the browser")
    public void theEarthAPIImageIsOpenedInTheBrowser() {
        this.openImageInBrowser(this.apiResponse, "Earth");
    }

    private void openImageInBrowser(String response, String apiType) {
        try {
            if ("Earth".equals(apiType)) {
                this.imageUrl = this.callExtractUrlFromResponse(response);
            } else {
                Assert.fail("Invalid API type specified.");
            }

            Assert.assertNotNull("Image URL should not be null", this.imageUrl);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(this.imageUrl));
                log.severe("Image successfully opened in browser: " + this.imageUrl);
            } else {
                log.severe("Desktop operations are not supported on this system.");
                Assert.fail("Desktop operations are not supported on this system.");
            }
        } catch (Exception generalException) {
            log.severe("Error while opening the image in browser: " + generalException.getMessage());
            Assert.fail("Error while opening the image in browser: " + generalException.getMessage());
        }
    }

    @Given("The Earth API only accepts GET parameters on the NASA API page")
    public void theEarthAPIOnlyAcceptsGETParametersOnTheNASAAPIPage() {
    }

    @Given(value = "I navigate to the NASA API homepage")
    public void navigateToNasaApiHomepage() {
        loadDriverPath(); // Load the driver path

        this.driver = new ChromeDriver(); // Initialize the WebDriver with ChromeDriver
        new Actions(this.driver); // Create an Actions object for performing complex interactions
        this.driver.get(Constants.BASE_URL); // Navigate to the NASA API homepage
        this.driver.manage().window().fullscreen(); // Set the browser window to fullscreen
        log.severe("Navigated to NASA API website: " + Constants.BASE_URL); // Log the navigation action
    }

    // Load the driver path from the properties file
    private void loadDriverPath() {
        // Load the value of the 'useWebDriverManager' property
        String useWebDriverManager = System.getProperty("useWebDriverManager", "false");

        // If useWebDriverManager=true, use WebDriverManager to download the driver
        if (Boolean.parseBoolean(useWebDriverManager)) {
            try {
                // Use WebDriverManager to set up the driver automatically
                WebDriverManager.chromedriver().setup();
                log.severe("WebDriverManager setup completed. ChromeDriver is now managed automatically.");
            } catch (Exception e) {
                log.severe("Error while setting up WebDriverManager: " + e.getMessage());
            }
        } else {
            // If WebDriverManager is not used, load the driver path from the config file
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (inputStream == null) {
                    log.severe("config.properties file not found.");
                    return;
                }

                // Load properties from the file
                Properties properties = new Properties();
                properties.load(inputStream);

                // Get the ChromeDriver path from the configuration file
                String driverPath = properties.getProperty("webdriver.chrome.driver");

                // If the driver path is not specified or the file doesn't exist, log an error
                if (driverPath == null || driverPath.isEmpty()) {
                    log.severe("ChromeDriver path not specified in the config file.");
                    return;
                }

                // Set the system property for ChromeDriver using the path from the config file
                System.setProperty("webdriver.chrome.driver", driverPath);
                log.severe("ChromeDriver path set to: " + driverPath);
            } catch (Exception e) {
                log.severe("Error loading driver path from properties file: " + e.getMessage());
            }
        }
    }


    @When("I search for {string}")
    public void iSearchFor(String searchQuery) throws InterruptedException {
        this.searchForApi(searchQuery);
    }

    public void searchForApi(String searchQuery) throws InterruptedException {

        // To make sure WebDriver is initialized before performing any actions
        if (this.driver == null) {
            loadDriverPath(); // Load the WebDriver path and initialize ChromeDriver
            this.driver = new ChromeDriver(); // Initialize the ChromeDriver
        }
        WebElement searchField = this.driver.findElement(By.cssSelector("#search-field-big"));
        searchField.clear();
        searchField.sendKeys(new CharSequence[]{searchQuery});
        Thread.sleep(1000L);
        WebElement button = this.driver.findElement(By.cssSelector(searchQuery.equalsIgnoreCase("APOD") ? "#apod" : "#earth"));
        button.click();
        Actions actions = new Actions(this.driver);
        actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
        Thread.sleep(1000L);
        actions.sendKeys(new CharSequence[]{Keys.PAGE_DOWN}).perform();
        Thread.sleep(1000L);
    }

    @Then(value = "I should see the {string} URL displayed and it matches the built URL")
    public void validateUrlDisplayedAndCompare(String type) {
        // Determine the selector based on type
        String selector = type.equalsIgnoreCase("APOD") ? "#b-a1 > p:nth-child(7) > code" : "#b-a4 > p:nth-child(9) > code";

        // Call validateUrl from NasaWebTest class
        this.nasaWebTest.validateUrl(driver, selector, type);  // Call the method
    }

    public String callExtractUrlFromResponse(String response) {
        return this.nasaApi.extractUrlFromResponse(response);
    }

    private void handleApiRequest(NasaApiHandler handler) throws IOException {
        this.apiUrl = handler.buildUrl();
        log.severe("Sending request to NASA API with URL: " + this.apiUrl);
        this.apiResponse = handler.fetchData(this.apiUrl);
        Assert.assertNotNull("API response should not be null", this.apiResponse);
        log.severe("Received response from NASA API: " + this.apiResponse);
        this.imageUrl = handler.extractImageUrl(this.apiResponse);
        Assert.assertNotNull("Image URL should not be null", this.imageUrl);
    }

    @Then("the API image is opened in the browser")
    public void openImageInBrowser() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(this.imageUrl));
                log.severe("Image successfully opened in the browser: " + this.imageUrl);
            } else {
                Assert.fail("Desktop operations are not supported.");
            }
        } catch (Exception generalException) {
            log.severe("Error opening image: " + generalException.getMessage());
            Assert.fail("Error opening image: " + generalException.getMessage());
        }
    }

    @When("I send a valid request to the APOD API on NASA's site")
    public void sendRequestToApodApi() throws IOException {
        this.handleApiRequest(new ApodApiHandler());
    }

    @When("I send a valid request to the Earth API on NASA's site")
    public void sendRequestToEarthApi() throws IOException {
        this.handleApiRequest(new EarthApiHandler());
    }

    @After
    public void closeBrowser() {
        if (this.driver != null) {
            this.driver.quit();
            log.severe("Browser closed successfully.");
        }
    }

    @When("I send a valid POST request to the Earth API on NASA's site")
    public void sendPostRequestToEarthApi() {
        this.apiUrl = (new EarthApiHandler()).buildUrl();
        log.severe("Sending POST request to Earth API with URL: " + this.apiUrl);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            try {
                HttpPost httpPost = new HttpPost(this.apiUrl);
                CloseableHttpResponse response = httpClient.execute(httpPost);

                try {
                    this.statusCode = response.getStatusLine().getStatusCode();
                    log.severe("Received response with status code: " + this.statusCode);
                } catch (Throwable responseException) {
                    if (response != null) {
                        try {
                            response.close();
                        } catch (Throwable closeException) {
                            responseException.addSuppressed(closeException);
                        }
                    }

                    throw responseException;
                }

                if (response != null) {
                    response.close();
                }
            } catch (Throwable httpClientException) {
                if (httpClient != null) {
                    try {
                        httpClient.close();
                    } catch (Throwable closeException) {
                        httpClientException.addSuppressed(closeException);
                    }
                }

                throw httpClientException;
            }

            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException ioException) {
            log.severe("Error during POST request: " + ioException.getMessage());
            Assert.fail("Error during POST request: " + ioException.getMessage());
        }
    }

    @Then("the Earth API response contains a {int} error")
    public void verifyPostRequestError(int expectedStatusCode) {
        Assert.assertEquals("Status code should match the expected value", (long)expectedStatusCode, (long)this.statusCode);
        if (this.statusCode == 405) {
            log.severe("-----------------------------------------------------------------------------------" +
                    "Error 405: POST method is not allowed for this resource." +
                    "----------------------------------------------------------------------------------");
        }
    }

    @Then("I should see the APOD URL displayed and it matches the built URL")
    public void iShouldSeeTheAPODURLDisplayedAndItMatchesTheBuiltURL() {
    }

    @Then("I should see the Earth URL displayed and it matches the built URL")
    public void iShouldSeeTheEarthURLDisplayedAndItMatchesTheBuiltURL() {
    }

    abstract static class NasaApiHandler {
        NasaApiHandler() {
        }

        abstract String buildUrl();

        abstract String fetchData(String selection) throws IOException;

        abstract String extractImageUrl(String selection);
    }

    static class ApodApiHandler extends NasaApiHandler {
        private final NasaApi nasaApi = new NasaApi();

        ApodApiHandler() {
        }

        String buildUrl() {
            return Constants.BASE_URL + "/planetary/apod?api_key=" + Constants.API_KEY;
        }

        String fetchData(String url) throws IOException {
            return this.nasaApi.fetchData(url);
        }

        String extractImageUrl(String response) {
            return this.nasaApi.extractUrlFromResponse(response);
        }
    }

    static class EarthApiHandler extends NasaApiHandler {
        private final NasaEarthImagery earthImagery = new NasaEarthImagery();

        EarthApiHandler() {
        }

        String buildUrl() {
            String protocolUrl = Constants.BASE_URL;
            return protocolUrl + "/planetary/earth/assets?lon=" + Constants.LONGITUDE + "&lat=" + Constants.LATITUDE + "&date=" + ConfigLoader.getCurrentDate() + "&dim=" + Constants.DIMENSION + "&api_key=" + Constants.API_KEY;
        }

        String fetchData(String url) throws IOException {
            return this.earthImagery.fetchData(url);
        }

        private String extractImageUrlUsingReflection(Object handler, String response) {
            try {
                Method method = handler.getClass().getDeclaredMethod("extractUrlFromResponse", String.class);
                method.setAccessible(true);
                return (String)method.invoke(handler, response);
            } catch (Exception reflectionException) {
                log.severe("Error accessing protected method: " + reflectionException.getMessage());
                throw new RuntimeException("Failed to extract image URL using reflection", reflectionException);
            }
        }

        String extractImageUrl(String response) {
            return this.extractImageUrlUsingReflection(this.earthImagery, response);
        }
    }
}
