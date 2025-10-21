package com.automation.framework.base;

import com.automation.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base class for all Page Objects
 * Contains common methods and utilities for pages
 */
public class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    /**
     * Constructor that initializes PageFactory and WebDriverWait
     * Explicit wait timeout is loaded from config.properties
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        int explicitWait = ConfigReader.getExplicitWait();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Waits for an element to be visible
     */
    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Waits for an element to be clickable
     */
    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Clicks an element after waiting for it to be clickable
     */
    protected void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }
    
    /**
     * Sends text to an element after waiting for it to be visible
     */
    protected void sendKeys(WebElement element, String text) {
        waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Gets text from an element after waiting for it to be visible
     */
    protected String getText(WebElement element) {
        waitForElementToBeVisible(element);
        return element.getText();
    }
    
    /**
     * Navigates to a URL
     */
    protected void navigateToUrl(String url) {
        driver.get(url);
    }
    
    /**
     * Gets the current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Gets the current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}

