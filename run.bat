@echo off
echo ========================================
echo    BomberMan - Starting Game...
echo ========================================
echo.

cd /d "%~dp0"

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH!
    echo Please install Java JDK and try again.
    pause
    exit /b 1
)

REM Run the game
echo Starting BomberMan...
echo.
java -cp bin com.bomberman.BomberQuest

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to start the game!
    echo Make sure the project is compiled correctly.
    pause
    exit /b 1
)

pause
