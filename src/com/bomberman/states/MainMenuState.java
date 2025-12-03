package com.bomberman.states;

import com.bomberman.core.GameManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class MainMenuState implements GameState {
    private int selection = 0;
    private final String[] options = { "Start Game", "Leaderboard", "Settings", "Exit" };
    private boolean showExitConfirmation = false;

    @Override
    public void update(GameManager gm) {
    }

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        // Background Gradient
        java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, new Color(20, 20, 30), 0,
                GameManager.GRID_H * GameManager.TILE_SIZE, new Color(5, 5, 10));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, GameManager.GRID_W * GameManager.TILE_SIZE, GameManager.GRID_H * GameManager.TILE_SIZE);

        // Decorative Grid
        g2d.setColor(new Color(255, 255, 255, 10));
        for(int i=0; i<GameManager.GRID_W; i++) {
            g2d.drawLine(i*GameManager.TILE_SIZE, 0, i*GameManager.TILE_SIZE, GameManager.GRID_H*GameManager.TILE_SIZE);
        }
        for(int i=0; i<GameManager.GRID_H; i++) {
            g2d.drawLine(0, i*GameManager.TILE_SIZE, GameManager.GRID_W*GameManager.TILE_SIZE, i*GameManager.TILE_SIZE);
        }

        // Title Animation


        
        Font titleFont = new Font("Consolas", Font.BOLD, 60);
        g2d.setFont(titleFont);
        String title = "BOMBER QUEST";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        int centerX = (GameManager.GRID_W * GameManager.TILE_SIZE) / 2;
        
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(title, centerX - titleW/2 + 5, 155);

        // Main Title
        g2d.setColor(new Color(255, 100, 100));
        g2d.drawString(title, centerX - titleW/2, 150);
        
        // Subtitle
        g2d.setFont(new Font("Consolas", Font.BOLD, 20));
        g2d.setColor(Color.CYAN);
        String sub = "RETRO EDITION";
        int subW = g2d.getFontMetrics().stringWidth(sub);
        g2d.drawString(sub, centerX - subW/2, 190);

        // Menu Options
        g2d.setFont(new Font("Consolas", Font.BOLD, 28));
        int startY = 320;
        for (int i = 0; i < options.length; i++) {
            int y = startY + i * 70;
            
            if (i == selection) {
                // Selected Item Box with shadow
                int boxW = 350;
                int boxH = 55;
                int boxX = centerX - boxW/2;
                int boxY = y - 38;
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillRoundRect(boxX + 3, boxY + 3, boxW, boxH, 12, 12);
                
                // Box with gradient
                java.awt.GradientPaint gp2 = new java.awt.GradientPaint(
                    boxX, boxY, new Color(70, 130, 180, 100),
                    boxX, boxY + boxH, new Color(100, 149, 237, 120)
                );
                g2d.setPaint(gp2);
                g2d.fillRoundRect(boxX, boxY, boxW, boxH, 12, 12);
                
                g2d.setColor(new Color(100, 149, 237));
                g2d.setStroke(new java.awt.BasicStroke(2));
                g2d.drawRoundRect(boxX, boxY, boxW, boxH, 12, 12);
                
                g2d.setColor(Color.YELLOW);
                
                // Arrow
                g2d.drawString(">", boxX - 35, y);
                g2d.drawString("<", boxX + boxW + 15, y);
            } else {
                g2d.setColor(new Color(180, 180, 200));
            }
            
            String opt = options[i];
            int optW = g2d.getFontMetrics().stringWidth(opt);
            g2d.drawString(opt, centerX - optW/2, y);
        }

        g2d.setFont(new Font("Consolas", Font.ITALIC, 12));
        g2d.setColor(Color.DARK_GRAY);
        String footer = "v1.0 | Use Arrow Keys & Enter";
        int footerW = g2d.getFontMetrics().stringWidth(footer);
        g2d.drawString(footer, centerX - footerW/2, GameManager.GRID_H * GameManager.TILE_SIZE - 20);

        // Exit Confirmation Overlay
        if (showExitConfirmation) {
            g2d.setColor(new Color(0, 0, 0, 220));
            g2d.fillRect(0, 0, GameManager.GRID_W * GameManager.TILE_SIZE, GameManager.GRID_H * GameManager.TILE_SIZE);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Consolas", Font.BOLD, 30));
            String msg = "EXIT GAME?";
            int msgWidth = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (GameManager.GRID_W * GameManager.TILE_SIZE - msgWidth) / 2, 300);
            
            g2d.setFont(new Font("Consolas", Font.PLAIN, 20));
            String subMsg = "[ENTER] Confirm   [ESC] Cancel";
            int subMsgWidth = g2d.getFontMetrics().stringWidth(subMsg);
            g2d.drawString(subMsg, (GameManager.GRID_W * GameManager.TILE_SIZE - subMsgWidth) / 2, 350);
        }
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (showExitConfirmation) {
            if (keyCode == KeyEvent.VK_ENTER) {
                System.exit(0);
            } else if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_N) {
                showExitConfirmation = false;
            }
            return;
        }

        if (keyCode == KeyEvent.VK_UP) {
            selection--;
            if (selection < 0)
                selection = options.length - 1;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= options.length)
                selection = 0;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            if (selection == 0) {
                gm.setState(gm.DIFFICULTY_SELECTION_STATE, com.bomberman.core.TransitionManager.TransitionType.FADE);
            } else if (selection == 1) {
                gm.setState(gm.LEADERBOARD_STATE, com.bomberman.core.TransitionManager.TransitionType.SLIDE_LEFT);
            } else if (selection == 2) {
                gm.setState(gm.SETTINGS_STATE, com.bomberman.core.TransitionManager.TransitionType.SLIDE_RIGHT);
            } else if (selection == 3) {
                showExitConfirmation = true;
            }
        }
    }
}
