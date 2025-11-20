package com.bomberman.states;

import com.bomberman.core.GameManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameOverState implements GameState {
    @Override
    public void update(GameManager gm) {}

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        gm.PLAYING_STATE.render(g2d, gm); // Render background game
        
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, GameManager.GRID_W * GameManager.TILE_SIZE, GameManager.GRID_H * GameManager.TILE_SIZE);
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.drawString("GAME OVER", 180, 300);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Press ENTER to Restart", 200, 350);
        g2d.drawString("Press ESC for Menu", 210, 380);
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (keyCode == KeyEvent.VK_ENTER) {
            gm.resetGame();
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            gm.setState(gm.MAIN_MENU_STATE);
        }
    }
}
