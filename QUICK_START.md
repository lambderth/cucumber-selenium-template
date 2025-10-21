# 🚀 Quick Start - Automation Framework

## ⚡ Initial Steps

### 1. Pre-requisites
Make sure you have installed:
- ☕ **Java JDK 11** or higher
- 📦 **Maven 3.6+**
- 🌐 **Google Chrome** (or Firefox/Edge)

### 2. Verify Installation
```bash
java -version
mvn -version
```

### 3. Download Dependencies
```bash
mvn clean install
```

## 🏃‍♂️ Running Tests

### Option 1: Using Maven (Recommended)
```bash
# Run all tests
mvn clean test

# Run Smoke Tests only
mvn clean test -Dcucumber.filter.tags="@Smoke"

# Run Regression Tests only
mvn clean test -Dcucumber.filter.tags="@Regression"
```

### Option 2: Using .bat Scripts (Windows)
```bash
# Run all tests
run-tests.bat

# Run Smoke Tests
run-smoke-tests.bat

# Run Regression Tests
run-regression-tests.bat
```

### Option 3: From Eclipse
1. Right-click on `testng.xml`
2. Select **Run As → TestNG Suite**

### Option 4: From IntelliJ IDEA
1. Right-click on `TestRunner.java`
2. Select **Run 'TestRunner'**

## 📊 View Reports

After running tests, you can view multiple report formats:

### Cucumber HTML Report
```
target/cucumber-reports/cucumber.html
```

### ExtentReports (Enhanced Reports with Screenshots)
```
test-output/ExtentReports/ExtentReport_[timestamp].html
```

**ExtentReports** provides:
- 📸 Embedded screenshots for all test scenarios
- 📊 Interactive charts and statistics
- 🎯 Step-by-step execution details
- 🏷️ Tag-based test categorization
- ⏱️ Execution timeline
- 💻 System information

## 🔧 Quick Configuration

### Change Browser
Edit `src/test/resources/config/config.properties`:
```properties
browser=chrome    # Options: chrome, firefox, edge
```

### Change Base URL
```properties
base.url=https://www.google.com
```

### Configure Screenshots
```properties
take.screenshot.on.failure=true    # Always capture on failures (recommended)
take.screenshot.on.pass=false      # Set to true to capture on passed tests too
```

### Configure Timeouts
```properties
implicit.wait=10          # WebDriver implicit wait (seconds)
explicit.wait=15          # Explicit wait for elements (seconds)
page.load.timeout=30      # Page load timeout (seconds)
```

## 📝 Your First Test Case

### 1. Create Feature File
`src/test/resources/features/MyTest.feature`
```gherkin
Feature: My first test

  @MyTest
  Scenario: Verify something
    Given I start my application
    When I do something
    Then I verify the result
```

### 2. Create Page Object
`src/test/java/com/automation/pages/MyPage.java`
```java
package com.automation.pages;

import com.automation.framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyPage extends BasePage {
    
    @FindBy(id = "myElement")
    private WebElement myElement;
    
    public MyPage(WebDriver driver) {
        super(driver);
    }
    
    public void clickElement() {
        clickElement(myElement);
    }
}
```

### 3. Create Step Definitions
`src/test/java/com/automation/stepdefinitions/MySteps.java`
```java
package com.automation.stepdefinitions;

import io.cucumber.java.en.*;

public class MySteps {
    
    private final TestContext testContext;
    
    public MySteps(TestContext testContext) {
        this.testContext = testContext;
    }
    
    @Given("I start my application")
    public void iStartMyApplication() {
        // Your code here
    }
    
    @When("I do something")
    public void iDoSomething() {
        // Your code here
    }
    
    @Then("I verify the result")
    public void iVerifyTheResult() {
        // Your code here
    }
}
```

## 🎯 Available Tags

- `@GoogleSearch` - Google search tests
- `@Smoke` - Smoke tests (critical)
- `@Regression` - Regression tests

## ❗ Common Troubleshooting

### Error: "WebDriver not found"
- The framework uses WebDriverManager, which automatically downloads drivers
- Check your internet connection

### Error: "Element not found"
- Increase timeout values in `config.properties`
- Verify locators in the Page Object

### Tests running slow
- Reduce `implicit.wait` and `explicit.wait` in `config.properties`
- Check your internet connection

### Compilation error
```bash
mvn clean install -U
```

## 📚 Important Files Structure

```
📁 Project
├── 📄 pom.xml                    → Maven dependencies
├── 📄 testng.xml                 → TestNG configuration
├── 📁 src/test/java/com/automation/
│   ├── 📁 framework/             → Framework base classes
│   ├── 📁 pages/                 → Page Objects
│   ├── 📁 runners/               → Test Runners
│   └── 📁 stepdefinitions/       → Step Definitions
└── 📁 src/test/resources/
    ├── 📁 config/                → Configuration files
    └── 📁 features/              → .feature files
```

## 💡 Useful Tips

1. **Use explicit waits** defined in `BasePage`
2. **Organize your Page Objects** by functionality
3. **Name your scenarios** descriptively
4. **Use tags** to organize your tests
5. **Review reports** after each execution

## 🆘 Need Help?

- Review the `README.md` for complete documentation
- Check the examples in `GoogleSearch.feature`
- Consult official documentation for Selenium, Cucumber, and TestNG

## ✅ Initial Checklist

- [ ] Java and Maven installed
- [ ] Dependencies downloaded (`mvn clean install`)
- [ ] Sample test executed successfully
- [ ] HTML report generated and reviewed
- [ ] Custom configuration (browser, URLs, etc.)

You're ready to start automating! 🎉
