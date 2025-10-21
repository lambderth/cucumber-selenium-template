# Screenshot Configuration Guide

## 📸 Overview

The framework provides flexible screenshot configuration, allowing you to control when screenshots are captured during test execution.

## ⚙️ Configuration Options

Screenshots are configured in `src/test/resources/config/config.properties`:

```properties
# Screenshot configuration
take.screenshot.on.failure=true     # Capture screenshots when tests fail
take.screenshot.on.pass=false       # Capture screenshots when tests pass
screenshot.path=target/screenshots  # Directory to store screenshots
```

## 🎯 Configuration Flags

### 1. take.screenshot.on.failure

Controls whether screenshots are captured when a test fails.

**Values:**
- `true` - Capture screenshots on failures (default, recommended)
- `false` - Don't capture screenshots on failures

**Example:**
```properties
take.screenshot.on.failure=true
```

**Use Case:**
✅ Always recommended to be `true` for debugging failed tests.

### 2. take.screenshot.on.pass

Controls whether screenshots are captured when a test passes.

**Values:**
- `true` - Capture screenshots on passed tests
- `false` - Don't capture screenshots on passed tests (default)

**Example:**
```properties
take.screenshot.on.pass=false
```

**Use Cases:**
- ✅ Set to `false` to reduce report size and execution time
- ✅ Set to `true` for visual validation or documentation purposes
- ✅ Set to `true` when you need evidence of successful execution

### 3. screenshot.path

Defines the directory where screenshots are stored.

**Default:** `target/screenshots`

**Example:**
```properties
screenshot.path=test-output/screenshots
```

## 📋 Configuration Scenarios

### Scenario 1: Only Failed Tests (Default - Recommended)
```properties
take.screenshot.on.failure=true
take.screenshot.on.pass=false
```

**Benefits:**
- ✅ Smaller report files
- ✅ Faster test execution
- ✅ Focus on failures only
- ✅ Less storage required

**Best For:**
- Regular test runs
- CI/CD pipelines
- Quick feedback loops

### Scenario 2: All Tests (Pass and Fail)
```properties
take.screenshot.on.failure=true
take.screenshot.on.pass=true
```

**Benefits:**
- ✅ Complete visual documentation
- ✅ Evidence of successful executions
- ✅ Useful for compliance/auditing
- ✅ Visual validation of UI

**Best For:**
- Final regression testing
- Visual regression testing
- Documentation generation
- Client demonstrations
- Compliance requirements

### Scenario 3: No Screenshots
```properties
take.screenshot.on.failure=false
take.screenshot.on.pass=false
```

**Benefits:**
- ✅ Fastest execution
- ✅ Minimal storage
- ✅ Lightweight reports

**Best For:**
- Performance testing
- Quick smoke tests
- Development debugging
- When screenshots aren't needed

### Scenario 4: Only Passed Tests (Unusual)
```properties
take.screenshot.on.failure=false
take.screenshot.on.pass=true
```

**Benefits:**
- ✅ Documentation of successful flows
- ✅ Visual validation of expected behavior

**Best For:**
- Creating user guides
- Success path documentation
- Visual regression baselines

## 🔧 How It Works

### Implementation

The framework checks the configuration flags in the `Hooks` class after each scenario:

```java
@After
public void tearDown(Scenario scenario) {
    boolean shouldTakeScreenshot = false;
    
    if (scenario.isFailed() && ConfigReader.takeScreenshotOnFailure()) {
        shouldTakeScreenshot = true;
    } else if (!scenario.isFailed() && ConfigReader.takeScreenshotOnPass()) {
        shouldTakeScreenshot = true;
    }
    
    if (shouldTakeScreenshot) {
        // Capture and attach screenshot
    }
}
```

### ConfigReader Methods

```java
// Check if screenshots should be taken on failure
ConfigReader.takeScreenshotOnFailure()  // Returns true by default

// Check if screenshots should be taken on pass
ConfigReader.takeScreenshotOnPass()     // Returns false by default
```

## 📊 Impact Analysis

### Storage Impact

Assuming average screenshot size of 500KB:

| Configuration | Screenshots per 100 Tests (80% pass) | Storage Required |
|---------------|-------------------------------------|------------------|
| Only failures | 20 screenshots | ~10 MB |
| All tests | 100 screenshots | ~50 MB |
| No screenshots | 0 screenshots | 0 MB |

### Execution Time Impact

| Configuration | Time Impact |
|---------------|-------------|
| Only failures | +5-10 seconds |
| All tests | +20-30 seconds |
| No screenshots | 0 seconds |

*Note: Times vary based on test complexity and system performance*

## 🎨 Report Integration

### ExtentReports
Screenshots are automatically embedded in ExtentReports when attached to Cucumber scenarios.

**With Screenshots:**
- 📸 Visual evidence in reports
- 📊 Charts and statistics
- 🔍 Easy failure analysis

**Without Screenshots:**
- 📝 Text-based logs only
- 📊 Charts and statistics
- ⚡ Faster report loading

### Cucumber Reports
Screenshots appear in the standard Cucumber HTML reports as well.

## 💡 Best Practices

### 1. Default Configuration (Recommended)
```properties
take.screenshot.on.failure=true
take.screenshot.on.pass=false
```

**Why:**
- Provides essential debugging information
- Keeps report size manageable
- Good balance between information and performance

### 2. CI/CD Pipeline
```properties
take.screenshot.on.failure=true
take.screenshot.on.pass=false
```

**Why:**
- Focus on failures
- Faster pipeline execution
- Less artifact storage

### 3. Documentation Run
```properties
take.screenshot.on.failure=true
take.screenshot.on.pass=true
```

**Why:**
- Complete visual documentation
- Evidence of all test steps
- Useful for stakeholder reviews

### 4. Development/Debugging
```properties
take.screenshot.on.failure=true
take.screenshot.on.pass=true
```

**Why:**
- Maximum visibility
- Understand test flow
- Visual validation

## 🔄 Dynamic Configuration

You can override configuration at runtime using system properties:

```bash
mvn test -Dtake.screenshot.on.pass=true
```

*Note: This requires adding system property support in ConfigReader*

## 📝 Example Configurations

### Production Environment
```properties
# config.properties
browser=chrome
take.screenshot.on.failure=true
take.screenshot.on.pass=false
```

### QA Environment
```properties
# config.properties
browser=chrome
take.screenshot.on.failure=true
take.screenshot.on.pass=true
```

### Development Environment
```properties
# config.properties
browser=chrome
take.screenshot.on.failure=true
take.screenshot.on.pass=false
```

## 🐛 Troubleshooting

### Screenshots Not Appearing

**Problem:** Screenshots don't appear in reports

**Solutions:**
1. Check configuration:
   ```properties
   take.screenshot.on.failure=true
   ```

2. Verify WebDriver is not null in Hooks

3. Check console output for errors

4. Ensure screenshot directory has write permissions

### Too Many Screenshots

**Problem:** Reports are too large

**Solution:**
```properties
take.screenshot.on.pass=false  # Disable for passed tests
```

### Missing Failure Screenshots

**Problem:** No screenshots for failed tests

**Solution:**
```properties
take.screenshot.on.failure=true  # Must be true
```

## ✅ Verification

### Check Configuration Loading

Add this to your test setup to verify configuration:

```java
System.out.println("Screenshot on failure: " + ConfigReader.takeScreenshotOnFailure());
System.out.println("Screenshot on pass: " + ConfigReader.takeScreenshotOnPass());
```

### Console Output

When screenshots are captured, you'll see:
```
Screenshot captured for failed scenario: Test Scenario Name
```

When skipped:
```
Screenshot skipped (as per configuration) for passed scenario: Test Scenario Name
```

## 🎯 Summary

The screenshot configuration provides:
- ✅ Flexible control over when screenshots are captured
- ✅ Balance between information and performance
- ✅ Easy configuration via properties file
- ✅ Automatic integration with reports
- ✅ No code changes required

**Default Behavior:**
- ✅ Captures screenshots on failures
- ✅ Skips screenshots on passes
- ✅ Optimal for most use cases

**To Enable Screenshots on Passes:**
Simply change in `config.properties`:
```properties
take.screenshot.on.pass=true
```

---

**Remember:** Screenshots are automatically embedded in both Cucumber and ExtentReports!

