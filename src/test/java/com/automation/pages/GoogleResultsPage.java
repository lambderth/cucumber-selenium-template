package com.automation.pages;

import com.automation.framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for Google Search Results Page
 */
public class GoogleResultsPage extends BasePage {
    
    // Page elements using PageFactory
    @FindBy(id = "search")
    private WebElement searchResultsContainer;
    
    @FindBy(xpath = "//div[@id='search']//h3")
    private List<WebElement> searchResultTitles;
    
    @FindBy(name = "q")
    private WebElement searchBox;
    
    @FindBy(id = "result-stats")
    private WebElement resultStats;
    
    /**
     * Constructor
     */
    public GoogleResultsPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Checks if the results page is loaded
     */
    public boolean isPageLoaded() {
        try {
            waitForElementToBeVisible(searchResultsContainer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the number of search results found
     */
    public int getNumberOfResults() {
        waitForElementToBeVisible(searchResultsContainer);
        return searchResultTitles.size();
    }
    
    /**
     * Checks if search results are present
     */
    public boolean hasResults() {
        return getNumberOfResults() > 0;
    }
    
    /**
     * Gets the text of the first search result
     */
    public String getFirstResultText() {
        if (searchResultTitles.size() > 0) {
            waitForElementToBeVisible(searchResultTitles.get(0));
            return searchResultTitles.get(0).getText();
        }
        return "";
    }
    
    /**
     * Checks if the search term appears in the search box
     */
    public boolean isSearchTermDisplayed(String searchTerm) {
        String actualSearchTerm = searchBox.getAttribute("value");
        return actualSearchTerm.contains(searchTerm);
    }
    
    /**
     * Gets the page title
     */
    public String getSearchPageTitle() {
        return getPageTitle();
    }
}

