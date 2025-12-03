package com.bomberman.core;

import com.bomberman.entities.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EntityFactory class.
 * Tests Factory pattern implementation for creating game entities.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
class EntityFactoryTest {
    
    /**
     * Tests creation of wall entities.
     */
    @Test
    @DisplayName("Factory creates wall entities correctly")
    void testCreateWall() {
        GameObject hardWall = EntityFactory.createWall(5, 5, true);
        GameObject softWall = EntityFactory.createWall(6, 6, false);
        
        assertNotNull(hardWall, "Hard wall should not be null");
        assertNotNull(softWall, "Soft wall should not be null");
        assertInstanceOf(Wall.class, hardWall, "Should create Wall instance");
        assertInstanceOf(Wall.class, softWall, "Should create Wall instance");
        
        Wall hardWallObj = (Wall) hardWall;
        Wall softWallObj = (Wall) softWall;
        
        assertFalse(hardWallObj.isDestructible(), "Hard wall should not be destructible");
        assertTrue(softWallObj.isDestructible(), "Soft wall should be destructible");
        
        assertEquals(5, hardWall.getX(), "X coordinate should match");
        assertEquals(5, hardWall.getY(), "Y coordinate should match");
    }
    
    /**
     * Tests creation of enemy entities.
     */
    @Test
    @DisplayName("Factory creates enemy entities correctly")
    void testCreateEnemy() {
        Enemy enemy = EntityFactory.createEnemy(10, 10);
        
        assertNotNull(enemy, "Enemy should not be null");
        assertInstanceOf(Enemy.class, enemy, "Should create Enemy instance");
        assertEquals(10, enemy.getX(), "X coordinate should match");
        assertEquals(10, enemy.getY(), "Y coordinate should match");
        assertTrue(enemy.isActive(), "Enemy should be active by default");
    }
    
    /**
     * Tests creation of bomb entities.
     */
    @Test
    @DisplayName("Factory creates bomb entities correctly")
    void testCreateBomb() {
        int radius = 3;
        Bomb bomb = EntityFactory.createBomb(7, 8, radius);
        
        assertNotNull(bomb, "Bomb should not be null");
        assertInstanceOf(Bomb.class, bomb, "Should create Bomb instance");
        assertEquals(7, bomb.getX(), "X coordinate should match");
        assertEquals(8, bomb.getY(), "Y coordinate should match");
        assertEquals(radius, bomb.getRadius(), "Radius should match");
        assertTrue(bomb.isActive(), "Bomb should be active by default");
    }
    
    /**
     * Tests that factory methods create distinct instances.
     */
    @Test
    @DisplayName("Factory creates distinct instances")
    void testDistinctInstances() {
        Enemy enemy1 = EntityFactory.createEnemy(1, 1);
        Enemy enemy2 = EntityFactory.createEnemy(1, 1);
        
        assertNotSame(enemy1, enemy2, "Should create distinct instances");
    }
}
