@echo off
echo ========================================
echo   Running Regression Tests
echo ========================================
echo.

mvn clean test -Dcucumber.filter.tags="@Regression"

echo.
echo ========================================
echo   Regression Tests Finished
echo ========================================
echo.
echo Reports generated at: target\cucumber-reports\
echo.
pause

