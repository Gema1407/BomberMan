package com.bomberman.core;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameManager class.
 * Tests Singleton pattern, game initialization, and state management.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
class GameManagerTest {
    
    private GameManager gameManager;
    
    @BeforeEach
    void setUp() {
        gameManager = GameManager.getInstance();
    }
    
    /**
     * Tests that GameManager properly implements Singleton pattern.
     */
    @Test
    @DisplayName("Singleton pattern: getInstance returns same instance")
    void testSingletonInstance() {
        GameManager instance1 = GameManager.getInstance();
        GameManager instance2 = GameManager.getInstance();
        
        assertSame(instance1, instance2, "getInstance should return the same instance");
    }
    
    /**
     * Tests game initialization with valid parameters.
     */
    @Test
    @DisplayName("Game initialization succeeds with valid grid size")
    void testGameInitialization() {
        assertDoesNotThrow(() -> gameManager.init(), 
            "Game initialization should not throw exception");
    }
    
    /**
     * Tests that resetGame properly clears and regenerates game state.
     */
    @Test
    @DisplayName("Reset game clears all entities and regenerates map")
    void testResetGame() {
        gameManager.resetGame();
        
        assertNotNull(gameManager.getPlayer(), "Player should exist after reset");
        assertNotNull(gameManager.getWalls(), "Walls should exist after reset");
        assertNotNull(gameManager.getEnemies(), "Enemies should exist after reset");
        assertTrue(gameManager.getWalls().size() > 0, "Should have walls after reset");
        assertTrue(gameManager.getBombs().isEmpty(), "Bombs should be empty after reset");
        assertTrue(gameManager.getExplosions().isEmpty(), "Explosions should be empty after reset");
    }
    
    /**
     * Tests difficulty setting functionality.
     */
    @Test
    @DisplayName("Difficulty can be set and retrieved")
    void testDifficultySettings() {
        gameManager.setDifficulty(com.bomberman.managers.SettingsManager.Difficulty.EASY);
        assertEquals(com.bomberman.managers.SettingsManager.Difficulty.EASY, 
            gameManager.getDifficulty(), "Difficulty should be EASY");
        
        gameManager.setDifficulty(com.bomberman.managers.SettingsManager.Difficulty.HARD);
        assertEquals(com.bomberman.managers.SettingsManager.Difficulty.HARD, 
            gameManager.getDifficulty(), "Difficulty should be HARD");
    }
    
    /**
     * Tests collision detection helper method.
     */
    @Test
    @DisplayName("isValidMove correctly detects valid and invalid positions")
    void testCollisionDetection() {
        gameManager.resetGame();
        
        // Test boundary checking
        assertFalse(GameManager.isValidMove(-1, 0, GameManager.GRID_W, GameManager.GRID_H, 
            gameManager.getWalls()), "Negative X should be invalid");
        assertFalse(GameManager.isValidMove(0, -1, GameManager.GRID_W, GameManager.GRID_H, 
            gameManager.getWalls()), "Negative Y should be invalid");
        assertFalse(GameManager.isValidMove(GameManager.GRID_W, 0, GameManager.GRID_W, 
            GameManager.GRID_H, gameManager.getWalls()), "X >= GRID_W should be invalid");
        assertFalse(GameManager.isValidMove(0, GameManager.GRID_H, GameManager.GRID_W, 
            GameManager.GRID_H, gameManager.getWalls()), "Y >= GRID_H should be invalid");
        
        // Test wall collision (corners are always walls)
        assertFalse(GameManager.isValidMove(0, 0, GameManager.GRID_W, GameManager.GRID_H, 
            gameManager.getWalls()), "Top-left corner should have wall");
    }
    
    /**
     * Tests grid size constants.
     */
    @Test
    @DisplayName("Grid constants have valid values")
    void testGridConstants() {
        assertTrue(GameManager.TILE_SIZE > 0, "TILE_SIZE should be positive");
        assertTrue(GameManager.GRID_W >= 5, "GRID_W should be at least 5");
        assertTrue(GameManager.GRID_H >= 5, "GRID_H should be at least 5");
        assertTrue(GameManager.BOMB_RADIUS > 0, "BOMB_RADIUS should be positive");
        assertTrue(GameManager.MAX_BOMBS > 0, "MAX_BOMBS should be positive");
    }
}
