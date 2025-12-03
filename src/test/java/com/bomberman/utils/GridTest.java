package com.bomberman.utils;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit tests for generic Grid utility.
 * Tests 2D grid operations with type safety.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
class GridTest {
    
    private Grid<String> grid;
    
    @BeforeEach
    void setUp() {
        grid = new Grid<>(10, 10);
    }
    
    /**
     * Tests grid initialization.
     */
    @Test
    @DisplayName("Grid initializes with correct dimensions")
    void testGridInitialization() {
        assertEquals(10, grid.getWidth(), "Width should be 10");
        assertEquals(10, grid.getHeight(), "Height should be 10");
    }
    
    /**
     * Tests setting and getting values.
     */
    @Test
    @DisplayName("Can set and get grid values")
    void testSetAndGet() {
        grid.set(5, 5, "TestValue");
        
        assertEquals("TestValue", grid.get(5, 5), "Should retrieve set value");
        assertNull(grid.get(0, 0), "Unset cells should be null");
    }
    
    /**
     * Tests coordinate validation.
     */
    @Test
    @DisplayName("isValid correctly validates coordinates")
    void testCoordinateValidation() {
        assertTrue(grid.isValid(0, 0), "Top-left corner should be valid");
        assertTrue(grid.isValid(9, 9), "Bottom-right corner should be valid");
        assertFalse(grid.isValid(-1, 0), "Negative X should be invalid");
        assertFalse(grid.isValid(0, -1), "Negative Y should be invalid");
        assertFalse(grid.isValid(10, 0), "X >= width should be invalid");
        assertFalse(grid.isValid(0, 10), "Y >= height should be invalid");
    }
    
    /**
     * Tests exception on invalid get.
     */
    @Test
    @DisplayName("Get throws exception for invalid coordinates")
    void testInvalidGet() {
        assertThrows(IndexOutOfBoundsException.class, 
            () -> grid.get(-1, 0), "Should throw on negative X");
        assertThrows(IndexOutOfBoundsException.class, 
            () -> grid.get(0, 10), "Should throw on Y out of bounds");
    }
    
    /**
     * Tests exception on invalid set.
     */
    @Test
    @DisplayName("Set throws exception for invalid coordinates")
    void testInvalidSet() {
        assertThrows(IndexOutOfBoundsException.class, 
            () -> grid.set(10, 0, "Test"), "Should throw on X out of bounds");
    }
    
    /**
     * Tests stream functionality.
     */
    @Test
    @DisplayName("Stream returns all non-null values")
    void testStream() {
        grid.set(0, 0, "A");
        grid.set(1, 1, "B");
        grid.set(2, 2, "C");
        
        List<String> values = grid.stream().collect(Collectors.toList());
        
        assertEquals(3, values.size(), "Should have 3 values");
        assertTrue(values.contains("A"), "Should contain A");
        assertTrue(values.contains("B"), "Should contain B");
        assertTrue(values.contains("C"), "Should contain C");
    }
    
    /**
     * Tests forEach iteration.
     */
    @Test
    @DisplayName("forEach iterates over all positions")
    void testForEach() {
        int[] count = {0};
        grid.forEach((x, y) -> count[0]++);
        
        assertEquals(100, count[0], "Should iterate over all 100 cells");
    }
    
    /**
     * Tests clear operation.
     */
    @Test
    @DisplayName("Clear removes all values")
    void testClear() {
        grid.set(0, 0, "Test");
        grid.set(5, 5, "Test2");
        grid.clear();
        
        assertEquals(0, grid.stream().count(), "Grid should be empty after clear");
    }
    
    /**
     * Tests invalid grid dimensions.
     */
    @Test
    @DisplayName("Constructor throws exception for invalid dimensions")
    void testInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Grid<String>(0, 10), "Should throw for width 0");
        assertThrows(IllegalArgumentException.class, 
            () -> new Grid<String>(10, 0), "Should throw for height 0");
        assertThrows(IllegalArgumentException.class, 
            () -> new Grid<String>(-5, 10), "Should throw for negative dimensions");
    }
}
