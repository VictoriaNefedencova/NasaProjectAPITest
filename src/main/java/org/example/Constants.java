package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    // Default values for configuration properties
    public static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    public static final String REPORT_FILE_PATH = "test-output/NasaApiDynamicReport.html";

    // HTTP status codes
    public static final int HTTP_OK = 200; // HTTP 200 OK status code
    public static final int HTTP_METHOD_NOT_ALLOWED = 405; // HTTP 405 Method Not Allowed status code

    // API-related constants (will be loaded from properties)
    public static String BASE_URL;
    public static String API_KEY;
    public static String LATITUDE;
    public static String LONGITUDE;
    public static String DIMENSION;

    // Load configuration values from the properties file
    static {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(input);

            BASE_URL = properties.getProperty("base.url");
            API_KEY = properties.getProperty("api.key");
            LATITUDE = properties.getProperty("latitude");
            LONGITUDE = properties.getProperty("longitude");
            DIMENSION = properties.getProperty("dimension");

        } catch (IOException ex) {
            System.err.println("Error loading configuration properties: " + ex.getMessage());
            // Handle exception appropriately, such as setting default values or exiting
        }
    }

    // API-related constants for endpoints
    public static final String APOD_ENDPOINT = "/planetary/apod";  // Endpoint for Astronomy Picture of the Day
    public static final String EARTH_API_ENDPOINT = "/planetary/earth/assets";  // Endpoint for Earth imagery
}
