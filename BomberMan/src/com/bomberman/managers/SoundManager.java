package com.bomberman.managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, Clip> sounds = new HashMap<>();
    private Clip currentMusic;
    private float musicVolume = -10.0f; // Decibels
    private float sfxVolume = -5.0f;

    // Asset Names - User can map these to files
    public static final String BGM_MENU = "menu_theme";
    public static final String BGM_GAME = "game_theme";
    public static final String SFX_BOMB_PLACE = "bomb_place";
    public static final String SFX_EXPLOSION = "explosion";
    public static final String SFX_POWERUP = "powerup";
    public static final String SFX_DEATH = "death";
    public static final String SFX_WIN = "win";
    public static final String SFX_SELECT = "select";

    private SoundManager() {
        // Pre-load sounds here if files exist
        // loadSound(BGM_MENU, "res/sounds/menu.wav");
        // loadSound(SFX_EXPLOSION, "res/sounds/explosion.wav");
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void loadSound(String name, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                sounds.put(name, clip);
            } else {
                System.out.println("Sound file not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMusic(String name) {
        stopMusic();
        if (sounds.containsKey(name)) {
            currentMusic = sounds.get(name);
            setVolume(currentMusic, musicVolume);
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

    public void playSFX(String name) {
        if (sounds.containsKey(name)) {
            Clip clip = sounds.get(name);
            setVolume(clip, sfxVolume);
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void setVolume(Clip clip, float volume) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
            } catch (Exception e) {
                // Control not supported
            }
        }
    }
    
    // Setup method for user to easily plug in files
    public void initSounds() {
        // TODO: User, please place your .wav files in a 'res/sounds/' folder 
        // and uncomment/edit the lines below to match your filenames.
        
        // loadSound(BGM_MENU, "res/sounds/menu_theme.wav");
        // loadSound(BGM_GAME, "res/sounds/battle_theme.wav");
        // loadSound(SFX_BOMB_PLACE, "res/sounds/place_bomb.wav");
        // loadSound(SFX_EXPLOSION, "res/sounds/boom.wav");
        // loadSound(SFX_SELECT, "res/sounds/blip.wav");
    }
}
