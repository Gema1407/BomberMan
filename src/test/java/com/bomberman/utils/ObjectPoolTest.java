package com.bomberman.utils;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for generic ObjectPool utility.
 * Tests pool acquisition, release, and capacity management.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
class ObjectPoolTest {
    
    private ObjectPool<String> stringPool;
    
    @BeforeEach
    void setUp() {
        stringPool = new ObjectPool<>(() -> "TestObject", 5);
    }
    
    /**
     * Tests pool initialization.
     */
    @Test
    @DisplayName("ObjectPool initializes correctly")
    void testPoolInitialization() {
        assertEquals(0, stringPool.getAvailableCount(), "Pool should start empty");
        assertEquals(0, stringPool.getTotalObjectCount(), "No objects should be created yet");
    }
    
    /**
     * Tests object acquisition from empty pool.
     */
    @Test
    @DisplayName("Acquire creates new object when pool is empty")
    void testAcquireFromEmptyPool() {
        String obj = stringPool.acquire();
        
        assertNotNull(obj, "Should return a new object");
        assertEquals("TestObject", obj, "Should use factory to create object");
        assertEquals(1, stringPool.getTotalObjectCount(), "Total count should be 1");
    }
    
    /**
     * Tests object release and reacquisition.
     */
    @Test
    @DisplayName("Released objects can be reacquired")
    void testReleaseAndReacquire() {
        String obj1 = stringPool.acquire();
        stringPool.release(obj1);
        
        assertEquals(1, stringPool.getAvailableCount(), "Pool should have 1 available object");
        
        String obj2 = stringPool.acquire();
        assertSame(obj1, obj2, "Should reuse released object");
        assertEquals(0, stringPool.getAvailableCount(), "Pool should be empty again");
    }
    
    /**
     * Tests pool maximum capacity.
     */
    @Test
    @DisplayName("Pool respects maximum capacity")
    void testMaxCapacity() {
        // Create and release 10 objects (max is 5)
        for (int i = 0; i < 10; i++) {
            String obj = stringPool.acquire();
            stringPool.release(obj);
        }
        
        assertEquals(5, stringPool.getAvailableCount(), 
            "Pool should not exceed max capacity");
    }
    
    /**
     * Tests pool clear operation.
     */
    @Test
    @DisplayName("Clear removes all pooled objects")
    void testClear() {
        stringPool.acquire();
        stringPool.acquire();
        stringPool.clear();
        
        assertEquals(0, stringPool.getAvailableCount(), "Pool should be empty");
        assertEquals(0, stringPool.getTotalObjectCount(), "Total count should reset");
    }
    
    /**
     * Tests null object handling.
     */
    @Test
    @DisplayName("Release ignores null objects")
    void testNullRelease() {
        stringPool.release(null);
        assertEquals(0, stringPool.getAvailableCount(), 
            "Null release should not affect pool");
    }
    
    /**
     * Tests invalid max size.
     */
    @Test
    @DisplayName("Constructor throws exception for invalid max size")
    void testInvalidMaxSize() {
        assertThrows(IllegalArgumentException.class, 
            () -> new ObjectPool<>(() -> "Test", 0),
            "Should throw exception for max size 0");
        
        assertThrows(IllegalArgumentException.class, 
            () -> new ObjectPool<>(() -> "Test", -1),
            "Should throw exception for negative max size");
    }
}
