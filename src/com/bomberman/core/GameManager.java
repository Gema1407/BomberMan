package com.bomberman.core;

import com.bomberman.entities.GameObject;
import com.bomberman.entities.Player;
import com.bomberman.exceptions.GameInitializationException;
import com.bomberman.managers.SettingsManager;
import com.bomberman.managers.SoundManager;
import com.bomberman.states.*;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {
    private static GameManager instance;
    private TransitionManager transitionManager;
    private final Random random = new Random();
    
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
    private GameState previousState;
    private boolean returnToPauseMenu = false;
    
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
        transitionManager = TransitionManager.getInstance();
    }

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // Initialize Game
    public void init() throws GameInitializationException {
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
        // Cleanup
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

        // Generate Enemies
        // Determine count based on Difficulty
        int enemiesToSpawn = 5;
        switch (currentDifficulty) {
            case EASY -> enemiesToSpawn = 3;
            case MEDIUM -> enemiesToSpawn = 6;
            case HARD -> enemiesToSpawn = 10;
        }

        // Spawning loop
        int count = 0;
        int maxAttempts = 1000;
        int attempts = 0;
        while (count < enemiesToSpawn && attempts < maxAttempts) {
            attempts++;
            int ex = random.nextInt(GRID_W);
            int ey = random.nextInt(GRID_H);
            // 1. Check distance from player or Check if position is valid (assuming isValidMove is a helper method)
            if (Math.abs(ex - player.getX()) + Math.abs(ey - player.getY()) <= 5 || !isValidMove(ex, ey, GRID_W, GRID_H, walls)) continue;
            
            // 2. Check minimum distance from other enemies
            boolean tooClose = false;
            for (GameObject enemy : enemies) {
                int dist = Math.abs(ex - enemy.getX()) + Math.abs(ey - enemy.getY());
                if (dist < 3) {
                    tooClose = true;
                    break;
                }
            }
            
            if (!tooClose) {
                enemies.add(EntityFactory.createEnemy(ex, ey));
                count++;
            }
        }

        gameStartTime = System.currentTimeMillis();
    }

    public void setState(GameState state) {
        setState(state, TransitionManager.TransitionType.FADE);
    }
    
    public void setState(GameState state, TransitionManager.TransitionType transitionType) {
        if (transitionManager.isTransitioning()) return; // Prevent transition spam
        
        transitionManager.startTransition(currentState, state, transitionType);
    }
    
    // Internal method called by TransitionManager
    public void setStateImmediate(GameState state) {
        // Track if we're leaving a paused game state
        boolean wasPaused = (currentState == PLAYING_STATE && ((PlayingState)PLAYING_STATE).isPaused());
        if (wasPaused && state == SETTINGS_STATE) {
            returnToPauseMenu = true;
        }
        
        // Clean up old state (but not if returning to pause menu)
        if (currentState == PLAYING_STATE && !returnToPauseMenu) {
            ((PlayingState)PLAYING_STATE).reset();
        } else if (currentState == VICTORY_STATE) {
            ((VictoryState)VICTORY_STATE).reset();
        }
        
        // Store previous state BEFORE entering settings (not after)
        // And never store SETTINGS_STATE as previous state
        if (state == SETTINGS_STATE && currentState != SETTINGS_STATE || state != SETTINGS_STATE && currentState != SETTINGS_STATE) {
            previousState = currentState;
        }
        
        this.currentState = state;
        
        // Don't restart music if returning to paused game or if already in that state's music
        if (state == MAIN_MENU_STATE) {
            SoundManager.getInstance().playMusic(SoundManager.BGM_MENU);
        } else if (state == PLAYING_STATE && !wasPaused) {
            SoundManager.getInstance().playMusic(SoundManager.BGM_GAME);
        }
    }

    public void update() {
        transitionManager.update(this);
        if (!transitionManager.isTransitioning()) {
            currentState.update(this);
        }
    }

    public void render(Graphics2D g2d) {
        currentState.render(g2d, this);
        
        // Render transition overlay
        int screenW = GRID_W * TILE_SIZE;
        int screenH = GRID_H * TILE_SIZE;
        transitionManager.render(g2d, this, screenW, screenH);
    }

    public void handleInput(int keyCode) {
        if (!transitionManager.isTransitioning()) {
            currentState.handleInput(keyCode, this);
        }
    }
    
    public void handleKeyReleased(int keyCode) {
        if (!transitionManager.isTransitioning()) {
            currentState.handleKeyReleased(keyCode, this);
        }
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
    
    public void returnToPreviousState() {
        if (previousState != null && previousState != SETTINGS_STATE) {
            GameState targetState = previousState;
            boolean shouldPause = returnToPauseMenu;
            
            // Restore pause state BEFORE transitioning
            if (shouldPause && targetState == PLAYING_STATE) {
                ((PlayingState)PLAYING_STATE).setPaused(true);
            }
            
            setState(targetState);
            
            returnToPauseMenu = false;
        } else {
            setState(MAIN_MENU_STATE);
        }
    }
}
