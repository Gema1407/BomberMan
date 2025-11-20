package com.bomberman.states;

import com.bomberman.core.GameManager;
import com.bomberman.core.EntityFactory;
import com.bomberman.entities.*;
import com.bomberman.managers.SoundManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PlayingState implements GameState {
    private Set<Integer> pressedKeys = new HashSet<>();
    private int moveDelay = 0;
    private final int MOVE_DELAY_MAX = 8; // Adjust for speed (lower = faster)
    
    // Pause Menu
    private boolean paused = false;
    private int pauseSelection = 0;
    private final String[] pauseOptions = { "Resume", "Settings", "Main Menu", "Exit to Desktop" };

    @Override
    public void update(GameManager gm) {
        if (paused) return;

        Player p = gm.getPlayer();
        p.update();
        
        // Handle Continuous Movement
        handleMovement(gm);

        // Update Bombs
        Iterator<GameObject> bombIt = gm.getBombs().iterator();
        while (bombIt.hasNext()) {
            Bomb b = (Bomb) bombIt.next();
            b.update();
            if (!b.isActive()) {
                triggerExplosion(gm, b.getX(), b.getY(), b.getRadius());
                bombIt.remove();
                SoundManager.getInstance().playSFX(SoundManager.SFX_EXPLOSION);
            }
        }

        // Update Explosions
        Iterator<GameObject> expIt = gm.getExplosions().iterator();
        while (expIt.hasNext()) {
            Explosion e = (Explosion) expIt.next();
            e.update();

            // Check Player Hit
            if (e.getX() == p.getX() && e.getY() == p.getY()) {
                p.damage();
                SoundManager.getInstance().playSFX(SoundManager.SFX_DEATH);
            }

            // Check Enemy Hit
            Iterator<GameObject> enIt = gm.getEnemies().iterator();
            while (enIt.hasNext()) {
                Enemy en = (Enemy) enIt.next();
                if (e.getX() == en.getX() && e.getY() == en.getY()) {
                    enIt.remove();
                }
            }

            if (!e.isActive())
                expIt.remove();
        }

        // Update Enemies
        for (GameObject obj : gm.getEnemies()) {
            Enemy e = (Enemy) obj;
            e.tryMove(gm.getWalls(), gm.getEnemies(), gm.getBombs(), gm.getPlayer(), GameManager.GRID_W, GameManager.GRID_H);
            if (e.getX() == p.getX() && e.getY() == p.getY()) {
                p.damage();
                SoundManager.getInstance().playSFX(SoundManager.SFX_DEATH);
            }
        }

        // Game Over Conditions
        if (p.getHp() <= 0)
            gm.setState(gm.GAMEOVER_STATE);
        if (gm.getEnemies().isEmpty()) {
            gm.calculateScore(); // Calculate time taken
            gm.setState(gm.VICTORY_STATE);
            SoundManager.getInstance().playSFX(SoundManager.SFX_WIN);
        }
    }
    
    private void handleMovement(GameManager gm) {
        if (moveDelay > 0) {
            moveDelay--;
            return;
        }

        int dx = 0;
        int dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_W)) dy = -1;
        else if (pressedKeys.contains(KeyEvent.VK_DOWN) || pressedKeys.contains(KeyEvent.VK_S)) dy = 1;
        else if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) dx = -1;
        else if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) dx = 1;

        if (dx != 0 || dy != 0) {
            Player p = gm.getPlayer();
            int nx = p.getX() + dx;
            int ny = p.getY() + dy;

            // Check bomb collision (basic: can't walk into bomb)
            boolean hitBomb = false;
            for (GameObject b : gm.getBombs())
                if (b.getX() == nx && b.getY() == ny)
                    hitBomb = true;

            if (GameManager.isValidMove(nx, ny, GameManager.GRID_W, GameManager.GRID_H, gm.getWalls()) && !hitBomb) {
                p.setPosition(nx, ny);
                moveDelay = MOVE_DELAY_MAX;
            }
        }
    }

    private void triggerExplosion(GameManager gm, int bx, int by, int radius) {
        gm.getExplosions().add(new Explosion(bx, by)); // Center

        int[][] dirs = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
        for (int[] dir : dirs) {
            for (int i = 1; i <= radius; i++) {
                int tx = bx + (dir[0] * i);
                int ty = by + (dir[1] * i);

                // Bounds check
                if (tx < 0 || tx >= GameManager.GRID_W || ty < 0 || ty >= GameManager.GRID_H)
                    break;

                // Check Walls
                boolean hitHard = false;
                boolean hitSoft = false;
                Iterator<GameObject> wallIt = gm.getWalls().iterator();
                while (wallIt.hasNext()) {
                    Wall w = (Wall) wallIt.next();
                    if (w.getX() == tx && w.getY() == ty) {
                        if (w.isDestructible()) {
                            hitSoft = true;
                            wallIt.remove(); // Destroy wall
                        } else {
                            hitHard = true;
                        }
                        break;
                    }
                }

                if (hitHard)
                    break;

                gm.getExplosions().add(new Explosion(tx, ty));
                if (hitSoft)
                    break; // Stop after breaking soft wall
            }
        }
    }

    @Override
    public void render(Graphics2D g2d, GameManager gm) {
        // Draw Floor
        g2d.setColor(new Color(85, 239, 196));
        g2d.fillRect(0, 0, GameManager.GRID_W * GameManager.TILE_SIZE, GameManager.GRID_H * GameManager.TILE_SIZE);

        // Render Entities
        for (GameObject w : gm.getWalls())
            w.render(g2d, GameManager.TILE_SIZE);
        for (GameObject b : gm.getBombs())
            b.render(g2d, GameManager.TILE_SIZE);
        for (GameObject e : gm.getExplosions())
            e.render(g2d, GameManager.TILE_SIZE);
        for (GameObject e : gm.getEnemies())
            e.render(g2d, GameManager.TILE_SIZE);
        gm.getPlayer().render(g2d, GameManager.TILE_SIZE);

        // HUD
        renderHUD(g2d, gm);
        
        // Pause Menu Overlay
        if (paused) {
            renderPauseMenu(g2d, gm);
        }
    }
    
    private void renderHUD(Graphics2D g2d, GameManager gm) {
        int screenW = GameManager.GRID_W * GameManager.TILE_SIZE;
        
        // HUD Background Bar
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, screenW, 50);
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.drawLine(0, 50, screenW, 50);

        // 1. Health Section (Left)
        int hp = gm.getPlayer().getHp();
        g2d.setColor(Color.RED);
        for (int i = 0; i < 3; i++) {
            int hx = 20 + i * 35;
            int hy = 15;
            if (i < hp) {
                // Full Heart
                g2d.fillOval(hx, hy, 12, 12);
                g2d.fillOval(hx + 12, hy, 12, 12);
                int[] tx = {hx, hx + 24, hx + 12};
                int[] ty = {hy + 6, hy + 6, hy + 24};
                g2d.fillPolygon(tx, ty, 3);
            } else {
                // Empty Heart Outline
                g2d.drawOval(hx, hy, 12, 12);
                g2d.drawOval(hx + 12, hy, 12, 12);
                int[] tx = {hx, hx + 24, hx + 12};
                int[] ty = {hy + 6, hy + 6, hy + 24};
                g2d.drawPolygon(tx, ty, 3);
            }
        }

        // 2. Timer Section (Center)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Consolas", Font.BOLD, 24));
        long time = (System.currentTimeMillis() - gm.getGameStartTime()) / 1000;
        String timeStr = String.format("%02d:%02d", time / 60, time % 60);
        int timeW = g2d.getFontMetrics().stringWidth(timeStr);
        g2d.drawString(timeStr, (screenW - timeW) / 2, 32);
        
        // 3. Stats Section (Right)
        g2d.setFont(new Font("Consolas", Font.PLAIN, 16));
        String enemyStr = "ENEMIES: " + gm.getEnemies().size();
        int enemyW = g2d.getFontMetrics().stringWidth(enemyStr);
        g2d.drawString(enemyStr, screenW - enemyW - 20, 20);
        
        g2d.setColor(Color.YELLOW);
        String diffStr = gm.getDifficulty().toString();
        int diffW = g2d.getFontMetrics().stringWidth(diffStr);
        g2d.drawString(diffStr, screenW - diffW - 20, 40);
    }
    
    private void renderPauseMenu(Graphics2D g2d, GameManager gm) {
        int screenW = GameManager.GRID_W * GameManager.TILE_SIZE;
        int screenH = GameManager.GRID_H * GameManager.TILE_SIZE;
        
        // Dark Overlay
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, screenW, screenH);
        
        // Menu Box
        int boxW = 400;
        int boxH = 350;
        int boxX = (screenW - boxW) / 2;
        int boxY = (screenH - boxH) / 2;
        
        g2d.setColor(new Color(40, 40, 50));
        g2d.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        
        // Title
        g2d.setFont(new Font("Consolas", Font.BOLD, 40));
        String title = "PAUSED";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, boxX + (boxW - titleW) / 2, boxY + 60);
        
        // Options
        g2d.setFont(new Font("Consolas", Font.PLAIN, 24));
        for (int i = 0; i < pauseOptions.length; i++) {
            int optY = boxY + 130 + i * 50;
            String opt = pauseOptions[i];
            int optW = g2d.getFontMetrics().stringWidth(opt);
            int optX = boxX + (boxW - optW) / 2;
            
            if (i == pauseSelection) {
                g2d.setColor(Color.YELLOW);
                g2d.drawString("> " + opt + " <", optX - 20, optY);
            } else {
                g2d.setColor(Color.GRAY);
                g2d.drawString(opt, optX, optY);
            }
        }
    }

    @Override
    public void handleInput(int keyCode, GameManager gm) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            if (!paused) pressedKeys.clear(); // Reset keys on resume
            return;
        }

        if (paused) {
            if (keyCode == KeyEvent.VK_UP) {
                pauseSelection--;
                if (pauseSelection < 0) pauseSelection = pauseOptions.length - 1;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                pauseSelection++;
                if (pauseSelection >= pauseOptions.length) pauseSelection = 0;
            } else if (keyCode == KeyEvent.VK_ENTER) {
                if (pauseSelection == 0) { // Resume
                    paused = false;
                    pressedKeys.clear();
                } else if (pauseSelection == 1) { // Settings
                    gm.setState(gm.SETTINGS_STATE);
                } else if (pauseSelection == 2) { // Main Menu
                    gm.setState(gm.MAIN_MENU_STATE);
                    paused = false;
                } else if (pauseSelection == 3) { // Exit
                    System.exit(0);
                }
            }
            return;
        }

        pressedKeys.add(keyCode);
        
        if (keyCode == KeyEvent.VK_SPACE) {
            Player p = gm.getPlayer();
            // Place Bomb
            boolean bombExists = false;
            for (GameObject b : gm.getBombs()) {
                if (b.getX() == p.getX() && b.getY() == p.getY())
                    bombExists = true;
            }
            if (!bombExists && gm.getBombs().size() < GameManager.MAX_BOMBS) {
                gm.getBombs().add(EntityFactory.createBomb(p.getX(), p.getY(), GameManager.BOMB_RADIUS));
                SoundManager.getInstance().playSFX(SoundManager.SFX_BOMB_PLACE);
            }
        }
    }
    
    @Override
    public void handleKeyReleased(int keyCode, GameManager gm) {
        if (!paused) {
            pressedKeys.remove(keyCode);
        }
    }
}
