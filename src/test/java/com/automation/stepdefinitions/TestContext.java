package com.automation.stepdefinitions;

import org.openqa.selenium.WebDriver;

/**
 * Context class to share data between Step Definitions
 * Used with PicoContainer for dependency injection
 */
public class TestContext {
    
    private WebDriver driver;
    private String searchTerm;
    
    /**
     * Gets the WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Sets the WebDriver instance
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Gets the search term
     */
    public String getSearchTerm() {
        return searchTerm;
    }
    
    /**
     * Sets the search term
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}

