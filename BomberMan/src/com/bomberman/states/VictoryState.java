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

    @Override
    public void update(GameManager gm) {}

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        gm.PLAYING_STATE.render(g2d, gm);
        
        int screenW = GameManager.GRID_W * GameManager.TILE_SIZE;
        int screenH = GameManager.GRID_H * GameManager.TILE_SIZE;

        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(0, 0, screenW, screenH);
        
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Consolas", Font.BOLD, 60));
        String title = "VICTORY!";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (screenW - titleW) / 2, screenH / 3);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Consolas", Font.PLAIN, 24));
        String timeStr = "Time: " + gm.getLastGameTime() + "s";
        int timeW = g2d.getFontMetrics().stringWidth(timeStr);
        g2d.drawString(timeStr, (screenW - timeW) / 2, screenH / 3 + 50);
        
        if (!saved) {
            String prompt = "Enter Name: " + username.toString() + "_";
            int promptW = g2d.getFontMetrics().stringWidth(prompt);
            g2d.drawString(prompt, (screenW - promptW) / 2, screenH / 2 + 20);
            
            g2d.setFont(new Font("Consolas", Font.ITALIC, 16));
            g2d.setColor(Color.LIGHT_GRAY);
            String hint = "Press ENTER to Save";
            int hintW = g2d.getFontMetrics().stringWidth(hint);
            g2d.drawString(hint, (screenW - hintW) / 2, screenH / 2 + 60);
            
            if (!errorMessage.isEmpty()) {
                g2d.setColor(Color.RED);
                int errW = g2d.getFontMetrics().stringWidth(errorMessage);
                g2d.drawString(errorMessage, (screenW - errW) / 2, screenH / 2 + 100);
            }
        } else {
            g2d.setColor(Color.YELLOW);
            String savedMsg = "Score Saved!";
            int savedW = g2d.getFontMetrics().stringWidth(savedMsg);
            g2d.drawString(savedMsg, (screenW - savedW) / 2, screenH / 2 + 20);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Consolas", Font.PLAIN, 20));
            String playMsg = "Press ENTER to Play Again";
            int playW = g2d.getFontMetrics().stringWidth(playMsg);
            g2d.drawString(playMsg, (screenW - playW) / 2, screenH / 2 + 80);
            
            String menuMsg = "Press ESC for Menu";
            int menuW = g2d.getFontMetrics().stringWidth(menuMsg);
            g2d.drawString(menuMsg, (screenW - menuW) / 2, screenH / 2 + 110);
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
            } else {
                char c = (char) keyCode;
                if (Character.isLetterOrDigit(c) || c == ' ') {
                    if (username.length() < 12) username.append(c);
                    errorMessage = "";
                }
            }
        } else {
            if (keyCode == KeyEvent.VK_ENTER) {
                gm.setState(gm.DIFFICULTY_SELECTION_STATE); // Go to difficulty select instead of instant reset
                reset();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                gm.setState(gm.MAIN_MENU_STATE);
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
