package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            // Load the properties file
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
        }
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static double getDouble(String key) {
        String value = properties.getProperty(key);
        return value != null ? Double.parseDouble(value) : 0.0;
    }

    public static String getCurrentDate() {
        // Logic to get current date in the format required by the API
        return "2024-11-18"; // Example of static date format, replace with dynamic logic
    }
}
