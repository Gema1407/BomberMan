package com.bomberman.entities;

import com.bomberman.core.GameManager;
import com.bomberman.managers.SettingsManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

public class Enemy extends GameObject {
    private int moveTimer = 0;
    private int moveInterval = 30;

    public Enemy(int x, int y) {
        super(x, y);
        // Random color for each enemy
        float hue = new Random().nextFloat();
        this.color = Color.getHSBColor(hue, 0.7f, 0.6f);
        
        // Adjust speed based on difficulty
        SettingsManager.Difficulty diff = GameManager.getInstance().getDifficulty();
        switch (diff) {
            case EASY: moveInterval = 45; break; // Slower
            case MEDIUM: moveInterval = 30; break;
            case HARD: moveInterval = 15; break; // Faster
        }
    }

    // AI: Hunt Player & Avoid Overlap
    public void tryMove(List<GameObject> walls, List<GameObject> enemies, List<GameObject> bombs, Player player, int gridW, int gridH) {
        if (moveTimer > 0) {
            moveTimer--;
            return;
        }
        moveInterval = getMoveInterval(); // Refresh interval in case difficulty changed mid-game (unlikely but safe)
        moveTimer = moveInterval;

        int bestDirIndex = -1;
        double minDistance = Double.MAX_VALUE;
        int[][] dirs = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } }; // Up, Down, Left, Right

        // 1. Try to move towards player
        // Simple greedy approach: check all 4 neighbors, pick one that gets closer to
        // player and is valid

        // Shuffle directions to add some randomness if multiple are equally good or
        // blocked
        List<Integer> indices = new java.util.ArrayList<>();
        for (int i = 0; i < 4; i++)
            indices.add(i);
        java.util.Collections.shuffle(indices);

        for (int i : indices) {
            int nx = x + dirs[i][0];
            int ny = y + dirs[i][1];

            if (isValidMove(nx, ny, walls, enemies, bombs, gridW, gridH)) {
                double dist = Math.pow(nx - player.getX(), 2) + Math.pow(ny - player.getY(), 2);
                if (dist < minDistance) {
                    minDistance = dist;
                    bestDirIndex = i;
                }
            }
        }
        
        // Difficulty Adjustment: Hard enemies are smarter (always pick best path), Easy enemies are dumber (randomness)
        SettingsManager.Difficulty diff = GameManager.getInstance().getDifficulty();
        if (diff == SettingsManager.Difficulty.EASY && Math.random() < 0.5) {
             bestDirIndex = -1; // Force random move 50% of time
        }

        // If found a move that gets closer
        if (bestDirIndex != -1) {
            x += dirs[bestDirIndex][0];
            y += dirs[bestDirIndex][1];
        } else {
            // If stuck or no better move, try any valid random move (fallback)
            for (int i : indices) {
                int nx = x + dirs[i][0];
                int ny = y + dirs[i][1];
                if (isValidMove(nx, ny, walls, enemies, bombs, gridW, gridH)) {
                    x = nx;
                    y = ny;
                    break;
                }
            }
        }
    }
    
    private int getMoveInterval() {
         SettingsManager.Difficulty diff = GameManager.getInstance().getDifficulty();
        switch (diff) {
            case EASY: return 45;
            case MEDIUM: return 30;
            case HARD: return 15;
            default: return 30;
        }
    }

    private boolean isValidMove(int tx, int ty, List<GameObject> walls, List<GameObject> enemies, List<GameObject> bombs, int w, int h) {
        if (!GameManager.isValidMove(tx, ty, w, h, walls))
            return false;

        // Check against other enemies
        for (GameObject obj : enemies) {
            if (obj == this)
                continue; // Skip self
            if (obj.getX() == tx && obj.getY() == ty)
                return false;
        }

        // Check against bombs
        for (GameObject b : bombs) {
            if (b.getX() == tx && b.getY() == ty)
                return false;
        }

        return true;
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        int px = x * tileSize;
        int py = y * tileSize;
        
        // Retro Balloon-like Enemy
        g.setColor(this.color);
        g.fillOval(px + 4, py + 4, tileSize - 8, tileSize - 8);
        
        // Shine
        g.setColor(new Color(255, 255, 255, 100));
        g.fillOval(px + 8, py + 8, tileSize/4, tileSize/4);
        
        // Face
        g.setColor(Color.BLACK);
        // Eyes
        g.fillRect(px + 12, py + 16, 4, 8);
        g.fillRect(px + tileSize - 16, py + 16, 4, 8);
        // Mouth (Zigzag)
        g.drawPolyline(new int[]{px + 12, px + 16, px + 20, px + 24, px + 28}, 
                       new int[]{py + 32, py + 28, py + 32, py + 28, py + 32}, 5);
    }
}
