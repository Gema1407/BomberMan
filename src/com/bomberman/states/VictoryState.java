package com.bomberman.states;

import com.bomberman.core.GameManager;
import com.bomberman.managers.LeaderboardManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class VictoryState implements GameState {
    private StringBuilder username = new StringBuilder();
    private boolean saved = false;
    private String errorMessage = "";
    private static final String FONT_NAME_DEFAULT = "Consolas";

    @Override
    public void update(GameManager gm) {
        // Handle periodic game logic, state changes, or object movement/behavior
        // for this component, driven by the main game loop (gm).
    }

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        gm.PLAYING_STATE.render(g2d, gm);
        
        int screenW = GameManager.GRID_W * GameManager.TILE_SIZE;
        int screenH = GameManager.GRID_H * GameManager.TILE_SIZE;

        // Dark Overlay
        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(0, 0, screenW, screenH);
        
        // Center Box
        int boxW = 600;
        int boxH = 450;
        int boxX = (screenW - boxW) / 2;
        int boxY = (screenH - boxH) / 2;
        
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(boxX + 5, boxY + 5, boxW, boxH, 20, 20);
        
        g2d.setColor(new Color(35, 45, 35));
        g2d.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g2d.setColor(new Color(100, 200, 100));
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        
        // Title with glow effect
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 70));
        String title = "VICTORY!";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        int titleX = boxX + (boxW - titleW) / 2;
        int titleY = boxY + 90;
        
        // Glow
        g2d.setColor(new Color(0, 255, 0, 50));
        for (int i = 0; i < 3; i++) {
            g2d.drawString(title, titleX - i, titleY - i);
            g2d.drawString(title, titleX + i, titleY + i);
        }
        
        g2d.setColor(new Color(100, 255, 100));
        g2d.drawString(title, titleX, titleY);
        
        // Time
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.PLAIN, 24));
        String timeStr = "Time: " + gm.getLastGameTime() + "s";
        int timeW = g2d.getFontMetrics().stringWidth(timeStr);
        g2d.drawString(timeStr, boxX + (boxW - timeW) / 2, boxY + 130);
        
        if (!saved) {
            // Input Box
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRect(boxX + 50, boxY + 180, boxW - 100, 40);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(boxX + 50, boxY + 180, boxW - 100, 40);
            
            String prompt = username.toString() + "_";
            int promptW = g2d.getFontMetrics().stringWidth(prompt);
            g2d.drawString(prompt, boxX + (boxW - promptW) / 2, boxY + 210);
            
            g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.ITALIC, 16));
            g2d.setColor(Color.LIGHT_GRAY);
            String hint = "Enter your name & press ENTER";
            int hintW = g2d.getFontMetrics().stringWidth(hint);
            g2d.drawString(hint, boxX + (boxW - hintW) / 2, boxY + 250);
            
            if (!errorMessage.isEmpty()) {
                g2d.setColor(Color.RED);
                int errW = g2d.getFontMetrics().stringWidth(errorMessage);
                g2d.drawString(errorMessage, boxX + (boxW - errW) / 2, boxY + 300);
            }
        } else {
            g2d.setColor(Color.YELLOW);
            String savedMsg = "Score Saved!";
            int savedW = g2d.getFontMetrics().stringWidth(savedMsg);
            g2d.drawString(savedMsg, boxX + (boxW - savedW) / 2, boxY + 200);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.PLAIN, 20));
            String playMsg = "Press ENTER to Play Again";
            int playW = g2d.getFontMetrics().stringWidth(playMsg);
            g2d.drawString(playMsg, boxX + (boxW - playW) / 2, boxY + 280);
            
            String menuMsg = "Press ESC for Menu";
            int menuW = g2d.getFontMetrics().stringWidth(menuMsg);
            g2d.drawString(menuMsg, boxX + (boxW - menuW) / 2, boxY + 310);
        }
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (!saved) {
            if (keyCode == KeyEvent.VK_ENTER) {
                if (username.length() > 0) {
                    String name = username.toString().trim();
                    LeaderboardManager lm = LeaderboardManager.getInstance();
                    
                    if (lm.isNameTaken(name)) {
                        errorMessage = "Name already taken! Choose another.";
                    } else {
                        lm.addScore(name, gm.getLastGameTime());
                        saved = true;
                        errorMessage = "";
                    }
                }
            } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
                if (username.length() > 0) username.setLength(username.length() - 1);
                errorMessage = "";
            } else if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
                // Letter keys
                if (username.length() < 12) username.append((char)keyCode);
                errorMessage = "";
            } else if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
                // Number keys
                if (username.length() < 12) username.append((char)keyCode);
                errorMessage = "";
            } else if (keyCode == KeyEvent.VK_SPACE) {
                if (username.length() < 12 && username.length() > 0) username.append(' ');
                errorMessage = "";
            }
        } else {
            if (keyCode == KeyEvent.VK_ENTER) {
                gm.setState(gm.DIFFICULTY_SELECTION_STATE, com.bomberman.core.TransitionManager.TransitionType.FADE);
                reset();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                gm.setState(gm.MAIN_MENU_STATE, com.bomberman.core.TransitionManager.TransitionType.FADE);
                reset();
            }
        }
    }

    public void reset() {
        username.setLength(0);
        saved = false;
        errorMessage = "";
    }
}
