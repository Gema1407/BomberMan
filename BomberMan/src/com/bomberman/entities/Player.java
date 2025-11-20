package com.bomberman.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends GameObject {
    private int hp = 3;
    private int maxHp = 3;
    private int invincibleTimer = 0;
    private final int INVINCIBLE_TIME = 120; // frames

    public Player(int x, int y) {
        super(x, y);
        this.color = Color.WHITE;
    }

    @Override
    public void update() {
        if (invincibleTimer > 0) invincibleTimer--;
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        if (invincibleTimer > 0 && (System.currentTimeMillis() / 100) % 2 == 0) return; // Blink effect

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

        // Body (Blue)
        g.setColor(Color.BLUE);
        g.fillRect(px + 10, py + tileSize - 16, tileSize - 20, 14);
        
        // Hands/Feet (White)
        g.setColor(Color.WHITE);
        g.fillOval(px + 4, py + tileSize/2, 10, 10); // L Hand
        g.fillOval(px + tileSize - 14, py + tileSize/2, 10, 10); // R Hand
        g.fillOval(px + 8, py + tileSize - 8, 12, 8); // L Foot
        g.fillOval(px + tileSize - 20, py + tileSize - 8, 12, 8); // R Foot
    }

    public void damage() {
        if (invincibleTimer <= 0) {
            hp--;
            invincibleTimer = INVINCIBLE_TIME;
        }
    }

    public int getHp() { return hp; }
    public void reset() { hp = maxHp; invincibleTimer = 0; x = 1; y = 1; active = true; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
}
