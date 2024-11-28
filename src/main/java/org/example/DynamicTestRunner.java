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
import java.util.logging.Logger;

public class DynamicTestRunner {

    private static final Logger log = Logger.getLogger(DynamicTestRunner.class.getName());

    // Use constant for report path
    private static final String REPORT_PATH = Constants.REPORT_FILE_PATH;
    private static final String REPORT_TITLE = "NASA API Test Report";
    private static final String REPORT_NAME = "Dynamic Test Execution Report";

    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;

    @BeforeSuite
    public void setupExtentReport() {
        // Setup ExtentReports
        log.info("Setting up ExtentReports...");
        sparkReporter = new ExtentSparkReporter(REPORT_PATH);
        sparkReporter.config().setDocumentTitle(REPORT_TITLE);
        sparkReporter.config().setReportName(REPORT_NAME);
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Tester", "Nefedencova Victoria");

        log.info("ExtentReports setup complete.");
    }

    @Test
    public void executeDynamicTests() {
        // Dynamic list of test classes
        log.info("Starting dynamic test execution...");
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
            // Create TestNG instance and add class
            TestNG testNG = new TestNG();
            testNG.setTestClasses(new Class[]{testClass});
            testNG.run();

            // Log successful execution
            testSection.pass("All tests in " + testClass.getSimpleName() + " executed successfully.");
            log.info("Tests executed successfully for class: " + testClass.getSimpleName());
        } catch (Exception e) {
            // Log error
            testSection.fail("Error executing tests in " + testClass.getSimpleName() + ": " + e.getMessage());
            log.severe("Error executing tests in class " + testClass.getSimpleName() + ": " + e.getMessage());
        }
    }

    @AfterSuite
    public void generateExtentReport() {
        // Generate the report
        log.info("Generating ExtentReport...");
        extent.flush();

        // Open the report in the browser
        try {
            File reportFile = new File(REPORT_PATH);
            log.info("Attempting to open report at: " + reportFile.getAbsolutePath());
            if (reportFile.exists()) {
                Desktop.getDesktop().browse(reportFile.toURI());
                log.info("Report opened in the browser.");
            } else {
                log.warning("Report file does not exist at: " + reportFile.getAbsolutePath());
            }
        } catch (Exception e) {
            log.severe("Failed to open the report in the browser: " + e.getMessage());
        }
    }
}
