@echo off
echo ========================================
echo   Running Smoke Tests
echo ========================================
echo.

mvn clean test -Dcucumber.filter.tags="@Smoke"

echo.
echo ========================================
echo   Smoke Tests Finished
echo ========================================
echo.
echo Reports generated at: target\cucumber-reports\
echo.
pause

