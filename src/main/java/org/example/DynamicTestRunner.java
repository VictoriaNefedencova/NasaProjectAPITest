package org.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.TestNG;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class DynamicTestRunner {

    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;

    @BeforeSuite
    public void setupExtentReport() {
        // Настройка ExtentReports
        sparkReporter = new ExtentSparkReporter("test-output/NasaApiDynamicReport.html");
        sparkReporter.config().setDocumentTitle("NASA API Test Report");
        sparkReporter.config().setReportName("Dynamic Test Execution Report");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Tester", "Nefedencova Victoria");
    }

    @Test
    public void executeDynamicTests() {
        // Динамический список тестовых классов
        List<Class<?>> testClasses = List.of(
                org.example.NasaApiBase.class,
                org.example.NasaApi.class,
                org.example.NasaCheckError.class,
                org.example.NasaEarthImagery.class,
                org.example.NasaWebTest.class
        );

        for (Class<?> testClass : testClasses) {
            runTestsForClass(testClass);
        }
    }

    private void runTestsForClass(Class<?> testClass) {
        ExtentTest testSection = extent.createTest(testClass.getSimpleName() + " Tests");
        try {
            // Создаем TestNG instance и добавляем класс
            TestNG testNG = new TestNG();
            testNG.setTestClasses(new Class[]{testClass});
            testNG.run();

            // Логируем успешное выполнение
            testSection.pass("All tests in " + testClass.getSimpleName() + " executed successfully.");
        } catch (Exception e) {
            // Логируем ошибку
            testSection.fail("Error executing tests in " + testClass.getSimpleName() + ": " + e.getMessage());
        }
    }

    @AfterSuite
    public void generateExtentReport() {
        // Генерация отчета
        extent.flush();

        // Путь к отчету
        String reportPath = "test-output/NasaApiDynamicReport.html";

        // Открытие отчета в браузере
        try {
            Desktop.getDesktop().browse(new File(reportPath).toURI());
            System.out.println("Report generated: " + reportPath);
        } catch (Exception e) {
            System.err.println("Failed to open the report in browser: " + e.getMessage());
        }
    }
}
