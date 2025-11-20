package com.bomberman.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class GameObject {
    protected int x, y; // Grid coordinates
    protected Color color;
    protected boolean active = true;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render(Graphics2D g, int tileSize);
    public void update() {} // Default implementation

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
