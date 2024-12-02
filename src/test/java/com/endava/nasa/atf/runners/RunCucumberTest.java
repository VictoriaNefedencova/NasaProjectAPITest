package com.endava.nasa.atf.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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

    @BeforeClass
    public static void setup() {
        // Method executed before tests
        log.severe("Starting Cucumber tests...");
    }

    @AfterClass
    public static void tearDown() {
        // Automatically open the report in the browser after tests
        openReport();
    }

    private static void openReport() {
        File reportFile = new File(HTML_REPORT_PATH);

        if (Desktop.isDesktopSupported()) {
            try {
                if (reportFile.exists()) {
                    // Open the report in the default browser
                    Desktop.getDesktop().browse(reportFile.toURI());
                    log.severe("HTML report opened in the browser: " + HTML_REPORT_PATH);
                } else {
                    log.severe("HTML report not found at: " + HTML_REPORT_PATH);
                }
            } catch (IOException e) {
                log.severe("Error opening the HTML report: " + e.getMessage());
            }
        } else {
            log.severe("Desktop API is not supported on this system.");
        }
    }
}


