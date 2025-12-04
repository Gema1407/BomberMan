package com.bomberman.managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundManager {
    private static final Logger logger = Logger.getLogger(SoundManager.class.getName());
    private static SoundManager instance;
    private Map<String, Clip> sounds = new HashMap<>();
    private Clip currentMusic;
    
    // Volume settings (0-100)
    private int musicVolumeScale = 70;
    private int sfxVolumeScale = 80;
    private boolean musicMuted = false;
    private boolean sfxMuted = false;

    // Asset Names - User can map these to files
    public static final String BGM_MENU = "menu_theme";
    public static final String BGM_GAME = "game_theme";
    public static final String SFX_BOMB_PLACE = "bomb_place";
    public static final String SFX_EXPLOSION = "explosion";
    public static final String SFX_POWERUP = "powerup";
    public static final String SFX_DEATH = "death";
    public static final String SFX_WIN = "win";
    public static final String SFX_SELECT = "select";
    public static final String SFX_HOVER = "hover";
    public static final String SFX_CLICK = "click";

    private SoundManager() {
        // Pre-load sounds here if files exist
        // loadSound(BGM_MENU, "res/sounds/menu_theme.wav");
        // loadSound(SFX_EXPLOSION, "res/sounds/explosion.wav");
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    public void setMusicVolume(int scale) {
        this.musicVolumeScale = Math.clamp(scale, 0, 100);
        updateMusicVolume();
    }
    
    public int getMusicVolume() { return musicVolumeScale; }
    
    public void setSFXVolume(int scale) {
        this.sfxVolumeScale = Math.clamp(scale, 0, 100);
    }
    
    public int getSFXVolume() { return sfxVolumeScale; }
    
    public void setMusicMuted(boolean muted) {
        this.musicMuted = muted;
        updateMusicVolume();
        if (muted) stopMusic();
        else if (currentMusic != null && !currentMusic.isRunning()) currentMusic.start();
    }
    
    public boolean isMusicMuted() { return musicMuted; }
    
    public void setSFXMuted(boolean muted) { this.sfxMuted = muted; }
    public boolean isSFXMuted() { return sfxMuted; }

    public void loadSound(String name, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                sounds.put(name, clip);
            } else {
                logger.log(Level.WARNING, "Sound file not found: {0}", path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMusic(String name) {
        stopMusic();
        if (sounds.containsKey(name) && !musicMuted) {
            currentMusic = sounds.get(name);
            updateMusicVolume();
            currentMusic.setFramePosition(0);
            currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
            currentMusic.start();
        }
    }

    public void stopMusic() {
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop();
        }
    }

    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop();
        }
    }

    public void resumeMusic() {
        if (currentMusic != null && !musicMuted && !currentMusic.isRunning()) {
            currentMusic.start();
        }
    }

    public void playSFX(String name) {
        if (sounds.containsKey(name) && !sfxMuted) {
            Clip clip = sounds.get(name);
            setVolume(clip, getDb(sfxVolumeScale));
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    private void updateMusicVolume() {
        if (currentMusic != null) {
            if (musicMuted) {
                setVolume(currentMusic, -80.0f); // Effectively mute
            } else {
                setVolume(currentMusic, getDb(musicVolumeScale));
            }
        }
    }
    
    private float getDb(int scale) {
        if (scale <= 0) return -80.0f;
        // Linear scale 0-100 to Logarithmic dB
        // 100 -> 6.0 dB (amplified slightly)
        // 50 -> -10 dB
        // 1 -> -40 dB
        return (float) (20.0 * Math.log10(scale / 100.0) * 2.0); 
        // Simplified: 20 * log10(ratio). 
        // Let's use a simpler mapping:
        // Range -40dB to 6dB
        // return (float) (-40.0 + (scale / 100.0) * 46.0);
    }

    private void setVolume(Clip clip, float volume) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // Clamp to valid range if needed, usually -80 to 6
                float min = gainControl.getMinimum();
                float max = gainControl.getMaximum();
                volume = Math.clamp(volume,min,max);
                gainControl.setValue(volume);
            } catch (Exception e) {
                // Control not supported
            }
        }
    }
    
    // Setup method for user to easily plug in files
    public void initSounds() {
        // Load Music
        loadSound(BGM_MENU, "res/sounds/menu_theme.wav");
        loadSound(BGM_GAME, "res/sounds/game_theme.wav");
        
        // Load SFX
        loadSound(SFX_BOMB_PLACE, "res/sounds/bomb_place.wav");
        loadSound(SFX_EXPLOSION, "res/sounds/explosion.wav");
        loadSound(SFX_POWERUP, "res/sounds/powerup.wav");
        loadSound(SFX_DEATH, "res/sounds/death.wav");
        loadSound(SFX_WIN, "res/sounds/win.wav");
        loadSound(SFX_SELECT, "res/sounds/select.wav");
        loadSound(SFX_HOVER, "res/sounds/hover.wav");
        loadSound(SFX_CLICK, "res/sounds/click.wav");
        
        logger.info("Sound initialization complete.");
    }
}
