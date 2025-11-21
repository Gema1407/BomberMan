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
        int screenW = GameManager.GRID_W * GameManager.TILE_SIZE;
        int screenH = GameManager.GRID_H * GameManager.TILE_SIZE;

        // Background Gradient
        java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, new Color(45, 52, 54), 0,
                screenH, new Color(0, 0, 0));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, screenW, screenH);

        // Title
        g2d.setColor(new Color(255, 215, 0)); // Gold
        g2d.setFont(new Font("Consolas", Font.BOLD, 48));
        String title = "TOP SCORES";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (screenW - titleW) / 2, 80);

        // Table Dimensions
        int tableW = 700;
        int tableX = (screenW - tableW) / 2;
        int headerY = 150;
        
        // Header Background with gradient
        java.awt.GradientPaint headerGp = new java.awt.GradientPaint(
            tableX, headerY - 30, new Color(70, 130, 180, 150),
            tableX, headerY + 10, new Color(100, 149, 237, 120)
        );
        g2d.setPaint(headerGp);
        g2d.fillRoundRect(tableX, headerY - 30, tableW, 40, 10, 10);

        // Header Text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Consolas", Font.BOLD, 22));
        g2d.drawString("RANK", tableX + 30, headerY);
        g2d.drawString("NAME", tableX + 200, headerY);
        g2d.drawString("TIME", tableX + 550, headerY);
        
        g2d.setColor(new Color(100, 149, 237));
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawLine(tableX, headerY + 12, tableX + tableW, headerY + 12);

        // List
        g2d.setFont(new Font("Consolas", Font.PLAIN, 22));
        List<Map.Entry<String, Integer>> topScores = leaderboardManager.getTopScores();
        int y = headerY + 50;
        int count = 0;
        
        for (Map.Entry<String, Integer> entry : topScores) {
            if (count >= 10) break;

            int rowH = 42;
            // Alternating row color with rounded corners
            if (count % 2 == 0) {
                g2d.setColor(new Color(255, 255, 255, 15));
                g2d.fillRoundRect(tableX + 5, y - 28, tableW - 10, rowH, 8, 8);
            }

            // Rank Color
            if (count == 0) g2d.setColor(new Color(255, 215, 0)); // Gold
            else if (count == 1) g2d.setColor(new Color(192, 192, 192)); // Silver
            else if (count == 2) g2d.setColor(new Color(205, 127, 50)); // Bronze
            else g2d.setColor(new Color(220, 220, 240));

            g2d.setFont(new Font("Consolas", Font.BOLD, 22));
            g2d.drawString("#" + (count + 1), tableX + 30, y);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Consolas", Font.PLAIN, 22));
            g2d.drawString(entry.getKey(), tableX + 200, y);
            
            // Format time
            int time = entry.getValue();
            String timeStr = String.format("%02d:%02d", time / 60, time % 60);
            g2d.setColor(new Color(100, 200, 100));
            g2d.drawString(timeStr, tableX + 550, y);

            y += 48;
            count++;
        }
        
        // Empty State
        if (topScores.isEmpty()) {
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Consolas", Font.ITALIC, 20));
            String noScores = "No scores yet. Be the first!";
            int nsW = g2d.getFontMetrics().stringWidth(noScores);
            g2d.drawString(noScores, (screenW - nsW) / 2, headerY + 100);
        }

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Consolas", Font.BOLD, 16));
        String footer = "Press ESC to Return";
        int footerW = g2d.getFontMetrics().stringWidth(footer);
        g2d.drawString(footer, (screenW - footerW) / 2, screenH - 50);
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            gm.setState(gm.MAIN_MENU_STATE, com.bomberman.core.TransitionManager.TransitionType.SLIDE_RIGHT);
        }
    }
}
