package com.bomberman.managers;

public class SettingsManager {
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public enum Resolution {
        RES_640x480(640, 480),
        RES_800x600(800, 600),
        RES_1024x576(1024, 576),
        RES_1024x768(1024, 768),
        RES_1152x648(1152, 648),
        RES_1280x720(1280, 720),
        RES_1280x800(1280, 800),
        RES_1366x768(1366, 768),
        RES_1440x900(1440, 900),
        RES_1600x900(1600, 900),
        RES_1680x1050(1680, 1050),
        RES_1920x1080(1920, 1080),
        RES_2560x1440(2560, 1440),
        RES_3840x2160(3840, 2160);

        int width;
        int height;
        Resolution(int w, int h) { this.width = w; this.height = h; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
        @Override
        public String toString() { return width + "x" + height; }
    }

    private static SettingsManager instance;

    private Resolution resolution = Resolution.RES_1280x720;
    private boolean fullscreen = false;
    private boolean retroEffects = true;
    private boolean showFPS = false;

    private SettingsManager() {}

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public Resolution getResolution() { return resolution; }
    public void setResolution(Resolution resolution) { this.resolution = resolution; }

    public boolean isFullscreen() { return fullscreen; }
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }

    public boolean isRetroEffects() { return retroEffects; }
    public void setRetroEffects(boolean retroEffects) { this.retroEffects = retroEffects; }

    public boolean isShowFPS() { return showFPS; }
    public void setShowFPS(boolean showFPS) { this.showFPS = showFPS; }
}
