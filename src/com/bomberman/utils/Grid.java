package com.bomberman.utils;

import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Generic 2D grid structure providing type-safe access to grid cells.
 * Demonstrates Generic Programming with bounds checking and functional operations.
 * 
 * @param <T> The type of elements stored in the grid
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public class Grid<T> {
    /** Internal 2D array storing grid data */
    private final T[][] data;
    
    /** Width of the grid */
    private final int width;
    
    /** Height of the grid */
    private final int height;
    
    /**
     * Creates a new grid with specified dimensions.
     * 
     * @param width Grid width (number of columns)
     * @param height Grid height (number of rows)
     * @throws IllegalArgumentException if width or height is less than 1
     */
    @SuppressWarnings("unchecked")
    public Grid(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Grid dimensions must be at least 1x1");
        }
        
        this.width = width;
        this.height = height;
        this.data = (T[][]) new Object[height][width];
    }
    
    /**
     * Gets the value at the specified grid position.
     * 
     * @param x X-coordinate (column)
     * @param y Y-coordinate (row)
     * @return The value at position (x, y), or null if empty
     * @throws IndexOutOfBoundsException if coordinates are outside grid bounds
     */
    public T get(int x, int y) {
        validateCoordinates(x, y);
        return data[y][x];
    }
    
    /**
     * Sets the value at the specified grid position.
     * 
     * @param x X-coordinate (column)
     * @param y Y-coordinate (row)
     * @param value The value to set
     * @throws IndexOutOfBoundsException if coordinates are outside grid bounds
     */
    public void set(int x, int y, T value) {
        validateCoordinates(x, y);
        data[y][x] = value;
    }
    
    /**
     * Checks if the specified coordinates are within grid bounds.
     * 
     * @param x X-coordinate to check
     * @param y Y-coordinate to check
     * @return true if coordinates are valid, false otherwise
     */
    public boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /**
     * Gets the width of the grid.
     * 
     * @return Grid width in cells
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the grid.
     * 
     * @return Grid height in cells
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Creates a stream of all non-null values in the grid.
     * Useful for functional-style filtering and processing.
     * 
     * @return Stream of all non-null grid values
     */
    public Stream<T> stream() {
        List<T> values = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[y][x] != null) {
                    values.add(data[y][x]);
                }
            }
        }
        return values.stream();
    }
    
    /**
     * Iterates over all grid positions, invoking the consumer for each.
     * 
     * @param consumer BiConsumer accepting (x, y) coordinates
     */
    public void forEach(BiConsumer<Integer, Integer> consumer) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                consumer.accept(x, y);
            }
        }
    }
    
    /**
     * Clears all values from the grid.
     */
    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = null;
            }
        }
    }
    
    /**
     * Validates that coordinates are within grid bounds.
     * 
     * @param x X-coordinate
     * @param y Y-coordinate
     * @throws IndexOutOfBoundsException if coordinates are invalid
     */
    private void validateCoordinates(int x, int y) {
        if (!isValid(x, y)) {
            throw new IndexOutOfBoundsException(
                String.format("Invalid coordinates (%d, %d) for grid of size %dx%d", 
                    x, y, width, height)
            );
        }
    }
}
