package com.bomberman.powerups;

import com.bomberman.entities.Player;

/**
 * Bomb capacity boost power-up that increases maximum simultaneous bombs.
 * Concrete decorator implementing the Decorator pattern.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public class BombCapacityBoost extends PowerUp {
    /** Number of additional bombs granted */
    private static final int ADDITIONAL_BOMBS = 2;
    
    /** Default duration: 20 seconds */
    private static final int CAPACITY_BOOST_DURATION = 1200;
    
    /**
     * Creates a new bomb capacity boost power-up.
     */
    public BombCapacityBoost() {
        super("Bomb Capacity+", CAPACITY_BOOST_DURATION);
    }
    
    /**
     * Applies bomb capacity boost to player.
     * 
     * @param player Player to boost
     */
    @Override
    public void apply(Player player) {
        super.apply(player);
        player.addMaxBombs(ADDITIONAL_BOMBS);
    }
    
    /**
     * Removes bomb capacity boost from player.
     * 
     * @param player Player to remove boost from
     */
    @Override
    public void remove(Player player) {
        super.remove(player);
        player.addMaxBombs(-ADDITIONAL_BOMBS);
    }
}
