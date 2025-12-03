package com.bomberman.powerups;

import com.bomberman.entities.Player;

/**
 * Speed boost power-up that increases player movement speed.
 * Concrete decorator implementing the Decorator pattern.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public class SpeedBoost extends PowerUp {
    /** Speed multiplier applied to player */
    private static final double SPEED_MULTIPLIER = 1.5;
    
    /** Default duration: 15 seconds */
    private static final int SPEED_BOOST_DURATION = 900;
    
    /**
     * Creates a new speed boost power-up.
     */
    public SpeedBoost() {
        super("Speed Boost", SPEED_BOOST_DURATION);
    }
    
    /**
     * Applies speed boost to player by reducing move delay.
     * 
     * @param player Player to boost
     */
    @Override
    public void apply(Player player) {
        super.apply(player);
        player.setSpeedBoost(SPEED_MULTIPLIER);
    }
    
    /**
     * Removes speed boost from player.
     * 
     * @param player Player to remove boost from
     */
    @Override
    public void remove(Player player) {
        super.remove(player);
        player.setSpeedBoost(1.0);
    }
}
