package com.endava.nasa.atf.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Путь к feature-файлам
        glue = "com.endava.nasa.atf.steps", // Пакет с step definitions
        plugin = {
                "pretty", // Логирование в консоль
                "html:target/cucumber-html-report.html", // Генерация HTML-отчета
                "json:target/cucumber.json" // Генерация JSON-отчета
        }
)
public class RunCucumberTest {

    @BeforeClass
    public static void setup() {
        // Метод, который выполняется перед запуском тестов
        System.out.println("Запуск тестов Cucumber...");
    }

    @AfterClass
    public static void tearDown() {
        // Автоматическое открытие отчета в браузере после выполнения тестов
        openReport();
    }

    public static void openReport() {
        // Путь к отчету
        String reportPath = "target/cucumber-html-report.html";

        // Проверяем, поддерживается ли Desktop API
        if (Desktop.isDesktopSupported()) {
            try {
                File reportFile = new File(reportPath);
                if (reportFile.exists()) {
                    // Открываем отчет в браузере
                    Desktop.getDesktop().browse(reportFile.toURI());
                } else {
                    System.err.println("Отчет не найден: " + reportPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop API не поддерживается на этой системе.");
        }
    }
}
