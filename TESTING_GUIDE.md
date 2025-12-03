# BomberQuest - Testing Guide

## Overview

This guide explains how to run and write tests for the BomberQuest project using JUnit 5.

## Test Structure

```
src/test/java/com/bomberman/
├── core/
│   ├── GameManagerTest.java      - Tests for GameManager singleton
│   └── EntityFactoryTest.java    - Tests for Factory pattern
├── entities/
│   ├── PlayerTest.java            - Tests for Player mechanics and power-ups
│   └── BombTest.java              - Tests for Bomb timer and explosions
└── utils/
    ├── ObjectPoolTest.java        - Tests for generic ObjectPool
    └── GridTest.java              - Tests for generic Grid
```

## Prerequisites

### Required Libraries

Download JUnit 5 standalone JAR:
```powershell
# Download to lib directory
mkdir lib
cd lib
# Download from: https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/
# File: junit-platform-console-standalone-1.9.3.jar
```

## Running Tests

### Compile Tests

```powershell
# Navigate to project root
cd c:\Users\nunu\Downloads\BOMBERMAN_PBO\BomberMan

# Compile main source files first
javac -d bin -sourcepath src src/com/bomberman/BomberQuest.java

# Compile test files
javac -d bin -cp "bin;lib/junit-platform-console-standalone-1.9.3.jar" -sourcepath "src;src/test/java" src/test/java/com/bomberman/**/*.java
```

### Run All Tests

```powershell
# Run all tests
java -jar lib/junit-platform-console-standalone-1.9.3.jar --class-path bin --scan-classpath
```

### Run Specific Test Class

```powershell
# Run specific test
java -jar lib/junit-platform-console-standalone-1.9.3.jar --class-path bin --select-class com.bomberman.entities.PlayerTest
```

## Test Coverage

### Current Test Coverage

| Component | Test Class | Test Count | Coverage |
|-----------|------------|------------|----------|
| GameManager | GameManagerTest | 6 tests | Singleton, init, reset, state, collision |
| EntityFactory | EntityFactoryTest | 4 tests | Wall, Enemy, Bomb creation |
| Player | PlayerTest | 10 tests | Damage, invincibility, power-ups |
| Bomb | BombTest | 4 tests | Timer, radius, activation |
| ObjectPool | ObjectPoolTest | 7 tests | Acquire, release, capacity |
| Grid | GridTest | 9 tests | Get/set, validation, stream |
| **TOTAL** | **6 classes** | **40+ tests** | **Core functionality** |

## Writing New Tests

### Test Template

```java
package com.bomberman.yourpackage;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for YourClass.
 */
class YourClassTest {
    
    private YourClass instance;
    
    @BeforeEach
    void setUp() {
        // Initialize test instance before each test
        instance = new YourClass();
    }
    
    @Test
    @DisplayName("Test description")
    void testSomething() {
        // Arrange
        int expected = 5;
        
        // Act
        int actual = instance.someMethod();
        
        // Assert
        assertEquals(expected, actual, "Error message");
    }
    
    @AfterEach
    void tearDown() {
        // Clean up after each test if needed
    }
}
```

### Common Assertions

```java
// Equality
assertEquals(expected, actual, "message");
assertNotEquals(value1, value2, "message");

// Null checks
assertNull(object, "message");
assertNotNull(object, "message");

// Boolean
assertTrue(condition, "message");
assertFalse(condition, "message");

// Same instance
assertSame(object1, object2, "message");
assertNotSame(object1, object2, "message");

// Type checking
assertInstanceOf(ExpectedClass.class, object, "message");

// Exceptions
assertThrows(ExceptionClass.class, () -> {
    // Code that should throw
}, "message");

assertDoesNotThrow(() -> {
    // Code that should not throw
}, "message");
```

## Best Practices

### 1. Test Naming
- Use descriptive test method names
- Use `@DisplayName` for human-readable descriptions
- Follow pattern: `testMethodName_Condition_ExpectedResult`

### 2. Test Structure
- **Arrange**: Set up test data
- **Act**: Execute the code being tested
- **Assert**: Verify the results

### 3. Test Independence
- Each test should be independent
- Use `@BeforeEach` to reset state
- Don't rely on test execution order

### 4. Test One Thing
- Each test should verify one behavior
- Keep tests simple and focused
- If testing multiple scenarios, create multiple tests

### 5. Use Meaningful Messages
- Always provide assertion messages
- Messages should explain what went wrong
- Help future developers understand failures

## Continuous Integration

### GitHub Actions Example

```yaml
name: Java CI

on: [push, pull_request]

jobs:
  test:
    runs-on: windows-latest
    
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'adopt'
    - name: Compile
      run: javac -d bin -sourcepath src src/com/bomberman/BomberQuest.java
    - name: Download JUnit
      run: |
        mkdir lib
        cd lib
        curl -o junit.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar
    - name: Compile Tests
      run: javac -d bin -cp "bin;lib/junit.jar" -sourcepath "src;src/test/java" src/test/java/com/bomberman/**/*.java
    - name: Run Tests
      run: java -jar lib/junit.jar --class-path bin --scan-classpath
```

## Troubleshooting

### Common Issues

**Problem**: `ClassNotFoundException`
- **Solution**: Ensure bin directory contains all compiled classes
- **Solution**: Check classpath includes both bin and JUnit JAR

**Problem**: Tests not discovered
- **Solution**: Verify test classes are in correct package structure
- **Solution**: Check test methods are public and annotated with `@Test`

**Problem**: Compilation errors
- **Solution**: Compile main source before tests
- **Solution**: Include JUnit JAR in classpath for test compilation

## Coverage Goals

- **Unit Tests**: 80% code coverage for core logic
- **Integration Tests**: All major game flows
- **Edge Cases**: Boundary conditions and error handling

## Future Enhancements

- [ ] Add integration tests for game flow
- [ ] Add performance benchmarks
- [ ] Add mutation testing
- [ ] Implement code coverage reports
- [ ] Add parameterized tests for multiple scenarios

---

**Last Updated**: 2025-12-03
**Version**: 1.0
**Framework**: JUnit 5.9.3
