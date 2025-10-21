package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runner to execute tests with @Smoke tag only
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.automation.stepdefinitions"},
    plugin = {
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    dryRun = false,
    tags = "@Smoke"
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

