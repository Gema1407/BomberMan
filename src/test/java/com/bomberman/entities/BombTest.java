package com.bomberman.entities;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Bomb class.
 * Tests bomb timer, explosion mechanics, and radius.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
class BombTest {
    
    private Bomb bomb;
    
    @BeforeEach
    void setUp() {
        bomb = new Bomb(5, 5, 3);
    }
    
    /**
     * Tests bomb initialization.
     */
    @Test
    @DisplayName("Bomb initializes with correct values")
    void testBombInitialization() {
        assertEquals(5, bomb.getX(), "X coordinate should be 5");
        assertEquals(5, bomb.getY(), "Y coordinate should be 5");
        assertEquals(3, bomb.getRadius(), "Radius should be 3");
        assertTrue(bomb.isActive(), "Bomb should be active initially");
    }
    
    /**
     * Tests bomb timer countdown.
     */
    @Test
    @DisplayName("Bomb timer counts down")
    void testBombTimer() {
        // Update bomb multiple times
        for (int i = 0; i < 120; i++) {
            bomb.update();
        }
        
        // After 120 updates, bomb should be inactive
        assertFalse(bomb.isActive(), "Bomb should be inactive after timer expires");
    }
    
    /**
     * Tests bomb radius getter.
     */
    @Test
    @DisplayName("Bomb radius can be retrieved")
    void testBombRadius() {
        Bomb smallBomb = new Bomb(0, 0, 2);
        Bomb largeBomb = new Bomb(0, 0, 5);
        
        assertEquals(2, smallBomb.getRadius(), "Small bomb radius should be 2");
        assertEquals(5, largeBomb.getRadius(), "Large bomb radius should be 5");
    }
    
    /**
     * Tests that bomb doesn't deactivate prematurely.
     */
    @Test
    @DisplayName("Bomb remains active before timer expires")
    void testBombRemainsActive() {
        for (int i = 0; i < 100; i++) {
            bomb.update();
        }
        
        assertTrue(bomb.isActive(), "Bomb should still be active after 100 frames");
    }
}
