# Automation Framework - Selenium + Cucumber + TestNG

This is a test automation framework built with **Java**, **Selenium WebDriver**, **Cucumber** and **TestNG**, using the **Page Object Model** pattern with **PageFactory**.

## 🚀 Technologies Used

- **Java 11**
- **Selenium WebDriver 4.15.0** - For web browser automation
- **Cucumber 7.14.0** - For BDD (Behavior Driven Development) testing
- **TestNG 7.8.0** - Testing framework
- **WebDriverManager 5.6.2** - Automatic browser driver management
- **ExtentReports 5.1.1** - Advanced HTML reporting with screenshots
- **Maven** - Dependency management and build tool
- **Log4j2** - Logging system

## 📁 Project Structure

```
cucumber-selenium-template/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│       ├── java/
│       │   └── com/automation/
│       │       ├── framework/
│       │       │   ├── base/
│       │       │   │   ├── BasePage.java
│       │       │   │   └── DriverManager.java
│       │       │   ├── config/
│       │       │   │   └── ConfigReader.java
│       │       │   ├── reports/
│       │       │   │   ├── ExtentReportManager.java
│       │       │   │   └── ExtentCucumberAdapter.java
│       │       │   └── utils/
│       │       │       ├── ScreenshotUtil.java
│       │       │       └── WaitUtil.java
│       │       ├── pages/
│       │       │   ├── GoogleHomePage.java
│       │       │   └── GoogleResultsPage.java
│       │       ├── runners/
│       │       │   ├── TestRunner.java
│       │       │   ├── SmokeTestRunner.java
│       │       │   └── RegressionTestRunner.java
│       │       └── stepdefinitions/
│       │           ├── GoogleSearchSteps.java
│       │           ├── Hooks.java
│       │           └── TestContext.java
│       └── resources/
│           ├── config/
│           │   └── config.properties
│           ├── features/
│           │   └── GoogleSearch.feature
│           ├── extent.properties
│           ├── extent-config.xml
│           └── log4j2.xml
├── pom.xml
├── testng.xml
└── README.md
```

## 🏗️ Framework Architecture

### Page Object Model (POM)
- **BasePage**: Base class with common methods for all pages
- **PageFactory**: Used to initialize web elements
- **Pages**: Classes representing each application page

### Cucumber BDD
- **Features**: .feature files with scenarios in Gherkin language
- **Step Definitions**: Implementation of steps defined in features
- **Hooks**: Configuration before and after each scenario

### TestNG Runner
- **TestRunner**: Executes all tests
- **SmokeTestRunner**: Executes tests with @Smoke tag only
- **RegressionTestRunner**: Executes tests with @Regression tag only

### Driver Management
- **DriverManager**: Manages WebDriver creation and destruction
- **ThreadLocal**: Support for parallel execution
- **WebDriverManager**: Automatic browser driver download

## ⚙️ Configuration

### config.properties File

```properties
browser=chrome                      # Options: chrome, firefox, edge
base.url=https://www.google.com

# Timeout settings (in seconds)
implicit.wait=10                    # WebDriver implicit wait
explicit.wait=15                    # WebDriverWait (used in BasePage)
page.load.timeout=30                # Page load timeout

# Screenshot configuration
take.screenshot.on.failure=true     # Capture screenshots on test failures
take.screenshot.on.pass=false       # Capture screenshots on test passes
```

### Supported Browsers
- Chrome (default)
- Firefox
- Edge

## 🚀 Running Tests

### Run all tests
```bash
mvn clean test
```

### Run with a specific browser
Modify the `config.properties` file or pass as parameter:
```bash
mvn clean test -Dbrowser=firefox
```

### Run Smoke Tests only
```bash
mvn clean test -Dcucumber.filter.tags="@Smoke"
```

### Run Regression Tests only
```bash
mvn clean test -Dcucumber.filter.tags="@Regression"
```

### Run a specific Runner from TestNG
Modify `testng.xml` to uncomment the desired runner and execute:
```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

## 📊 Reports

The framework uses **ExtentReports exclusively** for comprehensive test reporting.

### ExtentReports (Enhanced HTML Reports)
- **Location**: `test-output/ExtentReports/ExtentReport_[timestamp].html`
- **Retention**: Automatically keeps the last 10 reports (configurable)
- **Auto-cleanup**: Older reports are automatically deleted when limit is exceeded

**Features**:
- ✅ Beautiful and interactive HTML reports
- ✅ Screenshots embedded for all scenarios
- ✅ Pass/Fail statistics with charts
- ✅ Step-by-step execution details
- ✅ System information
- ✅ Execution timeline
- ✅ Tag-based categorization
- ✅ Timestamped reports for historical tracking
- ✅ Automatic cleanup of old reports

**Opening ExtentReports:**
After test execution, navigate to `test-output/ExtentReports/` and open the latest HTML file in your browser.

**Report Retention:**
```properties
extent.report.retention.count=10  # Keep last 10 reports (default)
```

## 📝 Example Test Case

The framework includes a sample test case that performs a Google search:

**Feature**: GoogleSearch.feature
```gherkin
Scenario: Perform a basic Google search
  Given the user is on the Google home page
  When the user searches for "Selenium WebDriver"
  Then search results are displayed
  And the page title contains the search term
```

## 🔧 Adding New Tests

### 1. Create a new Feature
Create a `.feature` file in `src/test/resources/features/`

### 2. Create the Page Object
Create a class in `src/test/java/com/automation/pages/` extending `BasePage`

```java
public class MyPage extends BasePage {
    
    @FindBy(id = "element")
    private WebElement element;
    
    public MyPage(WebDriver driver) {
        super(driver);
    }
    
    public void doSomething() {
        clickElement(element);
    }
}
```

### 3. Create Step Definitions
Create a class in `src/test/java/com/automation/stepdefinitions/`

```java
public class MySteps {
    
    private final TestContext testContext;
    
    public MySteps(TestContext testContext) {
        this.testContext = testContext;
    }
    
    @Given("some condition")
    public void someCondition() {
        // Implementation
    }
}
```

## 📦 Key Features

✅ Page Object Model with PageFactory  
✅ Cucumber BDD with Gherkin  
✅ TestNG as testing framework  
✅ Automatic WebDriver management  
✅ **Stealth mode to bypass automation detection**  
✅ Multi-browser support (Chrome, Firefox, Edge)  
✅ Centralized configuration  
✅ **ExtentReports with beautiful HTML reports** (exclusive reporter)  
✅ **Automatic report retention and cleanup**  
✅ **Timestamped reports for historical tracking**  
✅ **Screenshots embedded in reports** (configurable)  
✅ Logging system with Log4j2  
✅ Dependency injection with PicoContainer  
✅ Parallel execution support  
✅ Multiple runners (All, Smoke, Regression)  
✅ **Detailed step-by-step execution logs**

## 🎯 Implemented Best Practices

- Page Object Model for better maintainability
- PageFactory for element initialization
- Explicit waits for better stability
- **Stealth mode configurations to bypass bot detection**
- Separation of concerns (pages, steps, runners)
- Externalized configuration
- Structured logging
- Exception handling
- Screenshots on failures and success

## 📚 Additional Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [TestNG Documentation](https://testng.org/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [ExtentReports Documentation](https://extentreports.com/)

## 📖 Framework Guides

- **QUICK_START.md** - Quick start guide for running tests
- **EXTENTREPORTS_GUIDE.md** - Complete guide for ExtentReports usage
- **REPORT_MANAGEMENT.md** - Guide for report retention and automatic cleanup
- **STEALTH_MODE_GUIDE.md** - Guide for bypassing automation detection
- **SCREENSHOT_CONFIGURATION.md** - Guide for configuring screenshot capture
- **TROUBLESHOOTING.md** - Common issues and solutions

## 👨‍💻 Author

Framework developed as a base template for automation projects.

## 📄 License

This project is available for free use and modification according to team needs.
