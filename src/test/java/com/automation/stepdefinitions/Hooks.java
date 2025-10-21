package com.automation.stepdefinitions;

import com.automation.framework.base.DriverManager;
import com.automation.framework.config.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Hooks class to execute actions before and after each scenario
 */
public class Hooks {
    
    private final TestContext testContext;
    
    /**
     * Constructor with dependency injection
     */
    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }
    
    /**
     * Executes before each scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("========================================");
        System.out.println("Starting scenario: " + scenario.getName());
        System.out.println("Tags: " + scenario.getSourceTagNames());
        System.out.println("========================================");
        
        String browser = ConfigReader.getBrowser();
        DriverManager.initializeDriver(browser);
        testContext.setDriver(DriverManager.getDriver());
        
        System.out.println("Browser initialized: " + browser);
    }
    
    /**
     * Executes after each scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        // Determine if screenshot should be captured based on configuration
        boolean shouldTakeScreenshot = false;
        
        if (scenario.isFailed() && ConfigReader.takeScreenshotOnFailure()) {
            shouldTakeScreenshot = true;
        } else if (!scenario.isFailed() && ConfigReader.takeScreenshotOnPass()) {
            shouldTakeScreenshot = true;
        }
        
        // Capture screenshot if configured
        if (shouldTakeScreenshot) {
            try {
                byte[] screenshot = ((TakesScreenshot) testContext.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
                
                // Attach to Cucumber report (ExtentReports will automatically pick this up)
                String screenshotName = scenario.isFailed() ? 
                    "Failed - " + scenario.getName() : 
                    "Passed - " + scenario.getName();
                scenario.attach(screenshot, "image/png", screenshotName);
                
                System.out.println("Screenshot captured for " + 
                    (scenario.isFailed() ? "failed" : "passed") + 
                    " scenario: " + scenario.getName());
            } catch (Exception e) {
                System.out.println("Error capturing screenshot: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Screenshot skipped (as per configuration) for " + 
                (scenario.isFailed() ? "failed" : "passed") + 
                " scenario: " + scenario.getName());
        }
        
        System.out.println("========================================");
        System.out.println("Finishing scenario: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        System.out.println("========================================");
        
        // Close browser
        DriverManager.quitDriver();
    }
}

