# BomberQuest - Quick Reference Guide

## 🚀 Quick Start

### View Diagrams Online (Easiest)
1. Open https://www.plantuml.com/plantuml/uml/
2. Open any `.puml` file from `diagrams/` folder
3. Copy-paste the content
4. Click "Submit"

### View in VS Code
1. Install "PlantUML" extension
2. Install Graphviz: `choco install graphviz`
3. Open `.puml` file
4. Press `Alt+D`

### View HTML Documentation
Open `DESIGN_VISUALIZATION.html` in your browser for interactive documentation.

---

## 📊 Available Diagrams

### Class Diagrams (Recommended - Smaller Files)

| Diagram | File | Description |
|---------|------|-------------|
| **Simple Overview** | `diagrams/class_diagram_simple.puml` | Quick overview, fastest to render |
| **Architecture** | `diagrams/class_diagram_overview.puml` | High-level layers and design patterns |
| **Core Package** | `diagrams/class_diagram_core.puml` | GameManager, EntityFactory, TransitionManager |
| **Entity Package** | `diagrams/class_diagram_entities.puml` | GameObject hierarchy and all entities |
| **State Package** | `diagrams/class_diagram_states.puml` | All GameState implementations |
| **Manager Package** | `diagrams/class_diagram_managers.puml` | All Singleton managers |

### Complete Diagram (Large File)

| Diagram | File | Description |
|---------|------|-------------|
| **Complete Class** | `diagrams/class_diagram.puml` | Complete architecture (may be slow to render) |

### Other Diagrams

| Diagram | File | Description |
|---------|------|-------------|
| **State Diagram** | `diagrams/state_diagram.puml` | Game state machine with all transitions |
| **Game Init** | `diagrams/sequence_game_init.puml` | Initialization sequence when game starts |
| **State Transition** | `diagrams/sequence_state_transition.puml` | How states change with animations |
| **Bomb Explosion** | `diagrams/sequence_bomb_explosion.puml` | Bomb placement and explosion logic |
| **Collision** | `diagrams/sequence_collision.puml` | Entity collision detection system |
| **Leaderboard** | `diagrams/sequence_leaderboard.puml` | Score saving and display |

---

## 🎨 Design Patterns at a Glance

### 1. Singleton Pattern
**Where:** All Manager classes
```java
public class GameManager {
    private static GameManager instance;
    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }
}
```

**Classes:**
- `GameManager`
- `SoundManager`
- `SettingsManager`
- `LeaderboardManager`
- `TransitionManager`

### 2. State Pattern
**Where:** Game State Management

**Interface:** `GameState`
```java
public interface GameState {
    void update(GameManager gm);
    void render(Graphics2D g2d, GameManager gm);
    void handleInput(int keyCode, GameManager gm);
}
```

**Implementations:**
- `MainMenuState`
- `DifficultySelectionState`
- `PlayingState`
- `GameOverState`
- `VictoryState`
- `SettingsState`
- `LeaderboardState`

### 3. Factory Pattern
**Where:** Entity Creation
```java
public class EntityFactory {
    public static GameObject createWall(int x, int y, boolean hard);
    public static Enemy createEnemy(int x, int y);
    public static Bomb createBomb(int x, int y, int radius);
}
```

### 4. Observer Pattern
**Where:** Settings Change Notification
```java
public interface SettingsApplyListener {
    void onSettingsApplied();
}
```

### 5. Template Method Pattern
**Where:** GameObject Hierarchy
```java
public abstract class GameObject {
    public abstract void render(Graphics2D g, int tileSize);
    public void update() {} // Default implementation
}
```

---

## 🏗️ Architecture Layers

```
┌─────────────────────┐
│  Presentation       │  BomberQuest (Swing UI)
├─────────────────────┤
│  Game Logic         │  GameManager, GameState, TransitionManager
├─────────────────────┤
│  Entity             │  Player, Enemy, Bomb, Explosion, Wall
├─────────────────────┤
│  Manager            │  SoundManager, SettingsManager, LeaderboardManager
└─────────────────────┘
```

---

## 📦 Package Structure

```
com.bomberman
├── BomberQuest.java          # Main entry point
├── core/                     # Core game logic
│   ├── GameManager.java
│   ├── EntityFactory.java
│   └── TransitionManager.java
├── entities/                 # Game objects
│   ├── GameObject.java
│   ├── Player.java
│   ├── Enemy.java
│   ├── Bomb.java
│   ├── Explosion.java
│   └── Wall.java
├── states/                   # Game states
│   ├── GameState.java
│   ├── MainMenuState.java
│   ├── DifficultySelectionState.java
│   ├── PlayingState.java
│   ├── GameOverState.java
│   ├── VictoryState.java
│   ├── SettingsState.java
│   └── LeaderboardState.java
├── managers/                 # Singleton managers
│   ├── SoundManager.java
│   ├── SettingsManager.java
│   └── LeaderboardManager.java
└── exceptions/
    └── GameInitializationException.java
```

---

## 🔄 State Flow

```
MainMenu → DifficultySelection → Playing → {GameOver, Victory}
   ↓                                          ↓        ↓
Settings ←──────────────────────────────────┴────────┘
   ↓
Leaderboard
```

---

## 🎯 Key Relationships

### Composition (Has-A, Strong)
- `GameManager` **HAS** `Player`
- `GameManager` **HAS** `List<GameObject>`

### Aggregation (Uses-A, Weak)
- `GameManager` **USES** `GameState` implementations

### Dependency (Depends-On)
- All States **DEPEND ON** `GameManager`
- `PlayingState` **DEPENDS ON** `SoundManager`
- `Enemy` **DEPENDS ON** `Player` (for AI)

### Inheritance (Is-A)
- `Player` **IS A** `GameObject`
- `Enemy` **IS A** `GameObject`
- `Bomb` **IS A** `GameObject`
- `Explosion` **IS A** `GameObject`
- `Wall` **IS A** `GameObject`

---

## 🎮 Game Flow

### 1. Initialization
```
main() → BomberQuest() → GameManager.getInstance() → init()
→ SoundManager.initSounds() → playMusic(BGM_MENU)
→ setState(MAIN_MENU_STATE) → Timer.start(60 FPS)
```

### 2. Game Loop (60 FPS)
```
Timer tick → actionPerformed()
→ GameManager.update()
   → CurrentState.update(gm)
→ repaint()
   → paintComponent(g)
      → GameManager.render(g2d)
         → CurrentState.render(g2d, gm)
```

### 3. State Transition
```
User Input → CurrentState.handleInput(key, gm)
→ GameManager.setState(newState, transition)
   → TransitionManager.startTransition()
   → SoundManager.playMusic() [if needed]
   → currentState = newState
```

### 4. Bomb Explosion
```
SPACE → createBomb() → playSFX(BOMB_PLACE)
→ Timer(120 frames) → active = false
→ triggerExplosion()
   → Create center explosion
   → For each direction (up, down, left, right):
      → For each tile in radius:
         → Check walls (destroy soft, stop at hard)
         → Create explosion
         → Check entity collisions
→ playSFX(EXPLOSION)
```

---

## 📝 SOLID Principles Applied

### Single Responsibility (SRP)
✅ Each class has one clear responsibility
- `SoundManager` → Audio only
- `SettingsManager` → Configuration only
- `LeaderboardManager` → Score persistence only

### Open/Closed (OCP)
✅ Open for extension, closed for modification
- `GameObject` → Extend with new entities without modifying base
- `GameState` → Add new states without changing interface

### Liskov Substitution (LSP)
✅ Subtypes can replace their base types
- All `GameObject` subclasses are interchangeable
- All `GameState` implementations can be swapped

### Interface Segregation (ISP)
✅ Clients don't depend on unused methods
- `GameState` → Only required methods
- `SettingsApplyListener` → Single focused method

### Dependency Inversion (DIP)
✅ Depend on abstractions, not concretions
- `GameManager` depends on `GameState` interface
- High-level modules don't depend on low-level modules

---

## 🔧 Constants Reference

### GameManager
```java
TILE_SIZE = 48     // Pixel size of each grid tile
GRID_W = 25        // Grid width (tiles)
GRID_H = 15        // Grid height (tiles)
BOMB_RADIUS = 3    // Explosion radius in tiles
MAX_BOMBS = 3      // Max concurrent bombs
```

### Player
```java
maxHp = 3                // Starting health
INVINCIBLE_TIME = 120    // Invincibility frames after damage
```

### Bomb
```java
timer = 120        // Countdown in frames (~2 seconds at 60 FPS)
```

### Explosion
```java
timer = 15         // Duration in frames
```

### Enemy
```java
moveInterval = 30  // Frames between moves (Medium)
             = 45  // (Easy)
             = 15  // (Hard)
```

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `DESIGN_DOCUMENTATION.md` | Complete design docs with embedded PlantUML |
| `DESIGN_VISUALIZATION.html` | Interactive HTML documentation |
| `QUICK_REFERENCE.md` | This file - quick cheat sheet |
| `diagrams/README.md` | Guide for viewing diagrams |
| `diagrams/*.puml` | PlantUML source files |
| `README.md` | Project overview & compile instructions |
| `SOUND_ASSETS.md` | Audio file requirements |

---

## 🌐 Online Tools

| Tool | URL | Best For |
|------|-----|----------|
| PlantUML Online | https://www.plantuml.com/plantuml/uml/ | Quick viewing |
| PlantText | https://www.planttext.com/ | Simple & fast |
| PlantUML QEditor | https://plantuml-editor.kkeisuke.com/ | Export to multiple formats |

---

## 💡 Tips

### Viewing Diagrams
1. **Online (Fastest)**: Copy `.puml` content → Paste to online tool
2. **VS Code (Best)**: Install extension → Open `.puml` → `Alt+D`
3. **HTML (Interactive)**: Open `DESIGN_VISUALIZATION.html` in browser

### Export Diagrams
```powershell
# Download PlantUML JAR first
java -jar plantuml.jar diagrams/*.puml         # All as PNG
java -jar plantuml.jar -tsvg diagrams/*.puml   # All as SVG
java -jar plantuml.jar -tpdf diagrams/*.puml   # All as PDF
```

### Understanding Relationships
- **Solid arrow** (—▷) = Inheritance
- **Dashed arrow** (--▷) = Implementation
- **Solid line** (—) = Association
- **Dashed line** (--) = Dependency
- **Diamond** (◇—) = Aggregation
- **Filled diamond** (◆—) = Composition

---

## 🎓 Learning Path

1. Start with **Architecture Overview** (DESIGN_DOCUMENTATION.md)
2. Study **Class Diagram** to understand structure
3. Follow **State Diagram** to understand game flow
4. Deep dive into **Sequence Diagrams** for behavior
5. Review **Design Patterns** sections for implementation details

---

## 🆘 Troubleshooting

### Can't view .puml files?
→ Use online tools (no installation needed)

### Graphviz error in VS Code?
→ Install Graphviz: `choco install graphviz`
→ Restart VS Code after installation

### Diagram too large?
→ Use SVG export for zoom
→ Or view online with zoom controls

### Need specific part of diagram?
→ Edit `.puml` file and comment out sections
→ Re-generate focused view

---

## 📞 Quick Commands

```powershell
# Compile game
javac -d bin -sourcepath src src/com/bomberman/BomberQuest.java

# Run game
java -cp bin com.bomberman.BomberQuest

# Generate all diagrams
java -jar plantuml.jar diagrams/*.puml

# Open documentation
start DESIGN_VISUALIZATION.html
```

---

**Last Updated:** 2025-11-26
**Version:** 1.0
**Project:** BomberQuest Retro Edition

