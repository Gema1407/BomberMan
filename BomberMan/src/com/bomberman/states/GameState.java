package com.bomberman.states;

import com.bomberman.core.GameManager;
import java.awt.Graphics2D;

public interface GameState {
    void update(GameManager gm);
    void render(Graphics2D g2d, GameManager gm);
    void handleInput(int keyCode, GameManager gm);
    default void handleKeyReleased(int keyCode, GameManager gm) {}
}
