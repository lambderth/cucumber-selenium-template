# ExtentReports Integration Guide

This framework includes **ExtentReports 5.1.1** integrated with Cucumber for enhanced HTML reporting.

## 📋 What is ExtentReports?

ExtentReports is a popular HTML reporting library that generates beautiful, interactive, and detailed test execution reports with:
- 📸 Embedded screenshots
- 📊 Interactive charts and graphs
- 🎯 Step-by-step execution logs
- 🏷️ Tag-based test categorization
- ⏱️ Execution timeline
- 💻 System information

## 🔧 Configuration Files

### 1. extent.properties
Location: `src/test/resources/extent.properties`

Configures the ExtentReports output and features:
```properties
extent.reporter.spark.start=true
extent.reporter.spark.out=test-output/SparkReport/ExtentReport.html
extent.reporter.spark.config=src/test/resources/extent-config.xml
extent.reporter.spark.base64imagesrc=true
screenshot.dir=test-output/screenshots/
screenshot.rel.path=../screenshots/
```

### 2. extent-config.xml
Location: `src/test/resources/extent-config.xml`

Customizes the report appearance and settings:
- Report theme (STANDARD or DARK)
- Report title and name
- Timestamp format
- Custom CSS and JavaScript

## 📁 ExtentReports Classes

### 1. ExtentReportManager
**Location:** `src/test/java/com/automation/framework/reports/ExtentReportManager.java`

**Purpose:** Manages ExtentReports instance and provides utility methods.

**Key Methods:**
- `getInstance()` - Gets or creates ExtentReports instance
- `createTest(String name)` - Creates a new test
- `log(Status, String)` - Logs a message with status
- `info(String)` - Logs info message
- `pass(String)` - Logs pass message
- `fail(String)` - Logs fail message
- `addScreenshot(String)` - Adds screenshot to report
- `flush()` - Writes the report to file

**Usage Example:**
```java
ExtentReportManager.info("Starting test execution");
ExtentReportManager.pass("Test step passed successfully");
ExtentReportManager.addScreenshot(base64Screenshot);
```

### 2. ExtentCucumberAdapter
**Location:** `src/test/java/com/automation/framework/reports/ExtentCucumberAdapter.java`

**Purpose:** Cucumber plugin that listens to test events and updates ExtentReports.

**Features:**
- Automatically creates tests for scenarios
- Logs step execution
- Captures test status (pass/fail/skip)
- Adds tags to tests
- Handles errors and exceptions

## 🎯 Integration Points

### 1. Hooks Class
**Location:** `src/test/java/com/automation/stepdefinitions/Hooks.java`

The Hooks class integrates ExtentReports in the test lifecycle:

**@Before Hook:**
- Logs scenario start
- Logs browser initialization
- Records scenario tags

**@After Hook:**
- Captures screenshot (pass or fail)
- Attaches screenshot to report
- Logs final scenario status
- Records execution details

### 2. Test Runners
All test runners include ExtentReports plugin in the @CucumberOptions:

```java
@CucumberOptions(
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml",
        "com.automation.framework.reports.ExtentCucumberAdapter:",
        "tech.grasshopper.extent.ExtentCucumberAdapter:"
    }
)
```

## 📊 Report Generation

### Automatic Generation
ExtentReports are automatically generated when tests run:
1. Reports are created in `test-output/ExtentReports/`
2. Each report has a timestamp: `ExtentReport_yyyy.MM.dd.HH.mm.ss.html`
3. Screenshots are embedded as base64 images

### Report Location
```
test-output/
├── ExtentReports/
│   ├── ExtentReport_2024.01.15.14.30.45.html
│   ├── ExtentReport_2024.01.15.15.20.10.html
│   └── ...
└── screenshots/
    └── ... (if configured)
```

## 🎨 Report Features

### Dashboard View
- Total tests executed
- Pass/Fail/Skip statistics
- Execution time
- Pass percentage
- Interactive pie and bar charts

### Test Details
- Scenario name and description
- Tags assigned to the scenario
- Step-by-step execution log
- Screenshots at each step
- Error messages and stack traces (for failures)
- Execution time for each step

### System Information
- Operating System
- OS Version
- Java Version
- User Name
- Framework details

### Filters and Search
- Filter by status (Passed/Failed/Skipped)
- Search by test name
- Filter by tags/categories
- Sort by various criteria

## 🔍 Using ExtentReports in Tests

### Logging from Step Definitions

You can add custom logs from your step definitions:

```java
import com.automation.framework.reports.ExtentReportManager;

public class MySteps {
    
    @Given("some step")
    public void someStep() {
        ExtentReportManager.info("Executing some step");
        
        // Your test code
        
        ExtentReportManager.pass("Step executed successfully");
    }
    
    @When("another step")
    public void anotherStep() {
        try {
            // Your test code
            ExtentReportManager.info("Processing data...");
        } catch (Exception e) {
            ExtentReportManager.fail("Step failed: " + e.getMessage());
            throw e;
        }
    }
}
```

### Adding Screenshots

Screenshots are automatically added in the Hooks class, but you can also add them manually:

```java
// Capture screenshot
String base64Screenshot = ((TakesScreenshot) driver)
    .getScreenshotAs(OutputType.BASE64);

// Add to report
ExtentReportManager.addScreenshot(base64Screenshot, "Custom Screenshot");
```

### Logging Levels

```java
ExtentReportManager.info("Information message");
ExtentReportManager.pass("Success message");
ExtentReportManager.fail("Failure message");
ExtentReportManager.skip("Skipped message");
ExtentReportManager.warning("Warning message");
```

## 🎯 Best Practices

1. **Use Descriptive Messages**
   ```java
   ExtentReportManager.info("Navigating to login page");
   ExtentReportManager.pass("User logged in successfully with username: " + username);
   ```

2. **Add Screenshots at Key Points**
   - Before important actions
   - After validations
   - On errors (automatic)

3. **Log Important Data**
   ```java
   ExtentReportManager.info("Test data: User ID = " + userId);
   ExtentReportManager.info("Expected result: " + expected);
   ExtentReportManager.info("Actual result: " + actual);
   ```

4. **Keep Logs Concise**
   - Don't over-log trivial steps
   - Focus on important actions and validations

## 🔧 Customization

### Change Report Theme
Edit `src/test/resources/extent-config.xml`:
```xml
<theme>DARK</theme>  <!-- or STANDARD -->
```

### Change Report Title
Edit `src/test/resources/extent-config.xml`:
```xml
<reportName>Your Custom Report Name</reportName>
<documentTitle>Your Custom Title</documentTitle>
```

### Change Report Location
Edit `src/test/java/com/automation/framework/reports/ExtentReportManager.java`:
```java
String reportPath = "your/custom/path/ExtentReport_" + timestamp + ".html";
```

### Add Custom System Info
Edit `ExtentReportManager.java` in the `createInstance()` method:
```java
extent.setSystemInfo("Environment", "QA");
extent.setSystemInfo("Build", "1.0.0");
extent.setSystemInfo("Browser", ConfigReader.getBrowser());
```

## 📈 Report Metrics

ExtentReports automatically tracks:
- ✅ Total tests executed
- ✅ Pass/Fail/Skip counts
- ✅ Pass percentage
- ✅ Execution time per test
- ✅ Total execution time
- ✅ Error details and stack traces

## 🆘 Troubleshooting

### Report Not Generated
1. Check if tests are running successfully
2. Verify `extent.properties` file exists
3. Check write permissions on `test-output/` directory

### Screenshots Not Appearing
1. Verify screenshot capture in Hooks
2. Check base64 encoding
3. Ensure driver is not null

### Report Shows Wrong Information
1. Clear `test-output/` directory
2. Run tests again
3. Check ExtentReportManager implementation

## 📚 Additional Resources

- [ExtentReports Official Documentation](https://extentreports.com/docs/versions/5/java/index.html)
- [Grasshopper ExtentReports Cucumber Adapter](https://github.com/grasshopper7/cucumber-7-extent-adapter)

## ✅ Verification Checklist

After running tests, verify:
- [ ] ExtentReport HTML file created in `test-output/ExtentReports/`
- [ ] Report opens successfully in browser
- [ ] All scenarios are listed
- [ ] Screenshots are embedded
- [ ] Pass/Fail status is correct
- [ ] System information is displayed
- [ ] Tags are visible on tests
- [ ] Step details are shown

---

**Note:** ExtentReports work alongside standard Cucumber reports. Both report types are generated automatically during test execution.

