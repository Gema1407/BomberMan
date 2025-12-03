package com.bomberman.entities;

import com.bomberman.core.GameManager;
import com.bomberman.managers.SettingsManager;
import com.bomberman.utils.Pathfinding;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

/**
 * Enemy entity with advanced A* pathfinding AI.
 * Intelligently hunts the player using optimal path calculation.
 * 
 * @author BomberQuest Team
 * @version 2.0 - Enhanced AI
 */
public class Enemy extends GameObject {
    private int moveTimer = 0;
    private int moveInterval = 30;
    private List<int[]> currentPath = null;
    private int pathRecalculateTimer = 0;
    private static final int PATH_RECALCULATE_INTERVAL = 10; // Recalculate path every 10 frames

    /**
     * Creates a new enemy at the specified position.
     * 
     * @param x Grid X coordinate
     * @param y Grid Y coordinate
     */
    public Enemy(int x, int y) {
        super(x, y);
        // Random color for each enemy
        float hue = new Random().nextFloat();
        this.color = Color.getHSBColor(hue, 0.7f, 0.6f);
        
        // Adjust speed based on difficulty
        updateMoveInterval();
    }

    /**
     * Advanced AI: Perfect pathfinding to hunt player.
     * Uses A* algorithm for optimal path calculation.
     * 
     * @param walls List of wall objects
     * @param enemies List of enemy objects
     * @param bombs List of bomb objects
     * @param player Player object to hunt
     * @param gridW Grid width
     * @param gridH Grid height
     */
    public void tryMove(List<GameObject> walls, List<GameObject> enemies, List<GameObject> bombs, 
                        Player player, int gridW, int gridH) {
        if (moveTimer > 0) {
            moveTimer--;
            return;
        }
        
        updateMoveInterval(); // Refresh interval based on difficulty
        moveTimer = moveInterval;
        pathRecalculateTimer--;

        // Recalculate path periodically or if no path exists
        if (currentPath == null || pathRecalculateTimer <= 0 || currentPath.isEmpty()) {
            calculatePathToPlayer(walls, enemies, bombs, player, gridW, gridH);
            pathRecalculateTimer = PATH_RECALCULATE_INTERVAL;
        }

        // Follow calculated path
        if (currentPath != null && currentPath.size() > 1) {
            // Remove current position (first element)
            currentPath.remove(0);
            
            if (!currentPath.isEmpty()) {
                int[] nextPos = currentPath.get(0);
                int nx = nextPos[0];
                int ny = nextPos[1];
                
                // Verify move is still valid (in case map changed)
                if (isValidMove(nx, ny, walls, enemies, bombs, gridW, gridH)) {
                    x = nx;
                    y = ny;
                } else {
                    // Path blocked, recalculate immediately
                    currentPath = null;
                    pathRecalculateTimer = 0;
                }
            }
        } else {
            // Fallback: try any valid move towards player (greedy)
            tryGreedyMove(walls, enemies, bombs, player, gridW, gridH);
        }
    }

    /**
     * Calculates optimal path to player using A* algorithm.
     */
    private void calculatePathToPlayer(List<GameObject> walls, List<GameObject> enemies, 
                                      List<GameObject> bombs, Player player, int gridW, int gridH) {
        // Create walkable checker lambda
        Pathfinding.WalkableChecker checker = (tx, ty) -> {
            return isValidMove(tx, ty, walls, enemies, bombs, gridW, gridH);
        };

        // Find path using A* algorithm
        currentPath = Pathfinding.findPath(
            this.x, this.y,
            player.getX(), player.getY(),
            gridW, gridH,
            checker
        );
    }

    /**
     * Fallback greedy movement when pathfinding fails.
     */
    private void tryGreedyMove(List<GameObject> walls, List<GameObject> enemies, 
                              List<GameObject> bombs, Player player, int gridW, int gridH) {
        int bestDirIndex = -1;
        double minDistance = Double.MAX_VALUE;
        int[][] dirs = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} }; // Up, Down, Left, Right

        // Find best direction towards player
        for (int i = 0; i < 4; i++) {
            int nx = x + dirs[i][0];
            int ny = y + dirs[i][1];

            if (isValidMove(nx, ny, walls, enemies, bombs, gridW, gridH)) {
                double dist = Math.pow(nx - player.getX(), 2) + Math.pow(ny - player.getY(), 2);
                if (dist < minDistance) {
                    minDistance = dist;
                    bestDirIndex = i;
                }
            }
        }

        // Move in best direction
        if (bestDirIndex != -1) {
            x += dirs[bestDirIndex][0];
            y += dirs[bestDirIndex][1];
        }
    }

    /**
     * Updates move interval based on current difficulty.
     */
    private void updateMoveInterval() {
        SettingsManager.Difficulty diff = GameManager.getInstance().getDifficulty();
        switch (diff) {
            case EASY:   moveInterval = 45; break; // Slower
            case MEDIUM: moveInterval = 25; break; // Faster than before
            case HARD:   moveInterval = 10; break; // Very fast
            default:     moveInterval = 25; break;
        }
    }

    /**
     * Checks if a move to the specified position is valid.
     * 
     * @param tx Target X coordinate
     * @param ty Target Y coordinate
     * @param walls List of walls
     * @param enemies List of enemies
     * @param bombs List of bombs
     * @param w Grid width
     * @param h Grid height
     * @return true if move is valid, false otherwise
     */
    private boolean isValidMove(int tx, int ty, List<GameObject> walls, List<GameObject> enemies, 
                               List<GameObject> bombs, int w, int h) {
        // Check boundaries and walls
        if (!GameManager.isValidMove(tx, ty, w, h, walls))
            return false;

        // Check against other enemies (don't overlap)
        for (GameObject obj : enemies) {
            if (!(obj instanceof Enemy)) continue;
            Enemy other = (Enemy) obj;
            if (other.getX() == this.x && other.getY() == this.y)
                continue; // Skip self
            if (other.getX() == tx && other.getY() == ty)
                return false;
        }

        // Check against bombs
        for (GameObject b : bombs) {
            if (b.getX() == tx && b.getY() == ty)
                return false;
        }

        return true;
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        int px = x * tileSize;
        int py = y * tileSize;
        
        // Retro Balloon-like Enemy with aggressive look
        g.setColor(this.color);
        g.fillOval(px + 4, py + 4, tileSize - 8, tileSize - 8);
        
        // Shine
        g.setColor(new Color(255, 255, 255, 100));
        g.fillOval(px + 8, py + 8, tileSize/4, tileSize/4);
        
        // Angry eyes (red)
        g.setColor(Color.RED);
        g.fillRect(px + 12, py + 16, 4, 8);
        g.fillRect(px + tileSize - 16, py + 16, 4, 8);
        
        // Aggressive mouth
        g.setColor(Color.BLACK);
        g.drawPolyline(new int[]{px + 12, px + 16, px + 20, px + 24, px + 28}, 
                       new int[]{py + 32, py + 28, py + 32, py + 28, py + 32}, 5);
    }
}
