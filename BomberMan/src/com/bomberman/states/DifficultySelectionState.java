package com.bomberman.states;

import com.bomberman.core.GameManager;
import com.bomberman.managers.SettingsManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class DifficultySelectionState implements GameState {
    private int selection = 1; // Default to Medium
    private final String[] options = { "EASY", "MEDIUM", "HARD" };
    private final String[] descriptions = {
        "For beginners. Fewer enemies, slower pace.",
        "The standard experience. Balanced challenge.",
        "For experts. Many fast enemies. Good luck!"
    };

    @Override
    public void update(GameManager gm) {}

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        // Background
        g2d.setColor(new Color(20, 20, 30));
        g2d.fillRect(0, 0, GameManager.GRID_W * GameManager.TILE_SIZE, GameManager.GRID_H * GameManager.TILE_SIZE);

        // Title
        g2d.setColor(Color.ORANGE);
        g2d.setFont(new Font("Consolas", Font.BOLD, 40));
        g2d.drawString("SELECT DIFFICULTY", 100, 100);

        // Options
        g2d.setFont(new Font("Consolas", Font.BOLD, 32));
        for (int i = 0; i < options.length; i++) {
            int y = 250 + i * 80;
            
            if (i == selection) {
                g2d.setColor(Color.YELLOW);
                g2d.drawString("> " + options[i], 150, y);
                
                // Description
                g2d.setFont(new Font("Consolas", Font.ITALIC, 18));
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawString(descriptions[i], 150, y + 30);
                g2d.setFont(new Font("Consolas", Font.BOLD, 32)); // Reset font
            } else {
                g2d.setColor(Color.GRAY);
                g2d.drawString(options[i], 150, y);
            }
        }

        g2d.setFont(new Font("Consolas", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Press ENTER to Start, ESC to Back", 100, 600);
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
            if (selection == 1) diff = SettingsManager.Difficulty.MEDIUM;
            if (selection == 2) diff = SettingsManager.Difficulty.HARD;
            
            gm.setDifficulty(diff);
            gm.resetGame();
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            gm.setState(gm.MAIN_MENU_STATE);
        }
    }
}
