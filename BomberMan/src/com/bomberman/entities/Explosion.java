package com.bomberman.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Explosion extends GameObject {
    private int timer = 15; // Duration reduced from 30 to 15 as requested

    public Explosion(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {
        timer--;
        if (timer <= 0) active = false;
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        int px = x * tileSize;
        int py = y * tileSize;
        
        // Outer Fire
        g.setColor(new Color(255, 69, 0)); // Red-Orange
        g.fillRect(px, py, tileSize, tileSize);
        
        // Inner Fire
        g.setColor(new Color(255, 140, 0)); // Dark Orange
        g.fillRect(px + 4, py + 4, tileSize - 8, tileSize - 8);
        
        // Core
        g.setColor(Color.YELLOW);
        g.fillRect(px + 8, py + 8, tileSize - 16, tileSize - 16);
        
        // Random particles/sparks (simple)
        g.setColor(Color.WHITE);
        if (Math.random() < 0.5) g.fillRect(px + 2, py + 2, 4, 4);
        if (Math.random() < 0.5) g.fillRect(px + tileSize - 6, py + tileSize - 6, 4, 4);
    }
}
