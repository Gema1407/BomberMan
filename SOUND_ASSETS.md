# Sound Assets Guide

To enable sound in the game, you need to place `.wav` files in the `res/sounds/` directory.
Below is the list of sound effects and music tracks used by the game, along with their recommended filenames.

## Directory Structure
Create a folder named `res` in your project root, and inside it a folder named `sounds`.
```
BomberMan/
├── src/
├── res/
│   └── sounds/
│       ├── menu_theme.wav
│       ├── game_theme.wav
│       ├── ...
```

## Required Sound Files

| Sound Key | Description | Recommended Filename |
|-----------|-------------|----------------------|
| **Music** | | |
| `BGM_MENU` | Background music for Main Menu | `menu_theme.wav` |
| `BGM_GAME` | Background music during Gameplay | `game_theme.wav` |
| **Sound Effects** | | |
| `SFX_BOMB_PLACE` | When player places a bomb | `bomb_place.wav` |
| `SFX_EXPLOSION` | When a bomb explodes | `explosion.wav` |
| `SFX_POWERUP` | When player picks up an item | `powerup.wav` |
| `SFX_DEATH` | When player dies | `death.wav` |
| `SFX_WIN` | When player wins the level | `win.wav` |
| `SFX_SELECT` | Generic menu selection sound | `select.wav` |
| `SFX_HOVER` | When navigating menu options | `hover.wav` |
| `SFX_CLICK` | When confirming an option | `click.wav` |

## How to Configure
1. Place your `.wav` files in `res/sounds/`.
2. Open `src/com/bomberman/managers/SoundManager.java`.
3. Go to the `initSounds()` method.
4. Uncomment and update the `loadSound` lines to match your filenames.

Example:
```java
public void initSounds() {
    loadSound(BGM_MENU, "res/sounds/menu_theme.wav");
    loadSound(SFX_EXPLOSION, "res/sounds/explosion.wav");
    // ... add others
}
```
