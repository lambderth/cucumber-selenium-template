package com.automation.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for screenshot capture
 */
public class ScreenshotUtil {
    
    private static final String SCREENSHOT_DIR = "target/screenshots/";
    
    /**
     * Captures a screenshot and saves it to the screenshots directory
     * @param driver WebDriver instance
     * @param screenshotName Screenshot name
     * @return Screenshot file path
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            
            // Generate unique filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;
            
            // Capture screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            
            // Copy file
            Files.copy(srcFile.toPath(), destFile.toPath());
            
            System.out.println("Screenshot captured: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            System.err.println("Error capturing screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Captures a screenshot with a timestamp-based name
     * @param driver WebDriver instance
     * @return Screenshot file path
     */
    public static String captureScreenshot(WebDriver driver) {
        return captureScreenshot(driver, "screenshot");
    }
}

