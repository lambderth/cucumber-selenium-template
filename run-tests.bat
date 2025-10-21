@echo off
echo ========================================
echo   Running Automation Tests
echo ========================================
echo.

mvn clean test

echo.
echo ========================================
echo   Tests Finished
echo ========================================
echo.
echo Reports generated at: target\cucumber-reports\cucumber.html
echo.
pause

