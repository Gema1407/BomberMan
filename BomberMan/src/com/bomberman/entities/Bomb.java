package com.bomberman.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bomb extends GameObject {
    private int timer = 60; // 1 second at 60fps
    private int radius;

    public Bomb(int x, int y, int radius) {
        super(x, y);
        this.radius = radius;
        this.color = Color.BLACK;
    }

    @Override
    public void update() {
        timer--;
        if (timer <= 0) {
            this.active = false; // Bomb explodes
        }
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        int px = x * tileSize;
        int py = y * tileSize;

        // Pulsating effect
        int offset = (timer % 20 < 10) ? 4 : 0;

        // Bomb Body
        g.setColor(Color.BLACK);
        g.fillOval(px + 4 - offset / 2, py + 4 - offset / 2, tileSize - 8 + offset, tileSize - 8 + offset);
        
        // Highlight
        g.setColor(Color.DARK_GRAY);
        g.fillOval(px + 10, py + 10, tileSize/4, tileSize/4);

        // Fuse
        g.setColor(new Color(200, 100, 0)); // Brownish
        g.fillRect(px + tileSize/2 - 2, py, 4, 6);
        
        // Spark
        if (timer % 10 < 5) {
            g.setColor(Color.YELLOW);
            g.drawLine(px + tileSize/2, py, px + tileSize/2 - 4, py - 4);
            g.drawLine(px + tileSize/2, py, px + tileSize/2 + 4, py - 4);
            g.drawLine(px + tileSize/2, py, px + tileSize/2, py - 6);
        }
    }

    public int getRadius() {
        return radius;
    }
}
