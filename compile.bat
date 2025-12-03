@echo off
echo ========================================
echo    BomberMan - Compiling...
echo ========================================
echo.

cd /d "%~dp0"

REM Check if Java is installed
javac -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java compiler (javac) is not installed or not in PATH!
    echo Please install Java JDK and try again.
    pause
    exit /b 1
)

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Compile all Java files
echo Compiling Java source files...
echo.

javac -d bin -sourcepath src src\com\bomberman\*.java src\com\bomberman\core\*.java src\com\bomberman\entities\*.java src\com\bomberman\events\*.java src\com\bomberman\exceptions\*.java src\com\bomberman\managers\*.java src\com\bomberman\powerups\*.java src\com\bomberman\states\*.java src\com\bomberman\utils\*.java

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo    Compilation Successful!
    echo ========================================
    echo.
    echo You can now run the game using: run.bat
    echo Or manually with: java -cp bin com.bomberman.BomberQuest
) else (
    echo.
    echo ========================================
    echo    Compilation Failed!
    echo ========================================
    echo Please check the error messages above.
)

echo.
pause
