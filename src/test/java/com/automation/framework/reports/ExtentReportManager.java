package com.automation.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manager class for ExtentReports
 * Handles report initialization and configuration
 */
public class ExtentReportManager {
    
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    /**
     * Initializes ExtentReports
     */
    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }
    
    /**
     * Creates ExtentReports instance with configuration
     */
    private static ExtentReports createInstance() {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReports/ExtentReport_" + timestamp + ".html";
        
        // Create report directory if it doesn't exist
        File reportDir = new File(System.getProperty("user.dir") + "/test-output/ExtentReports");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
        sparkReporter = new ExtentSparkReporter(reportPath);
        
        // Configure Spark Reporter
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Selenium Cucumber Framework - Test Results");
        sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        sparkReporter.config().setEncoding("UTF-8");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Set system information
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("OS Version", System.getProperty("os.version"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Framework", "Selenium + Cucumber + TestNG");
        
        return extent;
    }
    
    /**
     * Gets the current test instance
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    /**
     * Sets the current test instance
     */
    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }
    
    /**
     * Creates a test in the report
     */
    public static ExtentTest createTest(String testName) {
        ExtentTest test = getInstance().createTest(testName);
        setTest(test);
        return test;
    }
    
    /**
     * Creates a test with description
     */
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        setTest(test);
        return test;
    }
    
    /**
     * Logs a message to the current test
     */
    public static void log(Status status, String message) {
        if (getTest() != null) {
            getTest().log(status, message);
        }
    }
    
    /**
     * Logs info message
     */
    public static void info(String message) {
        log(Status.INFO, message);
    }
    
    /**
     * Logs pass message
     */
    public static void pass(String message) {
        log(Status.PASS, message);
    }
    
    /**
     * Logs fail message
     */
    public static void fail(String message) {
        log(Status.FAIL, message);
    }
    
    /**
     * Logs skip message
     */
    public static void skip(String message) {
        log(Status.SKIP, message);
    }
    
    /**
     * Logs warning message
     */
    public static void warning(String message) {
        log(Status.WARNING, message);
    }
    
    /**
     * Adds a screenshot to the report
     */
    public static void addScreenshot(String base64Screenshot) {
        if (getTest() != null) {
            getTest().addScreenCaptureFromBase64String(base64Screenshot);
        }
    }
    
    /**
     * Adds a screenshot with title
     */
    public static void addScreenshot(String base64Screenshot, String title) {
        if (getTest() != null) {
            getTest().addScreenCaptureFromBase64String(base64Screenshot, title);
        }
    }
    
    /**
     * Flushes the report
     */
    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    /**
     * Removes the current test from ThreadLocal
     */
    public static void removeTest() {
        extentTest.remove();
    }
}

