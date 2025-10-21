package com.automation.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class to read configuration properties
 */
public class ConfigReader {
    
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";
    
    static {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE_PATH);
        }
    }
    
    /**
     * Gets a property from the configuration file
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Gets the configured browser
     */
    public static String getBrowser() {
        String browser = properties.getProperty("browser");
        return browser != null ? browser : "chrome";
    }
    
    /**
     * Gets the configured base URL
     */
    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }
    
    /**
     * Checks if screenshots should be taken on test failure
     */
    public static boolean takeScreenshotOnFailure() {
        String value = properties.getProperty("take.screenshot.on.failure");
        return value == null || Boolean.parseBoolean(value); // Default to true
    }
    
    /**
     * Checks if screenshots should be taken on test pass
     */
    public static boolean takeScreenshotOnPass() {
        String value = properties.getProperty("take.screenshot.on.pass");
        return value != null && Boolean.parseBoolean(value); // Default to false
    }
    
    /**
     * Gets the screenshot directory path
     */
    public static String getScreenshotPath() {
        String path = properties.getProperty("screenshot.path");
        return path != null ? path : "target/screenshots";
    }
    
    /**
     * Gets the ExtentReports output directory path
     */
    public static String getExtentReportPath() {
        String path = properties.getProperty("extent.report.path");
        return path != null ? path : "test-output/ExtentReports";
    }
    
    /**
     * Gets the number of ExtentReports to retain
     */
    public static int getExtentReportRetentionCount() {
        String value = properties.getProperty("extent.report.retention.count");
        try {
            return value != null ? Integer.parseInt(value) : 10; // Default to 10
        } catch (NumberFormatException e) {
            System.out.println("Invalid retention count, using default: 10");
            return 10;
        }
    }
    
    /**
     * Gets the implicit wait timeout in seconds
     */
    public static int getImplicitWait() {
        String value = properties.getProperty("implicit.wait");
        try {
            return value != null ? Integer.parseInt(value) : 10; // Default to 10 seconds
        } catch (NumberFormatException e) {
            System.out.println("Invalid implicit wait value, using default: 10 seconds");
            return 10;
        }
    }
    
    /**
     * Gets the explicit wait timeout in seconds
     */
    public static int getExplicitWait() {
        String value = properties.getProperty("explicit.wait");
        try {
            return value != null ? Integer.parseInt(value) : 15; // Default to 15 seconds
        } catch (NumberFormatException e) {
            System.out.println("Invalid explicit wait value, using default: 15 seconds");
            return 15;
        }
    }
    
    /**
     * Gets the page load timeout in seconds
     */
    public static int getPageLoadTimeout() {
        String value = properties.getProperty("page.load.timeout");
        try {
            return value != null ? Integer.parseInt(value) : 30; // Default to 30 seconds
        } catch (NumberFormatException e) {
            System.out.println("Invalid page load timeout value, using default: 30 seconds");
            return 30;
        }
    }
}

