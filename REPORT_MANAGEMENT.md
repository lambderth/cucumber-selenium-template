## 📊 Report Management Guide

## Overview

The framework uses **ExtentReports exclusively** for test reporting, with automatic report management including timestamping and cleanup of old reports.

## 🎯 Key Features

### 1. Timestamped Reports
Every test execution generates a uniquely named report with timestamp:
```
ExtentReport_2024-01-15_14-30-45.html
ExtentReport_2024-01-15_15-22-10.html
ExtentReport_2024-01-16_09-15-33.html
```

**Benefits:**
- ✅ Historical tracking of test executions
- ✅ Easy comparison between runs
- ✅ No report overwriting
- ✅ Clear execution timeline

### 2. Automatic Report Cleanup
The framework automatically maintains a clean reports directory by:
- Keeping only the N most recent reports (default: 10)
- Deleting oldest reports when limit is exceeded
- Running cleanup before each test execution

**Benefits:**
- ✅ Prevents disk space issues
- ✅ Keeps reports directory organized
- ✅ Maintains relevant history
- ✅ No manual cleanup needed

### 3. Single Reporter
Only ExtentReports is used (Cucumber HTML/JSON/XML reporters are disabled):
- ✅ Faster test execution
- ✅ Smaller artifact size
- ✅ Consistent reporting format
- ✅ Reduced complexity

## ⚙️ Configuration

### Report Retention Count

Configure in `src/test/resources/config/config.properties`:

```properties
# Reports configuration
extent.report.path=test-output/ExtentReports
extent.report.retention.count=10
```

**Options:**

| Value | Behavior | Use Case |
|-------|----------|----------|
| `10` | Keep last 10 reports (default) | Regular testing |
| `5` | Keep last 5 reports | Limited disk space |
| `20` | Keep last 20 reports | Extensive history tracking |
| `1` | Keep only latest report | Minimal storage |
| `50` | Keep last 50 reports | Long-term tracking |

### Report Directory

```properties
extent.report.path=test-output/ExtentReports
```

**Default:** `test-output/ExtentReports`

You can change this to any valid path:
```properties
extent.report.path=reports/extent
extent.report.path=target/test-reports
```

## 🔄 How It Works

### Lifecycle

1. **Before Test Execution (Cleanup Phase)**
   ```
   - ReportCleanupListener.onExecutionStart() is triggered
   - Checks current report count
   - If count > retention limit:
     - Identifies oldest reports
     - Deletes excess reports
     - Logs cleanup activity
   ```

2. **During Test Execution**
   ```
   - ExtentReports adapter generates report
   - Temporary file: ExtentReport.html
   - Screenshots are captured and embedded
   ```

3. **After Test Execution (Post-Processing Phase)**
   ```
   - ReportCleanupListener.onExecutionFinish() is triggered
   - Renames report with timestamp
   - Format: ExtentReport_YYYY-MM-DD_HH-mm-ss.html
   - Logs final report location
   ```

### Example Flow

**Scenario:** Retention count = 3, Current reports = 3

```
Before execution:
test-output/ExtentReports/
├── ExtentReport_2024-01-15_10-00-00.html  (oldest)
├── ExtentReport_2024-01-15_11-00-00.html
└── ExtentReport_2024-01-15_12-00-00.html  (newest)

Cleanup phase:
- Count (3) <= Limit (3) ✓
- No cleanup needed

Execution phase:
- Tests run...
- Temporary report created: ExtentReport.html

Post-processing phase:
- Rename: ExtentReport.html → ExtentReport_2024-01-15_13-00-00.html
- Count (4) > Limit (3) for next run

After execution:
test-output/ExtentReports/
├── ExtentReport_2024-01-15_10-00-00.html
├── ExtentReport_2024-01-15_11-00-00.html
├── ExtentReport_2024-01-15_12-00-00.html
└── ExtentReport_2024-01-15_13-00-00.html  (newest)

Next execution cleanup:
- Will delete: ExtentReport_2024-01-15_10-00-00.html (oldest)
- Will retain: 3 newest reports
```

## 📝 Console Output

### Cleanup Phase Output

```
========================================
ExtentReports Cleanup - Starting
========================================
Report cleanup: Found 11 reports, retention limit is 10. Deleting 1 oldest report(s)...
Deleted old report: ExtentReport_2024-01-10_09-00-00.html
Report cleanup completed: 1 file(s) deleted, 10 file(s) retained.
Current report count: 10
Retention limit: 10
========================================
```

### Post-Processing Output

```
========================================
ExtentReports Post-Processing - Starting
========================================
ExtentReport renamed successfully:
  From: ExtentReport.html
  To:   ExtentReport_2024-01-15_14-30-45.html
  Location: C:\project\test-output\ExtentReports\ExtentReport_2024-01-15_14-30-45.html
Total ExtentReports stored: 10
========================================
ExtentReports Post-Processing - Completed
========================================
```

## 🔧 Components

### 1. ReportManager Class

**Location:** `src/test/java/com/automation/framework/reports/ReportManager.java`

**Methods:**
```java
// Get timestamped report filename
String getTimestampedReportName()

// Get full report path
String getReportPath()

// Clean up old reports
void cleanupOldReports()

// List all reports (sorted by date)
List<String> listAllReports()

// Get current report count
int getReportCount()
```

### 2. ReportCleanupListener

**Location:** `src/test/java/com/automation/framework/listeners/ReportCleanupListener.java`

**Purpose:** TestNG listener that manages report lifecycle

**Hooks:**
- `onExecutionStart()` - Runs cleanup before tests
- `onExecutionFinish()` - Renames report after tests

### 3. ConfigReader Methods

```java
// Get report directory path
String getExtentReportPath()

// Get retention count
int getExtentReportRetentionCount()
```

## 📂 Directory Structure

```
test-output/
└── ExtentReports/
    ├── ExtentReport_2024-01-15_08-00-00.html
    ├── ExtentReport_2024-01-15_09-30-15.html
    ├── ExtentReport_2024-01-15_10-45-22.html
    ├── ...
    └── ExtentReport_2024-01-15_14-30-45.html  (latest)
```

## 🎯 Configuration Examples

### Minimal Storage (Keep 1 Report)

```properties
extent.report.retention.count=1
```

**Use Case:** CI/CD with external report archiving

### Standard Testing (Keep 10 Reports - Default)

```properties
extent.report.retention.count=10
```

**Use Case:** Regular development and testing

### Long-term Tracking (Keep 50 Reports)

```properties
extent.report.retention.count=50
```

**Use Case:** Regression tracking, trend analysis

### Custom Directory

```properties
extent.report.path=target/test-results/extent
extent.report.retention.count=15
```

## 💡 Best Practices

### 1. CI/CD Pipeline
```properties
extent.report.retention.count=5
```
- Keep minimal reports in CI
- Archive important reports externally
- Faster cleanup

### 2. Local Development
```properties
extent.report.retention.count=10
```
- Good balance of history and disk space
- Easy comparison between runs

### 3. Regression Testing
```properties
extent.report.retention.count=20
```
- Track patterns over time
- Compare against baseline

### 4. Nightly Builds
```properties
extent.report.retention.count=30
```
- One month of history
- Daily execution tracking

## 🔍 Monitoring Reports

### Check Current Report Count

The count is logged at the start of each execution:
```
Current report count: 10
Retention limit: 10
```

### List All Reports

Reports are sorted by timestamp in the directory:
```
test-output/ExtentReports/
```

### View Latest Report

The most recent report has the latest timestamp:
```
ExtentReport_2024-01-15_14-30-45.html  (latest)
```

## 🐛 Troubleshooting

### Reports Not Being Cleaned Up

**Symptom:** More reports than retention limit

**Causes:**
1. Listener not configured in testng.xml
2. Invalid retention count in config.properties
3. File permissions issues

**Solution:**
```xml
<!-- testng.xml -->
<listeners>
    <listener class-name="com.automation.framework.listeners.ReportCleanupListener"/>
</listeners>
```

### Report Not Renamed

**Symptom:** Report stays as ExtentReport.html

**Cause:** Listener not executing

**Solution:**
- Check testng.xml has listener configured
- Verify tests ran through TestNG (not direct Cucumber)

### Permission Errors

**Symptom:** Cannot delete old reports

**Cause:** File permissions or file in use

**Solution:**
- Close any open reports in browser
- Check directory permissions
- Run as administrator if needed

### Invalid Retention Count

**Symptom:** Error in console

```
Invalid retention count, using default: 10
```

**Solution:**
```properties
extent.report.retention.count=10  # Must be a valid integer
```

## 📊 Report Naming Convention

**Format:** `ExtentReport_YYYY-MM-DD_HH-mm-ss.html`

**Examples:**
- `ExtentReport_2024-01-15_14-30-45.html`
- `ExtentReport_2024-01-15_15-22-10.html`
- `ExtentReport_2024-01-16_09-15-33.html`

**Benefits:**
- Chronological sorting
- Easy identification
- No naming conflicts
- Timestamp-based tracking

## ✅ Verification

### Test Report Management

1. Run tests multiple times (more than retention limit)
2. Check `test-output/ExtentReports/` directory
3. Verify only N reports exist (N = retention count)
4. Check console for cleanup messages

### Expected Output

After running 15 tests with retention count of 10:
```
test-output/ExtentReports/
├── ExtentReport_2024-01-15_10-00-00.html
├── ExtentReport_2024-01-15_10-30-00.html
├── ...
└── ExtentReport_2024-01-15_14-30-00.html
Total: 10 files
```

## 🎉 Summary

The report management system provides:
- ✅ Automatic timestamping of all reports
- ✅ Configurable retention policy
- ✅ Automatic cleanup of old reports
- ✅ No manual intervention required
- ✅ Clear console feedback
- ✅ Historical tracking capability
- ✅ Disk space management
- ✅ ExtentReports-only (simplified)

**Default Configuration:**
- Retention: 10 reports
- Location: test-output/ExtentReports/
- Automatic cleanup: Enabled
- Timestamped naming: Enabled

---

**No manual cleanup needed - the framework handles everything automatically!** 🚀

