package com.automation.framework.base;

import com.automation.framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage WebDriver creation and configuration
 */
public class DriverManager {
    
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * Gets the WebDriver instance for the current thread
     */
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    /**
     * Initializes WebDriver based on the specified browser
     * @param browser Browser name (chrome, firefox, edge)
     */
    public static void initializeDriver(String browser) {
        WebDriver webDriver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                
                // Basic browser options
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                
                // Stealth options to bypass automation detection
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                
                // Additional stealth options
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--dns-prefetch-disable");
                chromeOptions.addArguments("--disable-browser-side-navigation");
                
                // Set user agent to appear as a regular browser
                chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                
                // Modify navigator properties to hide webdriver
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                chromeOptions.setExperimentalOption("prefs", prefs);
                
                webDriver = new ChromeDriver(chromeOptions);
                
                // Execute CDP commands to further hide automation
                Map<String, Object> params = new HashMap<>();
                params.put("source", 
                    "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
                ((ChromeDriver) webDriver).executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
                
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                
                // Basic browser options
                firefoxOptions.addArguments("--start-maximized");
                
                // Stealth options for Firefox
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("dom.webdriver.enabled", false);
                profile.setPreference("useAutomationExtension", false);
                profile.setPreference("general.useragent.override", 
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0");
                firefoxOptions.setProfile(profile);
                
                webDriver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                
                // Basic browser options
                edgeOptions.addArguments("--start-maximized");
                
                // Stealth options for Edge (similar to Chrome)
                edgeOptions.addArguments("--disable-blink-features=AutomationControlled");
                edgeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                edgeOptions.setExperimentalOption("useAutomationExtension", false);
                edgeOptions.addArguments("--disable-dev-shm-usage");
                edgeOptions.addArguments("--no-sandbox");
                edgeOptions.addArguments("--disable-gpu");
                edgeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0");
                
                Map<String, Object> edgePrefs = new HashMap<>();
                edgePrefs.put("credentials_enable_service", false);
                edgePrefs.put("profile.password_manager_enabled", false);
                edgeOptions.setExperimentalOption("prefs", edgePrefs);
                
                webDriver = new EdgeDriver(edgeOptions);
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        
        // Configure timeouts from config.properties
        int implicitWait = ConfigReader.getImplicitWait();
        int pageLoadTimeout = ConfigReader.getPageLoadTimeout();
        
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        
        System.out.println("Timeouts configured - Implicit: " + implicitWait + "s, Page Load: " + pageLoadTimeout + "s");
        
        driver.set(webDriver);
    }
    
    /**
     * Quits the WebDriver and cleans up ThreadLocal
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}

