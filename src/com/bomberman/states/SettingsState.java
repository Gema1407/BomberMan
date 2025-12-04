package com.bomberman.states;

import com.bomberman.core.GameManager;
import com.bomberman.managers.SettingsManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class SettingsState implements GameState {
    private int selection = 0;
    private final String[] options = { "Resolution", "Fullscreen", "Music Vol", "SFX Vol", "Retro Effects", "Show FPS", "Apply", "Back" };
    private static final String FONT_NAME_DEFAULT = "Consolas";
    
    // Pending changes
    private SettingsManager.Resolution pendingResolution;
    private boolean pendingFullscreen;
    private int pendingMusicVol;
    private int pendingSFXVol;
    private boolean pendingRetroEffects;
    private boolean pendingShowFPS;
    
    private boolean initialized = false;

    private void init() {
        SettingsManager sm = SettingsManager.getInstance();
        com.bomberman.managers.SoundManager sound = com.bomberman.managers.SoundManager.getInstance();
        
        pendingResolution = sm.getResolution();
        pendingFullscreen = sm.isFullscreen();
        pendingMusicVol = sound.getMusicVolume();
        pendingSFXVol = sound.getSFXVolume();
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
        int panelW = 700;
        int panelH = 580;
        int panelX = (GameManager.GRID_W * GameManager.TILE_SIZE - panelW) / 2;
        int panelY = (GameManager.GRID_H * GameManager.TILE_SIZE - panelH) / 2;
        
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(panelX + 5, panelY + 5, panelW, panelH, 20, 20);
        
        g2d.setColor(new Color(35, 40, 50));
        g2d.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2d.setColor(new Color(100, 149, 237));
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        // Title
        g2d.setColor(Color.CYAN);
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 40));
        String title = "CONFIGURATION";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, panelX + (panelW - titleW) / 2, panelY + 60);

        // Options
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 22));
        
        for (int i = 0; i < options.length; i++) {
            int y = panelY + 130 + i * 55;
            int x = panelX + 60;
            String text = options[i];
            
            // Draw Selection Highlight with gradient
            if (i == selection) {
                g2d.setColor(new Color(70, 130, 180, 80));
                g2d.fillRoundRect(x - 15, y - 32, panelW - 100, 45, 8, 8);
                g2d.setColor(new Color(100, 149, 237));
                g2d.drawRoundRect(x - 15, y - 32, panelW - 100, 45, 8, 8);
                g2d.setColor(Color.YELLOW);
            } else {
                g2d.setColor(new Color(200, 200, 220));
            }
            
            g2d.drawString(text, x, y);
            
            // Draw Values
            g2d.setColor(i == selection ? Color.WHITE : new Color(180, 180, 200));
            int valX = panelX + 380;
            
            switch (i) {
                case 0: // Resolution
                    String resText = "< " + pendingResolution + " >";
                    if (pendingFullscreen) {
                        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.PLAIN, 18));
                        g2d.drawString(resText, valX, y);
                        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.ITALIC, 12));
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.drawString("(Takes effect on exit fullscreen)", valX, y + 15);
                        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 22));
                    } else {
                        g2d.drawString(resText, valX, y);
                    }
                    break;
                case 1: // Fullscreen
                    drawCheckbox(g2d, valX, y - 20, pendingFullscreen);
                    break;
                case 2: // Music Vol
                    drawSlider(g2d, valX, y - 20, pendingMusicVol);
                    break;
                case 3: // SFX Vol
                    drawSlider(g2d, valX, y - 20, pendingSFXVol);
                    break;
                case 4: // Retro Effects
                    drawCheckbox(g2d, valX, y - 20, pendingRetroEffects);
                    break;
                case 5: // Show FPS
                    drawCheckbox(g2d, valX, y - 20, pendingShowFPS);
                    break;
                case 6: // Apply
                    if (i == selection) g2d.drawString("[ ENTER ]", valX, y);
                    break;
                case 7: // Back
                    if (i == selection) g2d.drawString("[ ESC ]", valX, y);
                    break;
                default:
                    break;
            }
        }
        
        // Footer
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.ITALIC, 14));
        g2d.setColor(Color.GRAY);
        String footer = "Press ENTER to Toggle/Apply, ARROWS to Navigate";
        int footerW = g2d.getFontMetrics().stringWidth(footer);
        g2d.drawString(footer, panelX + (panelW - footerW) / 2, panelY + panelH - 20);
    }
    
    private void drawCheckbox(Graphics2D g2d, int x, int y, boolean checked) {
        // Box
        g2d.setColor(new Color(50, 50, 60));
        g2d.fillRoundRect(x, y, 24, 24, 4, 4);
        
        g2d.setColor(new Color(150, 150, 170));
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRoundRect(x, y, 24, 24, 4, 4);
        
        // Checkmark
        if (checked) {
            g2d.setColor(new Color(100, 200, 100));
            g2d.setStroke(new java.awt.BasicStroke(3));
            g2d.drawLine(x + 5, y + 12, x + 10, y + 17);
            g2d.drawLine(x + 10, y + 17, x + 19, y + 7);
        }
    }
    
    private void drawSlider(Graphics2D g2d, int x, int y, int value) {
        int barW = 150;
        int barH = 12;
        
        // Bar Background
        g2d.setColor(new Color(50, 50, 60));
        g2d.fillRoundRect(x, y + 5, barW, barH, 6, 6);
        
        // Fill with gradient effect
        if (value > 0) {
            java.awt.GradientPaint gp = new java.awt.GradientPaint(
                x, y, new Color(100, 200, 100),
                x + (float) barW, y, new Color(50, 150, 50)
            );
            g2d.setPaint(gp);
            g2d.fillRoundRect(x, y + 5, (int)(barW * (value / 100.0)), barH, 6, 6);
        }
        
        // Border
        g2d.setColor(new Color(150, 150, 170));
        g2d.setStroke(new java.awt.BasicStroke(1.5f));
        g2d.drawRoundRect(x, y + 5, barW, barH, 6, 6);
        
        // Text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(FONT_NAME_DEFAULT, Font.BOLD, 16));
        g2d.drawString(value + "%", x + barW + 15, y + 16);
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
            if (selection == 0) { // Resolution (allow change even in fullscreen)
                SettingsManager.Resolution[] ress = SettingsManager.Resolution.values();
                int nextRes = (pendingResolution.ordinal() + dir + ress.length) % ress.length;
                pendingResolution = ress[nextRes];
            } else if (selection == 2) { // Music Vol
                pendingMusicVol += dir * 10;
                if (pendingMusicVol < 0) pendingMusicVol = 0;
                if (pendingMusicVol > 100) pendingMusicVol = 100;
            } else if (selection == 3) { // SFX Vol
                pendingSFXVol += dir * 10;
                if (pendingSFXVol < 0) pendingSFXVol = 0;
                if (pendingSFXVol > 100) pendingSFXVol = 100;
            }
        } else if (keyCode == KeyEvent.VK_ENTER) {
            if (selection == 1) { // Fullscreen
                pendingFullscreen = !pendingFullscreen;
            } else if (selection == 4) { // Retro Effects
                pendingRetroEffects = !pendingRetroEffects;
            } else if (selection == 5) { // Show FPS
                pendingShowFPS = !pendingShowFPS;
            } else if (selection == 6) { // Apply
                applySettings(gm);
            } else if (selection == 7) { // Back
                gm.returnToPreviousState();
                // Reset pending to actual
                init(); 
            }
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            gm.returnToPreviousState();
            init();
        }
    }
    
    private void applySettings(GameManager gm) {
        SettingsManager sm = SettingsManager.getInstance();
        sm.setResolution(pendingResolution);
        sm.setFullscreen(pendingFullscreen);
        sm.setRetroEffects(pendingRetroEffects);
        sm.setShowFPS(pendingShowFPS);
        
        com.bomberman.managers.SoundManager sound = com.bomberman.managers.SoundManager.getInstance();
        sound.setMusicVolume(pendingMusicVol);
        sound.setSFXVolume(pendingSFXVol);
        
        gm.notifySettingsApplied();
        
        // Visual feedback could be added here
    }
    
    @Override
    public void handleKeyReleased(int keyCode, GameManager gm) {
        // Handling when the key released
    }
}
