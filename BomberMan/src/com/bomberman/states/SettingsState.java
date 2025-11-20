package com.bomberman.states;

import com.bomberman.core.GameManager;
import com.bomberman.managers.SettingsManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class SettingsState implements GameState {
    private int selection = 0;
    private final String[] options = { "Resolution", "Fullscreen", "Retro Effects", "Show FPS", "Apply", "Back" };
    
    // Pending changes
    private SettingsManager.Resolution pendingResolution;
    private boolean pendingFullscreen;
    private boolean pendingRetroEffects;
    private boolean pendingShowFPS;
    
    private boolean initialized = false;

    private void init() {
        SettingsManager sm = SettingsManager.getInstance();
        pendingResolution = sm.getResolution();
        pendingFullscreen = sm.isFullscreen();
        pendingRetroEffects = sm.isRetroEffects();
        pendingShowFPS = sm.isShowFPS();
        initialized = true;
    }

    @Override
    public void update(GameManager gm) {
        if (!initialized) init();
    }

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        if (!initialized) init();

        // Background
        g2d.setColor(new Color(20, 20, 30));
        g2d.fillRect(0, 0, GameManager.GRID_W * GameManager.TILE_SIZE, GameManager.GRID_H * GameManager.TILE_SIZE);

        // Panel Background
        int panelW = 600;
        int panelH = 500;
        int panelX = (GameManager.GRID_W * GameManager.TILE_SIZE - panelW) / 2;
        int panelY = (GameManager.GRID_H * GameManager.TILE_SIZE - panelH) / 2;
        
        g2d.setColor(new Color(40, 40, 50));
        g2d.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2d.setColor(new Color(100, 100, 120));
        g2d.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        // Title
        g2d.setColor(Color.CYAN);
        g2d.setFont(new Font("Consolas", Font.BOLD, 40));
        String title = "CONFIGURATION";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, panelX + (panelW - titleW) / 2, panelY + 60);

        // Options
        g2d.setFont(new Font("Consolas", Font.PLAIN, 24));
        
        for (int i = 0; i < options.length; i++) {
            int y = panelY + 140 + i * 50;
            int x = panelX + 50;
            String text = options[i];
            
            // Draw Selection Highlight
            if (i == selection) {
                g2d.setColor(new Color(60, 60, 80));
                g2d.fillRect(x - 10, y - 30, panelW - 80, 40);
                g2d.setColor(Color.YELLOW);
            } else {
                g2d.setColor(Color.LIGHT_GRAY);
            }
            
            g2d.drawString(text, x, y);
            
            // Draw Values
            g2d.setColor(i == selection ? Color.WHITE : Color.GRAY);
            int valX = panelX + 350;
            
            switch (i) {
                case 0: // Resolution
                    g2d.drawString("< " + pendingResolution + " >", valX, y);
                    break;
                case 1: // Fullscreen
                    drawCheckbox(g2d, valX, y - 20, pendingFullscreen);
                    break;
                case 2: // Retro Effects
                    drawCheckbox(g2d, valX, y - 20, pendingRetroEffects);
                    break;
                case 3: // Show FPS
                    drawCheckbox(g2d, valX, y - 20, pendingShowFPS);
                    break;
                case 4: // Apply
                    if (i == selection) g2d.drawString("[ ENTER ]", valX, y);
                    break;
                case 5: // Back
                    if (i == selection) g2d.drawString("[ ESC ]", valX, y);
                    break;
            }
        }
        
        // Footer
        g2d.setFont(new Font("Consolas", Font.ITALIC, 14));
        g2d.setColor(Color.GRAY);
        String footer = "Press ENTER to Toggle/Apply, ARROWS to Navigate";
        int footerW = g2d.getFontMetrics().stringWidth(footer);
        g2d.drawString(footer, panelX + (panelW - footerW) / 2, panelY + panelH - 20);
    }
    
    private void drawCheckbox(Graphics2D g2d, int x, int y, boolean checked) {
        g2d.drawRect(x, y, 20, 20);
        if (checked) {
            g2d.fillRect(x + 4, y + 4, 12, 12);
        }
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (!initialized) init();

        if (keyCode == KeyEvent.VK_UP) {
            selection--;
            if (selection < 0) selection = options.length - 1;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            selection++;
            if (selection >= options.length) selection = 0;
        } else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            int dir = (keyCode == KeyEvent.VK_RIGHT) ? 1 : -1;
            if (selection == 0) { // Resolution
                SettingsManager.Resolution[] ress = SettingsManager.Resolution.values();
                int nextRes = (pendingResolution.ordinal() + dir + ress.length) % ress.length;
                pendingResolution = ress[nextRes];
            }
        } else if (keyCode == KeyEvent.VK_ENTER) {
            if (selection == 1) { // Fullscreen
                pendingFullscreen = !pendingFullscreen;
            } else if (selection == 2) { // Retro Effects
                pendingRetroEffects = !pendingRetroEffects;
            } else if (selection == 3) { // Show FPS
                pendingShowFPS = !pendingShowFPS;
            } else if (selection == 4) { // Apply
                applySettings(gm);
            } else if (selection == 5) { // Back
                gm.setState(gm.MAIN_MENU_STATE);
                // Reset pending to actual
                init(); 
            }
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            gm.setState(gm.MAIN_MENU_STATE);
            init();
        }
    }
    
    private void applySettings(GameManager gm) {
        SettingsManager sm = SettingsManager.getInstance();
        sm.setResolution(pendingResolution);
        sm.setFullscreen(pendingFullscreen);
        sm.setRetroEffects(pendingRetroEffects);
        sm.setShowFPS(pendingShowFPS);
        
        gm.notifySettingsApplied();
        
        // Visual feedback could be added here
    }
    
    @Override
    public void handleKeyReleased(int keyCode, GameManager gm) {}
}
