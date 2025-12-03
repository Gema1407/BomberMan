package com.bomberman.entities;

import com.bomberman.powerups.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Player class.
 * Tests player mechanics including movement, damage, and power-ups.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
class PlayerTest {
    
    private Player player;
    
    @BeforeEach
    void setUp() {
        player = new Player(1, 1);
    }
    
    /**
     * Tests player initialization.
     */
    @Test
    @DisplayName("Player initializes with correct default values")
    void testPlayerInitialization() {
        assertEquals(1, player.getX(), "Initial X should be 1");
        assertEquals(1, player.getY(), "Initial Y should be 1");
        assertEquals(3, player.getHp(), "Initial HP should be 3");
        assertTrue(player.isActive(), "Player should be active");
        assertEquals(1.0, player.getSpeedBoost(), 0.001, "Default speed should be 1.0");
        assertEquals(3, player.getMaxBombs(), "Default max bombs should be 3");
        assertEquals(3, player.getBombRadius(), "Default bomb radius should be 3");
    }
    
    /**
     * Tests player damage mechanism.
     */
    @Test
    @DisplayName("Player takes damage correctly")
    void testPlayerDamage() {
        int initialHp = player.getHp();
        boolean damaged = player.damage();
        
        assertTrue(damaged, "Should take damage on first hit");
        assertEquals(initialHp - 1, player.getHp(), "HP should decrease by 1");
    }
    
    /**
     * Tests invincibility frames after taking damage.
     */
    @Test
    @DisplayName("Player becomes invincible after taking damage")
    void testInvincibility() {
        player.damage();
        int hpAfterFirstDamage = player.getHp();
        
        boolean damagedAgain = player.damage();
        
        assertFalse(damagedAgain, "Should not take damage while invincible");
        assertEquals(hpAfterFirstDamage, player.getHp(), "HP should not change when invincible");
    }
    
    /**
     * Tests player reset functionality.
     */
    @Test
    @DisplayName("Player reset restores default state")
    void testPlayerReset() {
        player.damage();
        player.setPosition(5, 5);
        player.addPowerUp(new SpeedBoost());
        
        player.reset();
        
        assertEquals(3, player.getHp(), "HP should be restored");
        assertEquals(1, player.getX(), "X should reset to 1");
        assertEquals(1, player.getY(), "Y should reset to 1");
        assertTrue(player.getActivePowerUps().isEmpty(), "Power-ups should be cleared");
        assertEquals(1.0, player.getSpeedBoost(), 0.001, "Speed should reset to 1.0");
    }
    
    /**
     * Tests player positioning.
     */
    @Test
    @DisplayName("Player position can be set")
    void testPositioning() {
        player.setPosition(10, 15);
        
        assertEquals(10, player.getX(), "X should be updated");
        assertEquals(15, player.getY(), "Y should be updated");
    }
    
    /**
     * Tests speed boost power-up application.
     */
    @Test
    @DisplayName("Speed boost power-up increases player speed")
    void testSpeedBoostPowerUp() {
        SpeedBoost speedBoost = new SpeedBoost();
        player.addPowerUp(speedBoost);
        
        assertTrue(player.getSpeedBoost() > 1.0, "Speed should be boosted");
        assertEquals(1, player.getActivePowerUps().size(), "Should have 1 active power-up");
    }
    
    /**
     * Tests bomb capacity power-up application.
     */
    @Test
    @DisplayName("Bomb capacity power-up increases max bombs")
    void testBombCapacityPowerUp() {
        int initialMaxBombs = player.getMaxBombs();
        BombCapacityBoost capacityBoost = new BombCapacityBoost();
        player.addPowerUp(capacityBoost);
        
        assertTrue(player.getMaxBombs() > initialMaxBombs, "Max bombs should increase");
    }
    
    /**
     * Tests explosion range power-up application.
     */
    @Test
    @DisplayName("Explosion range power-up increases bomb radius")
    void testExplosionRangePowerUp() {
        int initialRadius = player.getBombRadius();
        ExplosionRangeBoost rangeBoost = new ExplosionRangeBoost();
        player.addPowerUp(rangeBoost);
        
        assertTrue(player.getBombRadius() > initialRadius, "Bomb radius should increase");
    }
    
    /**
     * Tests multiple power-ups stacking.
     */
    @Test
    @DisplayName("Multiple power-ups can stack")
    void testMultiplePowerUps() {
        player.addPowerUp(new SpeedBoost());
        player.addPowerUp(new BombCapacityBoost());
        player.addPowerUp(new ExplosionRangeBoost());
        
        assertEquals(3, player.getActivePowerUps().size(), "Should have 3 active power-ups");
        assertTrue(player.getSpeedBoost() > 1.0, "Speed should be boosted");
        assertTrue(player.getMaxBombs() > 3, "Max bombs should be increased");
        assertTrue(player.getBombRadius() > 3, "Bomb radius should be increased");
    }
}
