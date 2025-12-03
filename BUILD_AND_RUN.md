# BomberQuest - Build and Run Script

## Quick Start Guide

This document provides commands for compiling, testing, and running the BomberQuest application.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Windows PowerShell or Command Prompt

## Directory Structure

```
BomberMan/
├── src/
│   ├── com/bomberman/           # Main source code
│   └── test/java/com/bomberman/ # JUnit tests
├── bin/                         # Compiled .class files
├── lib/                         # External libraries (JUnit)
├── res/                         # Resources (sounds, images)
└── diagrams/                    # UML diagrams
```

## Build Commands

### Clean Build Directory

```powershell
# Remove all compiled files
Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path bin -Force
```

### Compile Main Application

```powershell
# Compile all source files
javac -d bin -sourcepath src src\com\bomberman\BomberQuest.java
```

**Expected**: No errors, `.class` files created in `bin/` directory

### Compile Tests (Requires JUnit)

```powershell
# First compile main code, then compile tests
javac -d bin -cp "bin;lib\junit-platform-console-standalone-1.9.3.jar" -sourcepath "src;src\test\java" src\test\java\com\bomberman\**\*.java
```

## Run Commands

### Run Game (GUI)

```powershell
# Run the main application
java -cp bin com.bomberman.BomberQuest
```

### Run Tests

```powershell
# Run all JUnit tests
java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path bin --scan-classpath
```

### Run Specific Test Class

```powershell
# Run only PlayerTest
java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path bin --select-class com.bomberman.entities.PlayerTest
```

## Complete Workflow

### First Time Setup

```powershell
# 1. Navigate to project directory
cd c:\Users\nunu\Downloads\BOMBERMAN_PBO\BomberMan

# 2. Create directories
New-Item -ItemType Directory -Path bin,lib -Force

# 3. Download JUnit (manual step or use curl)
# Download junit-platform-console-standalone-1.9.3.jar to lib/
```

### Development Cycle

```powershell
# 1. Clean and compile
Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path bin -Force
javac -d bin -sourcepath src src\com\bomberman\BomberQuest.java

# 2. Run application
java -cp bin com.bomberman.BomberQuest

# 3. Compile and run tests
javac -d bin -cp "bin;lib\junit-platform-console-standalone-1.9.3.jar" -sourcepath "src;src\test\java" src\test\java\com\bomberman\**\*.java
java -jar lib\junit-platform-console-standalone-1.9.3.jar --class-path bin --scan-classpath
```

## PowerShell Build Script

Create `build.ps1`:

```powershell
# BomberQuest Build Script

param(
    [switch]$Clean,
    [switch]$Test,
    [switch]$Run
)

$projectRoot = $PSScriptRoot
$binDir = Join-Path $projectRoot "bin"
$srcDir = Join-Path $projectRoot "src"
$testDir = Join-Path $projectRoot "src\test\java"
$libDir = Join-Path $projectRoot "lib"
$junitJar = Join-Path $libDir "junit-platform-console-standalone-1.9.3.jar"

Write-Host "=== BomberQuest Build Script ===" -ForegroundColor Cyan

# Clean
if ($Clean) {
    Write-Host "Cleaning bin directory..." -ForegroundColor Yellow
    Remove-Item -Recurse -Force $binDir -ErrorAction SilentlyContinue
    New-Item -ItemType Directory -Path $binDir -Force | Out-Null
}

# Compile Main
Write-Host "Compiling main application..." -ForegroundColor Yellow
javac -d $binDir -sourcepath $srcDir "$srcDir\com\bomberman\BomberQuest.java"

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}
Write-Host "Compilation successful!" -ForegroundColor Green

# Compile and Run Tests
if ($Test) {
    if (Test-Path $junitJar) {
        Write-Host "Compiling tests..." -ForegroundColor Yellow
        javac -d $binDir -cp "$binDir;$junitJar" -sourcepath "$srcDir;$testDir" "$testDir\com\bomberman\**\*.java"
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "Running tests..." -ForegroundColor Yellow
            java -jar $junitJar --class-path $binDir --scan-classpath
        } else {
            Write-Host "Test compilation failed!" -ForegroundError Red
        }
    } else {
        Write-Host "JUnit JAR not found at: $junitJar" -ForegroundColor Red
        Write-Host "Please download and place in lib directory" -ForegroundColor Yellow
    }
}

# Run Application
if ($Run) {
    Write-Host "Starting BomberQuest..." -ForegroundColor Yellow
    java -cp $binDir com.bomberman.BomberQuest
}

Write-Host "=== Build Complete ===" -ForegroundColor Cyan
```

### Usage:

```powershell
# Clean and compile
.\build.ps1 -Clean

# Compile and run tests
.\build.ps1 -Test

# Compile and run game
.\build.ps1 -Run

# All together
.\build.ps1 -Clean -Test -Run
```

## Troubleshooting

### Common Issues

**1. `javac: command not found`**
- Add Java to PATH: `C:\Program Files\Java\jdk-XX\bin`

**2. `ClassNotFoundException`**
- Ensure you're in correct directory
- Check classpath includes `bin`

**3. Tests not found**
- Verify JUnit JAR is in `lib/` directory
- Check test files are in `src/test/java/` structure

**4. Compilation errors**
- Clean bin directory first
- Compile main source before tests
- Check Java version (JDK 8+)

## Performance

- **Clean build**: ~2-5 seconds
- **Incremental build**: ~1-2 seconds
- **Test execution**: ~1-3 seconds
- **Application startup**: <1 second

## File Verification

After successful build, verify:

```powershell
# Check compiled classes
dir bin\com\bomberman -Recurse
dir bin\com\bomberman\core -Recurse
dir bin\com\bomberman\entities -Recurse
dir bin\com\bomberman\powerups -Recurse
dir bin\com\bomberman\utils -Recurse

# Should see .class files for all Java files
```

---

**Last Updated**: 2025-12-03
**Version**: 1.0
