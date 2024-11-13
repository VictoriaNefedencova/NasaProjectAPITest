package org.example;

import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties = new Properties();

    static {
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static double getDoubleProperty(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }

    public static String getCurrentDate() {
        return LocalDate.now().toString();  // Возвращает текущую дату
    }
}
