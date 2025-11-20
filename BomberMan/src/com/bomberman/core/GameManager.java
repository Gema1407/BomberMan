package com.bomberman.core;

import com.bomberman.entities.GameObject;
import com.bomberman.entities.Player;
import com.bomberman.states.*;
import com.bomberman.exceptions.GameInitializationException;
import com.bomberman.managers.SettingsManager;
import com.bomberman.managers.SoundManager;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    
    // Constants
    public static final int TILE_SIZE = 48; // Increased for retro feel
    public static final int GRID_W = 25;
    public static final int GRID_H = 15; // Adjusted for 16:9 aspect ratio
    public static final int BOMB_RADIUS = 3;
    public static final int MAX_BOMBS = 3;
    
    // Game Data
    private List<GameObject> walls;
    private List<GameObject> enemies;
    private List<GameObject> bombs;
    private List<GameObject> explosions;
    private Player player;

    private GameState currentState;
    
    // Concrete States
    public final GameState PLAYING_STATE = new PlayingState();
    public final GameState GAMEOVER_STATE = new GameOverState();
    public final GameState VICTORY_STATE = new VictoryState();
    public final GameState MAIN_MENU_STATE = new MainMenuState();
    public final GameState LEADERBOARD_STATE = new LeaderboardState();
    public final GameState SETTINGS_STATE = new SettingsState();
    public final GameState DIFFICULTY_SELECTION_STATE = new DifficultySelectionState();

    private long gameStartTime;
    private int lastGameTime;
    private SettingsManager.Difficulty currentDifficulty = SettingsManager.Difficulty.MEDIUM;

    // Listener for Settings
    public interface SettingsApplyListener {
        void onSettingsApplied();
    }
    private SettingsApplyListener settingsListener;
    public void setSettingsListener(SettingsApplyListener listener) { this.settingsListener = listener; }
    public void notifySettingsApplied() { if (settingsListener != null) settingsListener.onSettingsApplied(); }

    private GameManager() {
        walls = new ArrayList<>();
        enemies = new ArrayList<>();
        bombs = new ArrayList<>();
        explosions = new ArrayList<>();
        player = new Player(1, 1);
        currentState = MAIN_MENU_STATE; // Start at Menu
    }

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // Initialize Game
    public void init() throws GameInitializationException {
        if (GRID_W < 5 || GRID_H < 5) {
            throw new GameInitializationException("Map size too small!");
        }
        // Initialize Sound
        SoundManager.getInstance().initSounds();
        SoundManager.getInstance().playMusic(SoundManager.BGM_MENU);
    }

    public void setDifficulty(SettingsManager.Difficulty difficulty) {
        this.currentDifficulty = difficulty;
    }

    public SettingsManager.Difficulty getDifficulty() {
        return currentDifficulty;
    }

    public void resetGame() {
        walls.clear();
        enemies.clear();
        bombs.clear();
        explosions.clear();
        player.reset();

        // Generate Map
        for (int y = 0; y < GRID_H; y++) {
            for (int x = 0; x < GRID_W; x++) {
                if (x == 0 || x == GRID_W - 1 || y == 0 || y == GRID_H - 1) {
                    walls.add(EntityFactory.createWall(x, y, true));
                } else if (x % 2 == 0 && y % 2 == 0) {
                    walls.add(EntityFactory.createWall(x, y, true));
                } else if ((x < 3 && y < 3)) {
                    // Safe zone
                } else if (Math.random() < 0.4) {
                    walls.add(EntityFactory.createWall(x, y, false));
                }
            }
        }

        // Spawn Enemies based on Difficulty
        int enemiesToSpawn = 5;
        switch (currentDifficulty) {
            case EASY: enemiesToSpawn = 3; break;
            case MEDIUM: enemiesToSpawn = 6; break;
            case HARD: enemiesToSpawn = 10; break;
        }

        int count = 0;
        while (count < enemiesToSpawn) {
            int ex = (int) (Math.random() * GRID_W);
            int ey = (int) (Math.random() * GRID_H);
            if (isValidMove(ex, ey, GRID_W, GRID_H, walls) && (Math.abs(ex - player.getX()) + Math.abs(ey - player.getY()) > 5)) {
                enemies.add(EntityFactory.createEnemy(ex, ey));
                count++;
            }
        }

        gameStartTime = System.currentTimeMillis();
        setState(PLAYING_STATE);
        SoundManager.getInstance().playMusic(SoundManager.BGM_GAME);
    }

    public void setState(GameState state) {
        this.currentState = state;
        if (state == MAIN_MENU_STATE) {
            SoundManager.getInstance().playMusic(SoundManager.BGM_MENU);
        }
    }

    public void update() {
        currentState.update(this);
    }

    public void render(Graphics2D g2d) {
        currentState.render(g2d, this);
    }

    public void handleInput(int keyCode) {
        currentState.handleInput(keyCode, this);
    }
    
    public void handleKeyReleased(int keyCode) {
        currentState.handleKeyReleased(keyCode, this);
    }

    // Helper for collision
    public static boolean isValidMove(int x, int y, int w, int h, List<GameObject> walls) {
        if (x < 0 || x >= w || y < 0 || y >= h) return false;
        for (GameObject obj : walls) {
            if (obj.getX() == x && obj.getY() == y) return false;
        }
        return true;
    }

    public void calculateScore() {
        lastGameTime = (int) ((System.currentTimeMillis() - gameStartTime) / 1000);
    }

    // Getters
    public Player getPlayer() { return player; }
    public List<GameObject> getWalls() { return walls; }
    public List<GameObject> getEnemies() { return enemies; }
    public List<GameObject> getBombs() { return bombs; }
    public List<GameObject> getExplosions() { return explosions; }
    public long getGameStartTime() { return gameStartTime; }
    public int getLastGameTime() { return lastGameTime; }
}
