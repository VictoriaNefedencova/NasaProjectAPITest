package com.endava.nasa.atf.steps;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.ConfigLoader;
import org.example.Constants;
import org.example.NasaApi;
import org.example.NasaEarthImagery;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class NasaApiSteps {
    private final NasaApi nasaApi = new NasaApi();
    private final NasaEarthImagery earthImagery = new NasaEarthImagery();
    private static final Logger log = Logger.getLogger(NasaApiSteps.class.getName());
    private WebDriver driver;
    private String apiUrl;
    private String apiResponse;
    private String imageUrl;
    private int statusCode;

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
            log.info("Extracted image URL: " + this.imageUrl);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(this.imageUrl));
                log.info("Image successfully opened in the browser: " + this.imageUrl);
            } else {
                Assert.fail("Desktop operations are not supported.");
            }
        } catch (Exception var2) {
            Exception e = var2;
            log.severe("Error opening image: " + e.getMessage());
            Assert.fail("Error opening image: " + e.getMessage());
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
                log.warning("Image successfully opened in browser: " + this.imageUrl);
            } else {
                log.severe("Desktop operations are not supported on this system.");
                Assert.fail("Desktop operations are not supported on this system.");
            }
        } catch (Exception var4) {
            Exception e = var4;
            log.severe("Error while opening the image in browser: " + e.getMessage());
            Assert.fail("Error while opening the image in browser: " + e.getMessage());
        }

    }

    @Given("The Earth API only accepts GET parameters on the NASA API page")
    public void theEarthAPIOnlyAcceptsGETParametersOnTheNASAAPIPage() {
    }

    @Given("I navigate to the NASA API homepage")
    public void navigateToNasaApiHomepage() {
        System.setProperty("webdriver.chrome.driver", "C://Users//vnefedencova//chromedriver.exe");
        this.driver = new ChromeDriver();
        new Actions(this.driver);
        this.driver.get(Constants.BASE_URL);
        this.driver.manage().window().fullscreen();
        log.info("Navigated to NASA API website: " + Constants.BASE_URL);
    }

    @When("I search for {string}")
    public void iSearchFor(String searchQuery) throws InterruptedException {
        this.searchForApi(searchQuery);
    }

    public void searchForApi(String searchQuery) throws InterruptedException {
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

    @Then("I should see the {string} URL displayed and it matches the built URL")
    public void validateUrlDisplayedAndCompare(String type) {
        String selector = type.equalsIgnoreCase("APOD") ? "#b-a1 > p:nth-child(7) > code" : "#b-a4 > p:nth-child(9) > code";
        String expectedUrl = type.equalsIgnoreCase("APOD") ? this.nasaApi.buildUrl() : this.earthImagery.buildUrl();

        try {
            WebElement codeElement = this.driver.findElement(By.cssSelector(selector));
            String fullUrl = codeElement.getText().trim();
            URL url = new URL(fullUrl);
            String var10000 = url.getProtocol();
            String baseUrl = var10000 + "://" + url.getHost();
            String path = url.getPath();
            log.warning(type + " Base URL: " + baseUrl);
            log.warning(type + " Path: " + path);
            if (baseUrl.equals(Constants.BASE_URL)) {
                log.warning(type + " URL matches the base URL.");
            } else {
                log.warning(type + " URL does not match the base URL.");
            }

            Assert.assertEquals("Built URL matches API response URL", expectedUrl, fullUrl);
            log.warning(type + " Full URL matches the built URL.");
        } catch (Exception var9) {
            Exception e = var9;
            log.warning("Error processing " + type + " URL: " + e.getMessage());
            Assert.fail("Error processing " + type + " URL: " + e.getMessage());
        }

    }

    @Then("I should see the APOD URL displayed and it matches the built URL")
    public void iShouldSeeTheAPODURLDisplayedAndItMatchesTheBuiltURL() {
    }

    @Then("I should see the Earth URL displayed and it matches the built URL")
    public void iShouldSeeTheEarthURLDisplayedAndItMatchesTheBuiltURL() {
    }

    public String callExtractUrlFromResponse(String response) {
        return this.nasaApi.extractUrlFromResponse(response);
    }

    private void handleApiRequest(NasaApiHandler handler) throws IOException {
        this.apiUrl = handler.buildUrl();
        log.info("Sending request to NASA API with URL: " + this.apiUrl);
        this.apiResponse = handler.fetchData(this.apiUrl);
        Assert.assertNotNull("API response should not be null", this.apiResponse);
        log.info("Received response from NASA API: " + this.apiResponse);
        this.imageUrl = handler.extractImageUrl(this.apiResponse);
        Assert.assertNotNull("Image URL should not be null", this.imageUrl);
    }

    @Then("the API image is opened in the browser")
    public void openImageInBrowser() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(this.imageUrl));
                log.warning("Image successfully opened in the browser: " + this.imageUrl);
            } else {
                Assert.fail("Desktop operations are not supported.");
            }
        } catch (Exception var2) {
            Exception e = var2;
            log.severe("Error opening image: " + e.getMessage());
            Assert.fail("Error opening image: " + e.getMessage());
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
            log.info("Browser closed successfully.");
        }

    }

    @When("I send a valid POST request to the Earth API on NASA's site")
    public void sendPostRequestToEarthApi() {
        this.apiUrl = (new EarthApiHandler()).buildUrl();
        log.info("Sending POST request to Earth API with URL: " + this.apiUrl);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            try {
                HttpPost httpPost = new HttpPost(this.apiUrl);
                CloseableHttpResponse response = httpClient.execute(httpPost);

                try {
                    this.statusCode = response.getStatusLine().getStatusCode();
                    log.info("Received response with status code: " + this.statusCode);
                } catch (Throwable var8) {
                    if (response != null) {
                        try {
                            response.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }

                    throw var8;
                }

                if (response != null) {
                    response.close();
                }
            } catch (Throwable var9) {
                if (httpClient != null) {
                    try {
                        httpClient.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                }

                throw var9;
            }

            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException var10) {
            IOException e = var10;
            log.severe("Error during POST request: " + e.getMessage());
            Assert.fail("Error during POST request: " + e.getMessage());
        }

    }

    @Then("the Earth API response contains a {int} error")
    public void verifyPostRequestError(int expectedStatusCode) {
        Assert.assertEquals("Status code should match the expected value", (long)expectedStatusCode, (long)this.statusCode);
        if (this.statusCode == 405) {
            log.warning("-----------------------------------------------------------------------------------Error 405: POST method is not allowed for this resource.----------------------------------------------------------------------------------");
        }

    }

    abstract static class NasaApiHandler {
        NasaApiHandler() {
        }

        abstract String buildUrl();

        abstract String fetchData(String var1) throws IOException;

        abstract String extractImageUrl(String var1);
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
            String var10000 = Constants.BASE_URL;
            return var10000 + "/planetary/earth/assets?lon=" + Constants.LONGITUDE + "&lat=" + Constants.LATITUDE + "&date=" + ConfigLoader.getCurrentDate() + "&dim=" + Constants.DIMENSION + "&api_key=" + Constants.API_KEY;
        }

        String fetchData(String url) throws IOException {
            return this.earthImagery.fetchData(url);
        }

        private String extractImageUrlUsingReflection(Object handler, String response) {
            try {
                Method method = handler.getClass().getDeclaredMethod("extractUrlFromResponse", String.class);
                method.setAccessible(true);
                return (String)method.invoke(handler, response);
            } catch (Exception var4) {
                Exception e = var4;
                NasaApiSteps.log.severe("Error accessing protected method: " + e.getMessage());
                throw new RuntimeException("Failed to extract image URL using reflection", e);
            }
        }

        String extractImageUrl(String response) {
            return this.extractImageUrlUsingReflection(this.earthImagery, response);
        }
    }
}

