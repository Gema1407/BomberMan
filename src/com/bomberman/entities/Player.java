package com.bomberman.entities;

import com.bomberman.powerups.PowerUp;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Player entity with power-up support.
 * Enhanced to support the Decorator pattern for power-ups.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public class Player extends GameObject {
    /* Health System */
    private int hp = 3;
    private int maxHp = 3;
    private int invincibleTimer = 0;
    private static final int INVINCIBLE_TIME = 120; // frames

    /* Power-up System */
    private final List<PowerUp> activePowerUps;
    private double speedBoost = 1.0;
    private int maxBombs = 3;
    private int bombRadius = 3;
    
    /**
     * Creates a new player at the specified grid position.
     * 
     * @param x Initial X grid coordinate
     * @param y Initial Y grid coordinate
     */
    public Player(int x, int y) {
        super(x, y);
        this.color = Color.WHITE;
        this.activePowerUps = new ArrayList<>();
    }

    /**
     * Updates player state including invincibility timer and power-ups.
     */
    @Override
    public void update() {
        if (invincibleTimer > 0) {
            invincibleTimer--;
        }
        
        // Update all active power-ups
        activePowerUps.removeIf(powerUp -> {
            powerUp.update(this);
            return !powerUp.isActive();
        });
    }

    /**
     * Renders the player with retro Bomberman style.
     * Includes blink effect during invincibility.
     * 
     * @param g Graphics context to render to
     * @param tileSize Size of each grid tile in pixels
     */
    @Override
    public void render(Graphics2D g, int tileSize) {
        // Blink effect during invincibility
        if (invincibleTimer > 0 && (System.currentTimeMillis() / 100) % 2 == 0) {
            return;
        }

        int px = x * tileSize;
        int py = y * tileSize;

        // Retro Bomberman Style (Simplified)
        
        // Head (White Helmet)
        g.setColor(Color.WHITE);
        g.fillOval(px + 4, py + 2, tileSize - 8, tileSize - 8);
        g.setColor(Color.BLACK);
        g.drawOval(px + 4, py + 2, tileSize - 8, tileSize - 8);

        // Face (Pinkish)
        g.setColor(new Color(255, 200, 180));
        g.fillRect(px + 12, py + 12, tileSize - 24, tileSize/2);

        // Eyes
        g.setColor(Color.BLACK);
        g.fillRect(px + 16, py + 16, 4, 8);
        g.fillRect(px + tileSize - 20, py + 16, 4, 8);

        // Body (Blue or special color if powered up)
        if (!activePowerUps.isEmpty()) {
            g.setColor(new Color(255, 215, 0)); // Gold when powered up
        } else {
            g.setColor(Color.BLUE);
        }
        g.fillRect(px + 10, py + tileSize - 16, tileSize - 20, 14);
        
        // Hands/Feet (White)
        g.setColor(Color.WHITE);
        g.fillOval(px + 4, py + tileSize/2, 10, 10); // L Hand
        g.fillOval(px + tileSize - 14, py + tileSize/2, 10, 10); // R Hand
        g.fillOval(px + 8, py + tileSize - 8, 12, 8); // L Foot
        g.fillOval(px + tileSize - 20, py + tileSize - 8, 12, 8); // R Foot
    }

    /**
     * Damages the player if not invincible.
     * 
     * @return true if damage was taken, false if invincible
     */
    public boolean damage() {
        if (invincibleTimer <= 0) {
            hp--;
            invincibleTimer = INVINCIBLE_TIME;
            return true;
        }
        return false;
    }

    /**
     * Resets player to initial state, clearing power-ups.
     */
    public void reset() {
        hp = maxHp;
        invincibleTimer = 0;
        x = 1;
        y = 1;
        active = true;
        
        // Remove all power-ups
        for (PowerUp powerUp : activePowerUps) {
            powerUp.remove(this);
        }
        activePowerUps.clear();
        
        // Reset stats
        speedBoost = 1.0;
        maxBombs = 3;
        bombRadius = 3;
    }

    /**
     * Adds a power-up to the player.
     * 
     * @param powerUp Power-up to add
     */
    public void addPowerUp(PowerUp powerUp) {
        powerUp.apply(this);
        activePowerUps.add(powerUp);
    }

    /* Getters and Setters */
    
    /**
     * Gets current health points.
     * 
     * @return Current HP
     */
    public int getHp() {
        return hp;
    }
    
    /**
     * Sets the player's position.
     * 
     * @param x New X coordinate
     * @param y New Y coordinate
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Sets the speed boost multiplier.
     * 
     * @param multiplier Speed multiplier (1.0 = normal)
     */
    public void setSpeedBoost(double multiplier) {
        this.speedBoost = multiplier;
    }
    
    /**
     * Gets the current speed boost multiplier.
     * 
     * @return Speed multiplier
     */
    public double getSpeedBoost() {
        return speedBoost;
    }
    
    /**
     * Adds to maximum bomb capacity.
     * 
     * @param amount Amount to add (can be negative)
     */
    public void addMaxBombs(int amount) {
        this.maxBombs = Math.max(1, this.maxBombs + amount);
    }
    
    /**
     * Gets maximum number of simultaneous bombs.
     * 
     * @return Max bombs
     */
    public int getMaxBombs() {
        return maxBombs;
    }
    
    /**
     * Adds to bomb explosion radius.
     * 
     * @param amount Amount to add (can be negative)
     */
    public void addBombRadius(int amount) {
        this.bombRadius = Math.max(1, this.bombRadius + amount);
    }
    
    /**
     * Gets current bomb explosion radius.
     * 
     * @return Explosion radius in tiles
     */
    public int getBombRadius() {
        return bombRadius;
    }
    
    /**
     * Gets list of active power-ups.
     * 
     * @return List of active power-ups
     */
    public List<PowerUp> getActivePowerUps() {
        return new ArrayList<>(activePowerUps);
    }
}
