package com.automation.framework.reports;

import com.automation.framework.config.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages ExtentReports files including cleanup and organization
 */
public class ReportManager {
    
    /**
     * Creates a timestamped report filename
     */
    public static String getTimestampedReportName() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        return "ExtentReport_" + timestamp + ".html";
    }
    
    /**
     * Gets the full path for the ExtentReport output
     */
    public static String getReportPath() {
        String reportDir = ConfigReader.getExtentReportPath();
        String reportName = getTimestampedReportName();
        return reportDir + File.separator + reportName;
    }
    
    /**
     * Cleans up old ExtentReports based on retention count
     * Keeps only the most recent N reports as specified in configuration
     */
    public static void cleanupOldReports() {
        try {
            String reportDir = ConfigReader.getExtentReportPath();
            int retentionCount = ConfigReader.getExtentReportRetentionCount();
            
            File directory = new File(reportDir);
            
            // Create directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("Created ExtentReports directory: " + reportDir);
                return;
            }
            
            // Get all HTML files in the reports directory
            File[] reportFiles = directory.listFiles((dir, name) -> 
                name.startsWith("ExtentReport_") && name.endsWith(".html"));
            
            if (reportFiles == null || reportFiles.length <= retentionCount) {
                System.out.println("Report cleanup: " + (reportFiles != null ? reportFiles.length : 0) + 
                    " reports found, retention limit is " + retentionCount + ". No cleanup needed.");
                return;
            }
            
            // Sort files by creation time (oldest first)
            List<File> sortedFiles = Arrays.stream(reportFiles)
                .sorted(Comparator.comparingLong(ReportManager::getFileCreationTime))
                .collect(Collectors.toList());
            
            // Calculate how many files to delete
            int filesToDelete = sortedFiles.size() - retentionCount;
            
            System.out.println("Report cleanup: Found " + sortedFiles.size() + 
                " reports, retention limit is " + retentionCount + 
                ". Deleting " + filesToDelete + " oldest report(s)...");
            
            // Delete oldest files
            int deletedCount = 0;
            for (int i = 0; i < filesToDelete; i++) {
                File fileToDelete = sortedFiles.get(i);
                if (fileToDelete.delete()) {
                    System.out.println("Deleted old report: " + fileToDelete.getName());
                    deletedCount++;
                } else {
                    System.out.println("Failed to delete: " + fileToDelete.getName());
                }
            }
            
            System.out.println("Report cleanup completed: " + deletedCount + " file(s) deleted, " + 
                (sortedFiles.size() - deletedCount) + " file(s) retained.");
            
        } catch (Exception e) {
            System.err.println("Error during report cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the creation time of a file
     */
    private static long getFileCreationTime(File file) {
        try {
            Path path = Paths.get(file.getAbsolutePath());
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            return attrs.creationTime().toMillis();
        } catch (IOException e) {
            // If we can't get creation time, use last modified time
            return file.lastModified();
        }
    }
    
    /**
     * Lists all ExtentReports in the reports directory
     */
    public static List<String> listAllReports() {
        List<String> reports = new ArrayList<>();
        try {
            String reportDir = ConfigReader.getExtentReportPath();
            File directory = new File(reportDir);
            
            if (!directory.exists()) {
                return reports;
            }
            
            File[] reportFiles = directory.listFiles((dir, name) -> 
                name.startsWith("ExtentReport_") && name.endsWith(".html"));
            
            if (reportFiles != null) {
                Arrays.stream(reportFiles)
                    .sorted(Comparator.comparingLong(ReportManager::getFileCreationTime).reversed())
                    .forEach(file -> reports.add(file.getName()));
            }
        } catch (Exception e) {
            System.err.println("Error listing reports: " + e.getMessage());
        }
        return reports;
    }
    
    /**
     * Gets the count of existing reports
     */
    public static int getReportCount() {
        String reportDir = ConfigReader.getExtentReportPath();
        File directory = new File(reportDir);
        
        if (!directory.exists()) {
            return 0;
        }
        
        File[] reportFiles = directory.listFiles((dir, name) -> 
            name.startsWith("ExtentReport_") && name.endsWith(".html"));
        
        return reportFiles != null ? reportFiles.length : 0;
    }
}

