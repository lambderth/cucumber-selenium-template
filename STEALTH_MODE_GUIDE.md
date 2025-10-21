# Stealth Mode Configuration Guide

## 🕵️ Overview

The framework now includes **stealth configurations** to bypass automation detection mechanisms used by websites like Google. These configurations help make the automated browser appear more like a regular user browser.

## 🎯 What Was Changed

The `DriverManager.java` file has been enhanced with multiple stealth options for Chrome, Firefox, and Edge browsers.

## 🔧 Chrome Stealth Features

### 1. Disable Automation Flags
```java
chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
chromeOptions.setExperimentalOption("useAutomationExtension", false);
```
**Purpose:** Removes the automation indicators that websites detect.

### 2. Additional Browser Arguments
```java
chromeOptions.addArguments("--disable-dev-shm-usage");
chromeOptions.addArguments("--no-sandbox");
chromeOptions.addArguments("--disable-gpu");
chromeOptions.addArguments("--disable-extensions");
chromeOptions.addArguments("--dns-prefetch-disable");
chromeOptions.addArguments("--disable-browser-side-navigation");
```
**Purpose:** Improves browser stability and reduces detection vectors.

### 3. Custom User Agent
```java
chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
```
**Purpose:** Makes the browser appear as a standard Chrome browser.

### 4. Chrome DevTools Protocol (CDP) Commands
```java
Map<String, Object> params = new HashMap<>();
params.put("source", 
    "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
((ChromeDriver) webDriver).executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
```
**Purpose:** Removes the `navigator.webdriver` property that identifies automated browsers.

### 5. Browser Preferences
```java
Map<String, Object> prefs = new HashMap<>();
prefs.put("credentials_enable_service", false);
prefs.put("profile.password_manager_enabled", false);
chromeOptions.setExperimentalOption("prefs", prefs);
```
**Purpose:** Disables password saving prompts that can interfere with automation.

## 🦊 Firefox Stealth Features

### 1. Disable WebDriver Flag
```java
profile.setPreference("dom.webdriver.enabled", false);
profile.setPreference("useAutomationExtension", false);
```
**Purpose:** Disables the webdriver property in Firefox.

### 2. Custom User Agent
```java
profile.setPreference("general.useragent.override", 
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0");
```
**Purpose:** Sets a standard Firefox user agent.

## 🌐 Edge Stealth Features

Edge uses similar configurations to Chrome since both are Chromium-based:
- Disables automation flags
- Custom user agent
- Browser preferences
- Additional stability arguments

## 🚀 Usage

The stealth features are **automatically applied** when you initialize the browser. No additional configuration needed!

```java
// The stealth features are applied automatically
DriverManager.initializeDriver("chrome");
WebDriver driver = DriverManager.getDriver();

// Now browse normally - the stealth features are active
driver.get("https://www.google.com");
```

## 🔍 How It Works

### Detection Methods Bypassed

1. **navigator.webdriver Property**
   - Normal: `navigator.webdriver === true`
   - Stealth: `navigator.webdriver === undefined`

2. **Chrome Automation Extension**
   - Normal: Extension present in `navigator.plugins`
   - Stealth: Extension excluded

3. **User Agent String**
   - Normal: May contain automation indicators
   - Stealth: Standard browser user agent

4. **Window Properties**
   - Normal: `window.navigator.webdriver` visible
   - Stealth: Property redefined to be invisible

## ⚠️ Important Notes

### 1. Not 100% Foolproof
While these configurations significantly reduce detection, sophisticated anti-bot systems may still detect automation through:
- Mouse movement patterns
- Timing analysis
- Browser fingerprinting
- Behavioral analysis

### 2. Performance Impact
Some options (like `--no-sandbox`) may reduce browser security. These are acceptable for testing environments but should be used cautiously.

### 3. Google's Policies
Remember that bypassing automation detection to scrape content may violate Google's Terms of Service. Use responsibly and primarily for legitimate testing purposes.

## 🎯 Best Practices

### 1. Add Delays
Simulate human behavior with random delays:
```java
Thread.sleep(1000 + (long)(Math.random() * 2000)); // 1-3 seconds
```

### 2. Randomize Actions
Vary your test patterns to appear more human-like.

### 3. Use Realistic User Agents
Keep user agents updated to match current browser versions.

### 4. Handle CAPTCHAs Gracefully
If CAPTCHAs appear, consider:
- Implementing manual intervention points
- Using CAPTCHA solving services (for legitimate use cases)
- Reducing test frequency

## 🔧 Customization

### Change User Agent
Edit `DriverManager.java` and modify the user-agent string:
```java
chromeOptions.addArguments("user-agent=YOUR_CUSTOM_USER_AGENT");
```

### Add More Stealth Options
You can add additional Chrome arguments:
```java
chromeOptions.addArguments("--disable-web-security");
chromeOptions.addArguments("--allow-running-insecure-content");
```

### Disable Stealth Features
To disable stealth features, simply comment out the relevant lines in `DriverManager.java`.

## 📊 Verification

### Test if Stealth is Working

Create a test to verify the stealth configuration:

```java
@Test
public void verifyStealthMode() {
    driver.get("chrome://version/");
    // Check if automation flags are hidden
    
    driver.get("https://bot.sannysoft.com/");
    // This site tests for automation detection
    
    Object webdriverProperty = ((JavascriptExecutor) driver)
        .executeScript("return navigator.webdriver");
    
    Assert.assertNull(webdriverProperty, "navigator.webdriver should be undefined");
}
```

### Useful Test Sites
- **https://bot.sannysoft.com/** - Comprehensive bot detection tests
- **https://arh.antoinevastel.com/bots/areyouheadless** - Headless browser detection
- **https://amiunique.org/** - Browser fingerprinting analysis

## 🛡️ Detection Techniques Addressed

| Detection Method | Status | How Addressed |
|-----------------|--------|---------------|
| navigator.webdriver | ✅ Bypassed | CDP command to redefine property |
| Chrome automation flags | ✅ Bypassed | Disabled via options |
| User agent analysis | ✅ Bypassed | Custom user agent set |
| Chrome automation extension | ✅ Bypassed | Extension excluded |
| WebDriver property | ✅ Bypassed | Disabled in preferences |
| Browser fingerprinting | ⚠️ Partially | Some fingerprinting still possible |
| Behavioral analysis | ❌ Not addressed | Requires human-like behavior implementation |

## 📈 Success Rate

With these stealth configurations:
- ✅ Google searches work normally
- ✅ Most basic automation detection is bypassed
- ✅ Standard web browsing appears more natural
- ⚠️ Advanced anti-bot systems may still detect
- ⚠️ Machine learning-based detection requires additional measures

## 🔄 Maintenance

### Keeping Up-to-Date

1. **Update User Agents Regularly**
   - Chrome/Edge/Firefox release new versions frequently
   - Update user agent strings to match current versions

2. **Monitor Browser Changes**
   - New versions may add/remove flags
   - Test stealth features after browser updates

3. **Check Detection Methods**
   - Anti-bot systems evolve
   - Periodically test against detection sites

## ⚖️ Legal and Ethical Considerations

### Acceptable Use Cases
✅ Testing your own websites  
✅ Automated testing with permission  
✅ Internal QA processes  
✅ Load testing authorized systems  

### Problematic Use Cases
❌ Scraping content without permission  
❌ Bypassing rate limits maliciously  
❌ Automated account creation fraud  
❌ Circumventing security measures illegally  

## 🆘 Troubleshooting

### Still Detected?

1. **Check Browser Version**
   ```bash
   # Update ChromeDriver
   mvn clean install -U
   ```

2. **Verify CDP Commands Work**
   ```java
   // Test if CDP is working
   Map<String, Object> result = ((ChromeDriver) driver)
       .executeCdpCommand("Page.getNavigationHistory", new HashMap<>());
   System.out.println(result);
   ```

3. **Test on Detection Sites**
   Visit bot detection sites to see what's being detected.

4. **Add More Delays**
   Increase waits between actions to appear more human.

### Error: CDP Command Failed

If CDP commands fail:
- Ensure you're using ChromeDriver that matches your Chrome version
- Check that Chrome is not running in headless mode
- Verify Chrome version compatibility

## 📚 Additional Resources

- [Chrome DevTools Protocol Documentation](https://chromedevtools.github.io/devtools-protocol/)
- [Selenium Stealth GitHub](https://github.com/diprajpatra/selenium-stealth)
- [Bot Detection Techniques](https://antoinevastel.com/bot%20detection/2019/07/19/detecting-chrome-headless-v3.html)

## ✅ Summary

The framework now includes comprehensive stealth features that:
- ✅ Hide automation indicators
- ✅ Use realistic user agents
- ✅ Disable automation flags
- ✅ Remove webdriver properties
- ✅ Work across Chrome, Firefox, and Edge

These features are **automatically applied** - just use the framework normally and the stealth configurations will be active!

---

**Remember:** Use these features responsibly and in compliance with website Terms of Service and applicable laws.

