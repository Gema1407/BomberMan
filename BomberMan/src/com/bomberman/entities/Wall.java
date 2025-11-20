package com.bomberman.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Wall extends GameObject {
    private boolean destructible;

    public Wall(int x, int y, boolean destructible) {
        super(x, y);
        this.destructible = destructible;
        this.color = destructible ? new Color(214, 48, 49) : new Color(99, 110, 114);
    }

    public boolean isDestructible() { return destructible; }

    @Override
    public void render(Graphics2D g, int tileSize) {
        int px = x * tileSize;
        int py = y * tileSize;
        
        if (destructible) {
            // Brick Pattern
            g.setColor(new Color(180, 50, 50));
            g.fillRect(px, py, tileSize, tileSize);
            
            g.setColor(new Color(130, 30, 30));
            g.setStroke(new java.awt.BasicStroke(2));
            g.drawRect(px, py, tileSize, tileSize);
            
            // Inner Bricks
            g.drawLine(px, py + tileSize/2, px + tileSize, py + tileSize/2);
            g.drawLine(px + tileSize/2, py, px + tileSize/2, py + tileSize/2);
            g.drawLine(px + tileSize/4, py + tileSize/2, px + tileSize/4, py + tileSize);
            g.drawLine(px + 3*tileSize/4, py + tileSize/2, px + 3*tileSize/4, py + tileSize);
        } else {
            // Solid Metal/Stone Block
            g.setColor(new Color(80, 80, 80));
            g.fillRect(px, py, tileSize, tileSize);
            
            // Bevel effect
            g.setColor(new Color(120, 120, 120));
            g.fillRect(px, py, tileSize, 4);
            g.fillRect(px, py, 4, tileSize);
            
            g.setColor(new Color(40, 40, 40));
            g.fillRect(px, py + tileSize - 4, tileSize, 4);
            g.fillRect(px + tileSize - 4, py, 4, tileSize);
            
            // Rivets
            g.setColor(new Color(30, 30, 30));
            g.fillOval(px + 6, py + 6, 4, 4);
            g.fillOval(px + tileSize - 10, py + 6, 4, 4);
            g.fillOval(px + 6, py + tileSize - 10, 4, 4);
            g.fillOval(px + tileSize - 10, py + tileSize - 10, 4, 4);
        }
    }
}
