package com.bomberman.states;

import com.bomberman.core.EntityFactory;
import com.bomberman.core.GameManager;
import com.bomberman.entities.GameObject;
import com.bomberman.entities.Player;
import com.bomberman.managers.SettingsManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class DifficultySelectionState implements GameState {
    private int selection = 1; // Default to Medium
    private final String[] options = { "EASY", "MEDIUM", "HARD" };
    private final String[] descriptions = {
        "For beginners. Fewer enemies, slower pace.",
        "The standard experience. Balanced challenge.",
        "For experts. Many fast enemies. Good luck!"
    };
    private static final String FONT_NAME_DEFAULT = "Consolas";

    private List<GameObject> previewWalls = new ArrayList<>();
    private List<GameObject> previewEnemies = new ArrayList<>();
    private Player previewPlayer = new Player(1, 1);
    private int lastSelection = -1;

    @Override
    public void update(GameManager gm) {
        // Handle periodic game logic, state changes, or object movement/behavior
        // for this component, driven by the main game loop (gm).
    }

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        int screenW = GameManager.GRID_W * GameManager.TILE_SIZE;
        int screenH = GameManager.GRID_H * GameManager.TILE_SIZE;

        // Background
        g2d.setColor(new Color(20, 20, 30));
        g2d.fillRect(0, 0, screenW, screenH);

        // Title
        g2d.setColor(Color.ORANGE);
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 40));
        String title = "SELECT DIFFICULTY";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (screenW - titleW) / 2, 80);

        // Layout: Left side options, Right side preview
        int leftX = 150;
        int rightX = screenW / 2 + 50;
        int startY = 200;

        // Options (Left Side)
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 32));
        for (int i = 0; i < options.length; i++) {
            int y = startY + i * 100;
            
            if (i == selection) {
                String text = "> " + options[i];
                int textW = g2d.getFontMetrics().stringWidth(text);

                // Highlight Box
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(leftX - 20, y - 35, textW + 40, 50, 10, 10);
                
                g2d.setColor(Color.YELLOW);
                g2d.drawString(text, leftX, y);
                
                // Description
                g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.ITALIC, 16));
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawString(descriptions[i], leftX + 20, y + 30);
                g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 32)); // Reset font
            } else {
                g2d.setColor(Color.GRAY);
                g2d.drawString(options[i], leftX, y);
            }
        }

        // Preview (Right Side) - Larger and centered better
        int previewY = 150;
        drawPreview(g2d, rightX, previewY, selection);

        // Footer
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        String footer = "Press ENTER to Start, ESC to Back";
        int footerW = g2d.getFontMetrics().stringWidth(footer);
        g2d.drawString(footer, (screenW - footerW) / 2, screenH - 50);
    }
    
    private void updatePreview(int selection) {
        previewWalls.clear();
        previewEnemies.clear();
        
        // Use a fixed seed for consistent preview layout per difficulty
        java.util.Random rand = new java.util.Random(selection * 12345L);

        int w = GameManager.GRID_W;
        int h = GameManager.GRID_H;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (x == 0 || x == w - 1 || y == 0 || y == h - 1) {
                    previewWalls.add(EntityFactory.createWall(x, y, true));
                } else if (x % 2 == 0 && y % 2 == 0) {
                    previewWalls.add(EntityFactory.createWall(x, y, true));
                } else if ((x < 3 && y < 3)) {
                    // Safe zone
                } else if (rand.nextDouble() < 0.4) {
                    previewWalls.add(EntityFactory.createWall(x, y, false));
                }
            }
        }

        int enemiesToSpawn = switch (selection) {
            case 0 -> 3;
            case 1 -> 6;
            default -> 10;
        };
        int count = 0;
        int attempts = 0;
        while (count < enemiesToSpawn && attempts < 100) {
            int ex = rand.nextInt(w);
            int ey = rand.nextInt(h);
            // Simple validity check for preview
            boolean hitWall = false;
            for(GameObject wall : previewWalls) {
                if(wall.getX() == ex && wall.getY() == ey) { hitWall = true; break; }
            }
            
            if (!hitWall && (Math.abs(ex - 1) + Math.abs(ey - 1) > 3)) {
                previewEnemies.add(EntityFactory.createEnemy(ex, ey));
                count++;
            }
            attempts++;
        }
        
        
        previewPlayer.setPosition(1, 1);
    }

    private void drawPreview(Graphics2D g2d, int x, int y, int selection) {
        if (selection != lastSelection) {
            updatePreview(selection);
            lastSelection = selection;
        }

        int w = 500;
        int h = 400;
        
        // Border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, w, h);
        
        // Background
        g2d.setColor(new Color(20, 20, 20));
        g2d.fillRect(x + 1, y + 1, w - 1, h - 1);
        
        // Clip
        java.awt.Shape oldClip = g2d.getClip();
        g2d.setClip(x + 1, y + 1, w - 1, h - 1);
        
        // Calculate Scale
        double scaleX = (double) w / (GameManager.GRID_W * GameManager.TILE_SIZE);
        double scaleY = (double) h / (GameManager.GRID_H * GameManager.TILE_SIZE);
        double scale = Math.min(scaleX, scaleY);
        
        AffineTransform oldTransform = g2d.getTransform();
        
        // Translate to preview box
        g2d.translate(x, y);
        
        // Center the map in the box
        double mapW = GameManager.GRID_W * GameManager.TILE_SIZE * scale;
        double mapH = GameManager.GRID_H * GameManager.TILE_SIZE * scale;
        double offX = (w - mapW) / 2;
        double offY = (h - mapH) / 2;
        
        g2d.translate(offX, offY);
        g2d.scale(scale, scale);
        
        // Render Elements
        int ts = GameManager.TILE_SIZE;
        
        for (GameObject wall : previewWalls) {
            wall.render(g2d, ts);
        }
        
        for (GameObject enemy : previewEnemies) {
            enemy.render(g2d, ts);
        }
        
        previewPlayer.render(g2d, ts);
        
        // Restore
        g2d.setTransform(oldTransform);
        g2d.setClip(oldClip);
        
        // Label
        g2d.setColor(Color.CYAN);
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 20));
        String label = "PREVIEW: " + options[selection];
        int labelW = g2d.getFontMetrics().stringWidth(label);
        g2d.drawString(label, x + (w - labelW)/2, y - 10);
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (keyCode == KeyEvent.VK_UP) {
            selection--;
            if (selection < 0) selection = options.length - 1;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= options.length) selection = 0;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            SettingsManager.Difficulty diff = SettingsManager.Difficulty.MEDIUM;
            if (selection == 0) diff = SettingsManager.Difficulty.EASY;
            if (selection == 2) diff = SettingsManager.Difficulty.HARD;
            
            gm.setDifficulty(diff);
            gm.setState(gm.PLAYING_STATE, com.bomberman.core.TransitionManager.TransitionType.ZOOM_OUT);
            gm.resetGame();
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            gm.setState(gm.MAIN_MENU_STATE, com.bomberman.core.TransitionManager.TransitionType.SLIDE_RIGHT);
        }
    }
}
