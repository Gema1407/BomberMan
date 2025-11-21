package com.bomberman.core;

import com.bomberman.states.GameState;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class TransitionManager {
    private static TransitionManager instance;
    
    private boolean transitioning = false;
    private TransitionType type = TransitionType.FADE;
    private int transitionFrame = 0;
    private int transitionDuration = 20; // frames
    
    private GameState fromState;
    private GameState toState;
    private boolean midTransition = false;
    
    public enum TransitionType {
        FADE,
        SLIDE_LEFT,
        SLIDE_RIGHT,
        ZOOM_OUT,
        DISSOLVE
    }
    
    private TransitionManager() {}
    
    public static TransitionManager getInstance() {
        if (instance == null) {
            instance = new TransitionManager();
        }
        return instance;
    }
    
    public void startTransition(GameState from, GameState to, TransitionType type) {
        this.fromState = from;
        this.toState = to;
        this.type = type;
        this.transitioning = true;
        this.transitionFrame = 0;
        this.midTransition = false;
    }
    
    public void update(GameManager gm) {
        if (!transitioning) return;
        
        transitionFrame++;
        
        // Switch to new state at halfway point
        if (transitionFrame >= transitionDuration / 2 && !midTransition) {
            midTransition = true;
            gm.setStateImmediate(toState); // Internal method to change without triggering new transition
        }
        
        if (transitionFrame >= transitionDuration) {
            transitioning = false;
            transitionFrame = 0;
        }
    }
    
    public void render(Graphics2D g2d, GameManager gm, int screenW, int screenH) {
        if (!transitioning) return;
        
        float progress = (float) transitionFrame / transitionDuration;
        
        switch (type) {
            case FADE:
                renderFade(g2d, screenW, screenH, progress);
                break;
            case SLIDE_LEFT:
                renderSlideLeft(g2d, screenW, screenH, progress);
                break;
            case SLIDE_RIGHT:
                renderSlideRight(g2d, screenW, screenH, progress);
                break;
            case ZOOM_OUT:
                renderZoomOut(g2d, screenW, screenH, progress);
                break;
            case DISSOLVE:
                renderDissolve(g2d, screenW, screenH, progress);
                break;
        }
    }
    
    private void renderFade(Graphics2D g2d, int w, int h, float progress) {
        // Fade to black in first half, fade from black in second half
        float alpha;
        if (progress < 0.5f) {
            alpha = progress * 2.0f; // 0 to 1
        } else {
            alpha = (1.0f - progress) * 2.0f; // 1 to 0
        }
        
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fillRect(0, 0, w, h);
    }
    
    private void renderSlideLeft(Graphics2D g2d, int w, int h, float progress) {
        // Black curtain sliding from right
        if (progress < 0.5f) {
            int curtainX = (int) (w * (1.0f - progress * 2.0f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(curtainX, 0, w - curtainX, h);
        } else {
            int curtainX = (int) (w * ((progress - 0.5f) * 2.0f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, w - curtainX, h);
        }
    }
    
    private void renderSlideRight(Graphics2D g2d, int w, int h, float progress) {
        // Black curtain sliding from left
        if (progress < 0.5f) {
            int curtainW = (int) (w * progress * 2.0f);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, curtainW, h);
        } else {
            int curtainX = (int) (w * ((progress - 0.5f) * 2.0f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(curtainX, 0, w - curtainX, h);
        }
    }
    
    private void renderZoomOut(Graphics2D g2d, int w, int h, float progress) {
        // Scale down effect with fade
        float alpha;
        if (progress < 0.5f) {
            alpha = progress * 2.0f;
        } else {
            alpha = (1.0f - progress) * 2.0f;
        }
        
        // Vignette effect
        java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
            w / 2.0f, h / 2.0f, 
            Math.max(w, h) / 2.0f,
            new float[]{0.0f, 0.5f, 1.0f},
            new Color[]{
                new Color(0, 0, 0, 0),
                new Color(0, 0, 0, (int)(alpha * 100)),
                new Color(0, 0, 0, (int)(alpha * 255))
            }
        );
        g2d.setPaint(rgp);
        g2d.fillRect(0, 0, w, h);
    }
    
    private void renderDissolve(Graphics2D g2d, int w, int h, float progress) {
        // Pixelated dissolve effect
        int blockSize = 20;
        float alpha;
        if (progress < 0.5f) {
            alpha = progress * 2.0f;
        } else {
            alpha = (1.0f - progress) * 2.0f;
        }
        
        g2d.setColor(new Color(0, 0, 0, alpha * 0.8f));
        
        for (int y = 0; y < h; y += blockSize) {
            for (int x = 0; x < w; x += blockSize) {
                if (Math.random() < alpha) {
                    g2d.fillRect(x, y, blockSize, blockSize);
                }
            }
        }
    }
    
    public boolean isTransitioning() {
        return transitioning;
    }
}
