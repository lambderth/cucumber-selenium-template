package com.automation.framework.listeners;

import com.automation.framework.config.ConfigReader;
import com.automation.framework.reports.ReportManager;
import org.testng.IExecutionListener;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG listener to manage ExtentReports lifecycle
 * - Cleans up old reports before test execution
 * - Renames report with timestamp after test execution
 */
public class ReportCleanupListener implements IExecutionListener {
    
    private static final String TEMP_REPORT_NAME = "ExtentReport.html";
    
    /**
     * Executes before test suite starts
     * Cleans up old reports based on retention policy
     */
    @Override
    public void onExecutionStart() {
        System.out.println("========================================");
        System.out.println("ExtentReports Cleanup - Starting");
        System.out.println("========================================");
        
        // Run cleanup of old reports
        ReportManager.cleanupOldReports();
        
        int reportCount = ReportManager.getReportCount();
        int retentionLimit = ConfigReader.getExtentReportRetentionCount();
        
        System.out.println("Current report count: " + reportCount);
        System.out.println("Retention limit: " + retentionLimit);
        System.out.println("========================================");
    }
    
    /**
     * Executes after test suite completes
     * Renames the generated report with timestamp
     */
    @Override
    public void onExecutionFinish() {
        System.out.println("========================================");
        System.out.println("ExtentReports Post-Processing - Starting");
        System.out.println("========================================");
        
        try {
            String reportDir = ConfigReader.getExtentReportPath();
            File sourceFile = new File(reportDir + File.separator + TEMP_REPORT_NAME);
            
            if (!sourceFile.exists()) {
                System.out.println("No ExtentReport found to rename at: " + sourceFile.getAbsolutePath());
                return;
            }
            
            // Create timestamped filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String newFileName = "ExtentReport_" + timestamp + ".html";
            File targetFile = new File(reportDir + File.separator + newFileName);
            
            // Rename the file
            Files.move(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("ExtentReport renamed successfully:");
            System.out.println("  From: " + TEMP_REPORT_NAME);
            System.out.println("  To:   " + newFileName);
            System.out.println("  Location: " + targetFile.getAbsolutePath());
            
            // Show report summary
            int totalReports = ReportManager.getReportCount();
            System.out.println("Total ExtentReports stored: " + totalReports);
            
        } catch (Exception e) {
            System.err.println("Error renaming ExtentReport: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("========================================");
        System.out.println("ExtentReports Post-Processing - Completed");
        System.out.println("========================================");
    }
}

