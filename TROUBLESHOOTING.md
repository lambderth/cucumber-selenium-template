# Troubleshooting Guide

## Common Issues and Solutions

### 1. Configuration Failure - 0 Tests Run

**Problem:**
```
Total tests run: 0, Passes: 0, Failures: 0, Skips: 0
Configuration Failures: 1, Skips: 0
```

**Cause:**
- Conflicting ExtentReports adapters in the runner configuration
- Manual ExtentReportManager calls before context initialization
- Incorrect adapter class name

**Solution:**
✅ **FIXED** - The runners now use the correct adapter:
```java
"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
```

✅ **FIXED** - Hooks class simplified to let the adapter handle reporting automatically

✅ **FIXED** - Screenshots are attached to Cucumber scenarios, which ExtentReports picks up automatically

### 2. ExtentReports Not Generated

**Problem:**
No ExtentReports are created after test execution.

**Solution:**
1. Verify `extent.properties` exists in `src/test/resources/`
2. Check that the runner has the ExtentReports adapter configured
3. Ensure the output directory has write permissions

**Verify Configuration:**
```properties
# src/test/resources/extent.properties
extent.reporter.spark.start=true
extent.reporter.spark.out=test-output/ExtentReports/Spark.html
```

### 3. WebDriver Initialization Failure

**Problem:**
Browser fails to start or throws exceptions.

**Solution:**
1. Update Maven dependencies: `mvn clean install -U`
2. Verify ChromeDriver/browser compatibility
3. Check that WebDriverManager can download drivers (internet connection)

### 4. Tests Not Found

**Problem:**
TestNG reports "No tests found" or scenarios are not executed.

**Solution:**
1. Verify feature files are in `src/test/resources/features/`
2. Check that tags in runner match tags in feature files
3. Ensure step definitions package is correct in `glue` configuration

**Example:**
```java
@CucumberOptions(
    features = "src/test/resources/features",  // ✓ Correct path
    glue = {"com.automation.stepdefinitions"}, // ✓ Correct package
    tags = "@GoogleSearch"                      // ✓ Tag exists in feature
)
```

### 5. Step Definitions Not Found

**Problem:**
```
Undefined step: Given the user is on the Google home page
```

**Solution:**
1. Verify step definitions are in `com.automation.stepdefinitions` package
2. Check that the `glue` option in runner includes the correct package
3. Ensure annotations match the Gherkin keywords (Given, When, Then, And)

### 6. Configuration File Not Found

**Problem:**
```
Failed to load configuration file: src/test/resources/config/config.properties
```

**Solution:**
1. Create the config directory: `src/test/resources/config/`
2. Add `config.properties` file with required properties
3. Verify file path is correct

**Minimum Required Properties:**
```properties
browser=chrome
base.url=https://www.google.com
```

### 7. Google Automation Detection

**Problem:**
Google shows "unusual traffic" or CAPTCHA challenges.

**Solution:**
✅ **IMPLEMENTED** - Stealth mode is already configured in DriverManager
- Automation flags disabled
- Custom user agent
- CDP commands to hide webdriver property

**Additional Tips:**
- Add delays between actions
- Don't run tests too frequently
- Randomize timing where possible

### 8. Screenshots Not in Reports

**Problem:**
ExtentReports don't show screenshots.

**Solution:**
✅ **FIXED** - Screenshots are automatically captured in Hooks and attached to scenarios

The ExtentReports adapter automatically picks up any screenshots attached to Cucumber scenarios.

### 9. Maven Build Failures

**Problem:**
```
Failed to execute goal on project
```

**Solution:**
1. Clean and rebuild:
   ```bash
   mvn clean install -U
   ```
2. Check Java version (requires Java 11+):
   ```bash
   java -version
   ```
3. Update Maven:
   ```bash
   mvn --version
   ```

### 10. Parallel Execution Issues

**Problem:**
Tests fail when running in parallel or interfere with each other.

**Solution:**
- Currently parallel execution is disabled (recommended for browser tests)
- Each test uses ThreadLocal for WebDriver isolation
- If enabling parallel, ensure test data doesn't overlap

**To enable parallel execution:**
```java
@DataProvider(parallel = true)  // Change to true
public Object[][] scenarios() {
    return super.scenarios();
}
```

## Debugging Tips

### Enable Verbose Logging

In `testng.xml`:
```xml
<suite name="Test Suite" verbose="2">  <!-- Increase to 2 or 3 -->
```

### Check Console Output

Look for these indicators:
```
✓ "Browser initialized: chrome"
✓ "Starting scenario: [scenario name]"
✓ "Finishing scenario: [scenario name]"
✓ "Status: PASSED"
```

### Verify Test Discovery

Run in dry-run mode:
```java
@CucumberOptions(
    dryRun = true  // Checks if all steps are defined
)
```

### Check Dependencies

Verify all dependencies are downloaded:
```bash
mvn dependency:tree
```

## Getting Help

### Check Files
1. **testng.xml** - TestNG suite configuration
2. **extent.properties** - ExtentReports configuration
3. **config.properties** - Framework configuration
4. **Feature files** - Test scenarios
5. **Step Definitions** - Step implementations
6. **Hooks.java** - Before/After scenario configuration

### Common Commands

```bash
# Clean and rebuild
mvn clean install

# Run tests
mvn clean test

# Run specific test
mvn clean test -Dtest=TestRunner

# Update dependencies
mvn clean install -U

# Skip tests (just build)
mvn clean install -DskipTests
```

## Configuration Checklist

Before running tests, verify:

- [ ] Java 11+ installed
- [ ] Maven 3.6+ installed
- [ ] Dependencies downloaded (`mvn clean install`)
- [ ] `config.properties` file exists
- [ ] `extent.properties` file exists
- [ ] Feature files have correct tags
- [ ] Step definitions match Gherkin steps
- [ ] Browser drivers can be downloaded (internet connection)
- [ ] Test output directories have write permissions

## Still Having Issues?

1. Check the console output for specific error messages
2. Review the stack trace for root cause
3. Verify all configuration files are present
4. Ensure dependencies are up to date
5. Check that file paths are correct (case-sensitive on Linux/Mac)

---

**Note:** Most configuration issues are resolved by running `mvn clean install -U` to refresh dependencies.

