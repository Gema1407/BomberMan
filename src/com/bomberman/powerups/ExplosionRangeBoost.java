package com.bomberman.powerups;

import com.bomberman.entities.Player;

/**
 * Explosion range boost power-up that increases bomb explosion radius.
 * Concrete decorator implementing the Decorator pattern.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public class ExplosionRangeBoost extends PowerUp {
    /** Additional explosion radius granted */
    private static final int ADDITIONAL_RANGE = 2;
    
    /** Default duration: 20 seconds */
    private static final int RANGE_BOOST_DURATION = 1200;
    
    /**
     * Creates a new explosion range boost power-up.
     */
    public ExplosionRangeBoost() {
        super("Explosion Range+", RANGE_BOOST_DURATION);
    }
    
    /**
     * Applies explosion range boost to player.
     * 
     * @param player Player to boost
     */
    @Override
    public void apply(Player player) {
        super.apply(player);
        player.addBombRadius(ADDITIONAL_RANGE);
    }
    
    /**
     * Removes explosion range boost from player.
     * 
     * @param player Player to remove boost from
     */
    @Override
    public void remove(Player player) {
        super.remove(player);
        player.addBombRadius(-ADDITIONAL_RANGE);
    }
}
