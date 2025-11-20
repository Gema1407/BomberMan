package com.bomberman.states;

import com.bomberman.core.GameManager;
import com.bomberman.managers.LeaderboardManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

public class LeaderboardState implements GameState {
    private LeaderboardManager leaderboardManager;

    public LeaderboardState() {
        leaderboardManager = LeaderboardManager.getInstance();
    }

    @Override
    public void update(GameManager gm) {
    }

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        // Background Gradient
        java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, new Color(45, 52, 54), 0,
                GameManager.GRID_H * GameManager.TILE_SIZE, new Color(0, 0, 0));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, GameManager.GRID_W * GameManager.TILE_SIZE, GameManager.GRID_H * GameManager.TILE_SIZE);

        // Title
        g2d.setColor(new Color(255, 215, 0)); // Gold
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("TOP SCORES", 190, 80);

        // Header
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Rank", 150, 130);
        g2d.drawString("Name", 250, 130);
        g2d.drawString("Time", 450, 130);
        g2d.drawLine(140, 140, 500, 140);

        // List
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 18));
        List<Map.Entry<String, Integer>> topScores = leaderboardManager.getTopScores();
        int y = 170;
        int count = 0;
        for (Map.Entry<String, Integer> entry : topScores) {
            if (count >= 10)
                break;

            // Alternating row color
            if (count % 2 == 0)
                g2d.setColor(new Color(255, 255, 255, 20));
            else
                g2d.setColor(new Color(0, 0, 0, 0));
            g2d.fillRect(140, y - 20, 360, 30);

            // Rank Color
            if (count == 0)
                g2d.setColor(new Color(255, 215, 0)); // Gold
            else if (count == 1)
                g2d.setColor(new Color(192, 192, 192)); // Silver
            else if (count == 2)
                g2d.setColor(new Color(205, 127, 50)); // Bronze
            else
                g2d.setColor(Color.WHITE);

            g2d.drawString("#" + (count + 1), 150, y);
            g2d.setColor(Color.WHITE);
            g2d.drawString(entry.getKey(), 250, y);
            g2d.drawString(entry.getValue() + "s", 450, y);

            y += 35;
            count++;
        }

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Press ESC to Return", 220, 550);
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            gm.setState(gm.MAIN_MENU_STATE);
        }
    }
}
