package com.endava.nasa.atf.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = RunCucumberTest.FEATURES_PATH, // Path to feature files
        glue = RunCucumberTest.STEP_DEFINITIONS,  // Package with step definitions
        plugin = {
                "pretty",                         // Console logging
                "html:" + RunCucumberTest.HTML_REPORT_PATH, // Generate HTML report
                "json:" + RunCucumberTest.JSON_REPORT_PATH  // Generate JSON report
        }
)
public class RunCucumberTest {

    private static final Logger log = Logger.getLogger(RunCucumberTest.class.getName());

    // Constants for paths
    public static final String FEATURES_PATH = "src/test/resources/features";
    public static final String STEP_DEFINITIONS = "com.endava.nasa.atf.steps";
    public static final String HTML_REPORT_PATH = "target/cucumber-html-report.html";
    public static final String JSON_REPORT_PATH = "target/cucumber.json";
    public static final String REPORT_OUTPUT_DIRECTORY = "target/cucumber-report";

    @BeforeClass
    public static void setup() {
        // Method executed before tests
        log.severe("Starting Cucumber tests...");
    }

    @AfterClass
    public static void tearDown() {
        // Automatically open the report in the browser after tests
        generateCucumberReport();
        openReport();
    }

    private static void openReport() {
        // Open the main Cucumber HTML report
        File reportFile = new File(HTML_REPORT_PATH);

        if (Desktop.isDesktopSupported()) {
            try {
                if (reportFile.exists()) {
                    // Open the main Cucumber report in the default browser
                    Desktop.getDesktop().browse(reportFile.toURI());
                    log.severe("HTML report opened in the browser: " + HTML_REPORT_PATH);
                } else {
                    log.severe("HTML report not found at: " + HTML_REPORT_PATH);
                }

                // Open the additional overview-features.html report
                File overviewReportFile = new File("C:/Users/vnefedencova/ProjectAV/Project-Demo/target/cucumber-report/cucumber-html-reports/overview-features.html");
                if (overviewReportFile.exists()) {
                    // Open the additional overview report in the default browser
                    Desktop.getDesktop().browse(overviewReportFile.toURI());
                    log.severe("Overview features report opened in the browser: " + overviewReportFile.getAbsolutePath());
                } else {
                    log.severe("Overview features report not found at: " + overviewReportFile.getAbsolutePath());
                }

            } catch (IOException e) {
                // Log any IOException that occurs while trying to open the reports
                log.severe("Error opening the report: " + e.getMessage());
            }
        } else {
            // If Desktop API is not supported, log a message
            log.severe("Desktop API is not supported on this system.");
        }
    }


    private static void generateCucumberReport() {
        // List to store JSON report file paths as strings
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add(JSON_REPORT_PATH); // Add the path to the JSON report

        // Set up the configuration for the report
        Configuration config = new Configuration(new File(REPORT_OUTPUT_DIRECTORY), "Cucumber Tests");

        // Generate the Cucumber report
        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, config);
        reportBuilder.generateReports();
        log.severe("Cucumber report generated at: " + REPORT_OUTPUT_DIRECTORY);
    }

}
