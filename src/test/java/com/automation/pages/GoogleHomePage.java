package com.automation.pages;

import com.automation.framework.base.BasePage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Google Home Page
 */
public class GoogleHomePage extends BasePage {
    
    // Page elements using PageFactory
    @FindBy(name = "q")
    private WebElement searchBox;
    
    @FindBy(name = "btnK")
    private WebElement searchButton;
    
    @FindBy(xpath = "//input[@name='btnI']")
    private WebElement feelingLuckyButton;
    
    @FindBy(id = "logo")
    private WebElement googleLogo;
    
    /**
     * Constructor
     */
    public GoogleHomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigates to Google
     */
    public void navigateToGoogle() {
        navigateToUrl("https://www.google.com");
    }
    
    /**
     * Checks if Google page is loaded
     */
    public boolean isPageLoaded() {
        try {
            waitForElementToBeVisible(searchBox);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Performs a search by typing in the search box and pressing Enter
     */
    public void searchFor(String searchTerm) {
        waitForElementToBeVisible(searchBox);
        sendKeys(searchBox, searchTerm);
        searchBox.sendKeys(Keys.RETURN);
    }
    
    /**
     * Types in the search box without submitting
     */
    public void enterSearchTerm(String searchTerm) {
        sendKeys(searchBox, searchTerm);
    }
    
    /**
     * Clicks the search button
     */
    public void clickSearchButton() {
        clickElement(searchButton);
    }
    
    /**
     * Gets the placeholder text from the search box
     */
    public String getSearchBoxPlaceholder() {
        return searchBox.getAttribute("aria-label");
    }
}

