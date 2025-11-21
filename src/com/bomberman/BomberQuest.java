package com.bomberman;

import com.bomberman.core.GameManager;
import com.bomberman.exceptions.GameInitializationException;
import com.bomberman.managers.SettingsManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BomberQuest extends JPanel implements ActionListener {
    private Timer timer;
    private GameManager gameManager;
    private static JFrame frame;
    private boolean wasFullscreen = false;
    
    // FPS Tracking
    private long lastFpsTime = System.currentTimeMillis();
    private int frameCount = 0;
    private int currentFps = 60;

    public BomberQuest() {
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        updatePreferredSize();
        
        try {
            gameManager = GameManager.getInstance();
            gameManager.init();
            gameManager.setSettingsListener(() -> checkSettingsChange());
        } catch (GameInitializationException e) {
            System.err.println("Critical Error: " + e.getMessage());
            System.exit(1);
        }

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameManager.handleInput(e.getKeyCode());
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                gameManager.handleKeyReleased(e.getKeyCode());
            }
        });

        // Game Loop
        timer = new Timer(16, this); // ~60FPS
        timer.start();
    }

    private void updatePreferredSize() {
        SettingsManager.Resolution res = SettingsManager.getInstance().getResolution();
        this.setPreferredSize(new Dimension(res.getWidth(), res.getHeight()));
    }

    private void checkSettingsChange() {
        SettingsManager sm = SettingsManager.getInstance();
        SettingsManager.Resolution res = sm.getResolution();
        boolean isFullscreen = sm.isFullscreen();

        // Check Resolution Change
        if (!isFullscreen && (getWidth() != res.getWidth() || getHeight() != res.getHeight())) {
            updatePreferredSize();
            if (frame != null) {
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
        }
        
        // Check Fullscreen Change
        if (wasFullscreen != isFullscreen) {
            wasFullscreen = isFullscreen;
            toggleFullscreen(isFullscreen);
        }
    }
    
    private void toggleFullscreen(boolean fullscreen) {
        if (frame == null) return;
        
        // Stop rendering during transition to prevent flicker
        timer.stop();
        
        frame.setVisible(false);
        frame.dispose();
        
        // Wait a moment for cleanup
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        frame.setUndecorated(fullscreen);
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        if (fullscreen) {
            gd.setFullScreenWindow(frame);
        } else {
            gd.setFullScreenWindow(null);
            // Apply any pending resolution changes when exiting fullscreen
            updatePreferredSize();
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        
        frame.setVisible(true);
        this.requestFocus();
        
        // Resume rendering
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Game Content Size
        int gameW = GameManager.GRID_W * GameManager.TILE_SIZE;
        int gameH = GameManager.GRID_H * GameManager.TILE_SIZE;
        
        // Window Size
        int winW = getWidth();
        int winH = getHeight();
        
        // Calculate Scale (Letterboxing)
        double scaleX = (double) winW / gameW;
        double scaleY = (double) winH / gameH;
        double scale = Math.min(scaleX, scaleY);
        
        // Calculate Offset to Center
        int offsetX = (int) ((winW - (gameW * scale)) / 2);
        int offsetY = (int) ((winH - (gameH * scale)) / 2);
        
        // Apply Transform
        g2d.translate(offsetX, offsetY);
        g2d.scale(scale, scale);
        
        // Clip to game area to prevent drawing outside
        g2d.setClip(0, 0, gameW, gameH);

        // Rendering Hints for Retro Look (Nearest Neighbor)
        if (SettingsManager.getInstance().isRetroEffects()) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        gameManager.render(g2d);
        
        // FPS Counter
        if (SettingsManager.getInstance().isShowFPS()) {
            frameCount++;
            long now = System.currentTimeMillis();
            if (now - lastFpsTime >= 1000) {
                currentFps = frameCount;
                frameCount = 0;
                lastFpsTime = now;
            }
            
            g2d.setTransform(new java.awt.geom.AffineTransform()); // Reset transform for UI
            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("Consolas", Font.BOLD, 12));
            g2d.drawString("FPS: " + currentFps, 10, 20);
        }
        
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameManager.update();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("BomberQuest Retro");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            // Cleanup on close
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    com.bomberman.managers.SoundManager.getInstance().stopMusic();
                }
            });
            
            // Detect Monitor Resolution for Initial Setup if needed, 
            // but we start with default resolution from SettingsManager
            
            BomberQuest game = new BomberQuest();
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}