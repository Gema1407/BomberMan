package com.bomberman.states;

import com.bomberman.core.GameManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameOverState implements GameState {
    private static final String FONT_NAME_DEFAULT = "Consolas";
    @Override
    public void update(GameManager gm) {
        // Handle periodic game logic, state changes, or object movement/behavior
        // for this component, driven by the main game loop (gm).
    }

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        gm.PLAYING_STATE.render(g2d, gm); // Render background game
        
        int screenW = GameManager.GRID_W * GameManager.TILE_SIZE;
        int screenH = GameManager.GRID_H * GameManager.TILE_SIZE;
        
        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(0, 0, screenW, screenH);
        
        // Modal Box
        int boxW = 500;
        int boxH = 300;
        int boxX = (screenW - boxW) / 2;
        int boxY = (screenH - boxH) / 2;
        
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(boxX + 5, boxY + 5, boxW, boxH, 20, 20);
        
        g2d.setColor(new Color(50, 30, 30));
        g2d.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g2d.setColor(new Color(200, 50, 50));
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        
        // Title with effect
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 60));
        String title = "GAME OVER";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        int titleX = boxX + (boxW - titleW) / 2;
        int titleY = boxY + 90;
        
        g2d.setColor(new Color(255, 0, 0, 80));
        g2d.drawString(title, titleX + 2, titleY + 2);
        g2d.setColor(new Color(255, 100, 100));
        g2d.drawString(title, titleX, titleY);
        
        // Instructions
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.PLAIN, 20));
        String restart = "Press ENTER to Try Again";
        int restartW = g2d.getFontMetrics().stringWidth(restart);
        g2d.drawString(restart, boxX + (boxW - restartW) / 2, boxY + 170);
        
        String menu = "Press ESC for Main Menu";
        int menuW = g2d.getFontMetrics().stringWidth(menu);
        g2d.drawString(menu, boxX + (boxW - menuW) / 2, boxY + 210);
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (keyCode == KeyEvent.VK_ENTER) {
            gm.setState(gm.DIFFICULTY_SELECTION_STATE, com.bomberman.core.TransitionManager.TransitionType.FADE);
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            gm.setState(gm.MAIN_MENU_STATE, com.bomberman.core.TransitionManager.TransitionType.FADE);
        }
    }
}
