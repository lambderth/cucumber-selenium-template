package com.automation.stepdefinitions;

import com.automation.pages.GoogleHomePage;
import com.automation.pages.GoogleResultsPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

/**
 * Step Definitions for Google Search scenarios
 */
public class GoogleSearchSteps {
    
    private final TestContext testContext;
    private GoogleHomePage googleHomePage;
    private GoogleResultsPage googleResultsPage;
    
    /**
     * Constructor with dependency injection using PicoContainer
     */
    public GoogleSearchSteps(TestContext testContext) {
        this.testContext = testContext;
    }
    
    @Given("the user is on the Google home page")
    public void theUserIsOnTheGoogleHomePage() {
        googleHomePage = new GoogleHomePage(testContext.getDriver());
        googleHomePage.navigateToGoogle();
        Assert.assertTrue(googleHomePage.isPageLoaded(), 
            "Google page did not load correctly");
    }
    
    @When("the user searches for {string}")
    public void theUserSearchesFor(String searchTerm) {
        googleHomePage.searchFor(searchTerm);
        testContext.setSearchTerm(searchTerm);
    }
    
    @Then("search results are displayed")
    public void searchResultsAreDisplayed() {
        googleResultsPage = new GoogleResultsPage(testContext.getDriver());
        Assert.assertTrue(googleResultsPage.isPageLoaded(), 
            "Results page did not load correctly");
        Assert.assertTrue(googleResultsPage.hasResults(), 
            "No search results were found");
    }
    
    @And("the page title contains the search term")
    public void thePageTitleContainsTheSearchTerm() {
        googleResultsPage = new GoogleResultsPage(testContext.getDriver());
        String pageTitle = googleResultsPage.getSearchPageTitle();
        String searchTerm = testContext.getSearchTerm();
        Assert.assertTrue(pageTitle.contains(searchTerm), 
            "Page title does not contain the search term. " +
            "Expected: contains '" + searchTerm + "', Actual: '" + pageTitle + "'");
    }
    
    @And("the search term {string} appears in the search box")
    public void theSearchTermAppearsInTheSearchBox(String searchTerm) {
        googleResultsPage = new GoogleResultsPage(testContext.getDriver());
        Assert.assertTrue(googleResultsPage.isSearchTermDisplayed(searchTerm), 
            "Search term does not appear in the search box");
    }
}

