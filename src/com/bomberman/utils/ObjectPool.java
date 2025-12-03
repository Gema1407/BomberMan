package com.bomberman.utils;

import java.util.Queue;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * Generic object pool for efficient object reuse and reduced garbage collection.
 * This demonstrates Generic Programming principles by allowing type-safe pooling
 * of any object type.
 * 
 * @param <T> The type of objects to pool
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public class ObjectPool<T> {
    /** Queue storing available objects for reuse */
    private final Queue<T> pool;
    
    /** Factory for creating new objects when pool is empty */
    private final Supplier<T> factory;
    
    /** Maximum number of objects to keep in pool */
    private final int maxSize;
    
    /** Current number of objects created (active + pooled) */
    private int objectCount;
    
    /**
     * Creates a new object pool with specified factory and maximum size.
     * 
     * @param factory Supplier function to create new objects
     * @param maxSize Maximum number of objects to keep in pool
     * @throws IllegalArgumentException if maxSize is less than 1
     */
    public ObjectPool(Supplier<T> factory, int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("Max size must be at least 1");
        }
        
        this.factory = factory;
        this.maxSize = maxSize;
        this.pool = new LinkedList<>();
        this.objectCount = 0;
    }
    
    /**
     * Acquires an object from the pool, creating a new one if pool is empty.
     * 
     * @return An object of type T, either from pool or newly created
     */
    public T acquire() {
        if (pool.isEmpty()) {
            objectCount++;
            return factory.get();
        }
        return pool.poll();
    }
    
    /**
     * Releases an object back to the pool for reuse.
     * If pool is at maximum capacity, the object is discarded.
     * 
     * @param object The object to return to pool
     */
    public void release(T object) {
        if (object == null) {
            return;
        }
        
        if (pool.size() < maxSize) {
            pool.offer(object);
        } else {
            objectCount--;
        }
    }
    
    /**
     * Clears all objects from the pool.
     */
    public void clear() {
        pool.clear();
        objectCount = 0;
    }
    
    /**
     * Gets the current number of objects available in the pool.
     * 
     * @return Number of pooled objects ready for reuse
     */
    public int getAvailableCount() {
        return pool.size();
    }
    
    /**
     * Gets the total number of objects created by this pool.
     * 
     * @return Total object count (active + pooled)
     */
    public int getTotalObjectCount() {
        return objectCount;
    }
}
