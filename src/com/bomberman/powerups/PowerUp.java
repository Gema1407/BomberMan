package com.bomberman.powerups;

import com.bomberman.entities.Player;

/**
 * Abstract base class for player power-ups using the Decorator pattern.
 * This is a STRUCTURAL design pattern that allows abilities to be added
 * to the player dynamically without modifying the Player class.
 * 
 * Each PowerUp can be chained to create multiple enhancements.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public abstract class PowerUp {
    /** Name of this power-up */
    protected final String name;
    
    /** Duration of this power-up in frames (60 FPS) */
    protected int duration;
    
    /** Whether this power-up is currently active */
    protected boolean active;
    
    /** Default duration for power-ups (10 seconds at 60 FPS) */
    protected static final int DEFAULT_DURATION = 600;
    
    /**
     * Creates a new power-up with specified name and duration.
     * 
     * @param name Power-up name
     * @param duration Duration in frames
     */
    protected PowerUp(String name, int duration) {
        this.name = name;
        this.duration = duration;
        this.active = false;
    }
    
    /**
     * Gets the name of this power-up.
     * 
     * @return Power-up name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Checks if this power-up is currently active.
     * 
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Applies this power-up's effect to the player.
     * Subclasses should override to implement specific effects.
     * 
     * @param player Player to apply power-up to
     */
    public void apply(Player player) {
        this.active = true;
    }
    
    /**
     * Removes this power-up's effect from the player.
     * Subclasses should override to remove specific effects.
     * 
     * @param player Player to remove power-up from
     */
    public void remove(Player player) {
        this.active = false;
    }
    
    /**
     * Updates this power-up's state (decrements duration).
     * Automatically removes power-up when duration expires.
     * 
     * @param player Player that has this power-up
     */
    public void update(Player player) {
        if (active && duration > 0) {
            duration--;
            if (duration <= 0) {
                remove(player);
            }
        }
    }
    
    /**
     * Gets the remaining duration of this power-up.
     * 
     * @return Remaining frames
     */
    public int getRemainingDuration() {
        return duration;
    }
}
