# BomberQuest Diagrams

This folder contains PlantUML diagram files for visualizing the BomberQuest architecture.

## Files

### Class Diagrams

#### Recommended (Smaller, easier to render):
- **`class_diagram_simple.puml`** - Simplified class diagram (fastest to render)
- **`class_diagram_overview.puml`** - Architecture overview with design patterns
- **`class_diagram_core.puml`** - Core package (GameManager, EntityFactory, TransitionManager)
- **`class_diagram_entities.puml`** - Entity package (GameObject and all subclasses)
- **`class_diagram_states.puml`** - State package (all GameState implementations)
- **`class_diagram_managers.puml`** - Manager package (all Singleton managers)

#### Complete (Large file, may be slow):
- **`class_diagram.puml`** - Complete class diagram showing all packages, classes, and relationships

### State Diagrams
- **`state_diagram.puml`** - Game state machine showing all state transitions

### Sequence Diagrams
- **`sequence_game_init.puml`** - Game initialization sequence
- **`sequence_state_transition.puml`** - State transition with animation
- **`sequence_bomb_explosion.puml`** - Bomb placement and explosion logic
- **`sequence_collision.puml`** - Collision detection between entities
- **`sequence_leaderboard.puml`** - Leaderboard save and display

## How to View

### Option 1: Online PlantUML Viewer (Recommended)

Visit one of these online viewers:
- **PlantUML Online Editor**: https://www.plantuml.com/plantuml/uml/
- **PlantText**: https://www.planttext.com/
- **PlantUML QEditor**: https://plantuml-editor.kkeisuke.com/

Steps:
1. Open any of the `.puml` files in this folder
2. Copy the entire content
3. Paste into the online editor
4. Click "Submit" or "Generate"
5. View the generated diagram

### Option 2: VS Code Extension

1. Install the "PlantUML" extension in VS Code:
   - Extension ID: `jebbs.plantuml`
   - Or search "PlantUML" in Extensions marketplace

2. Install Graphviz (required for rendering):
   - Windows: `choco install graphviz` or download from https://graphviz.org/download/
   - Mac: `brew install graphviz`
   - Linux: `sudo apt-get install graphviz`

3. Open any `.puml` file in VS Code

4. Press `Alt+D` (Windows/Linux) or `Option+D` (Mac) to preview

### Option 3: IntelliJ IDEA Plugin

1. Install "PlantUML integration" plugin
2. Open any `.puml` file
3. Right-click → "Show PlantUML Diagram"

### Option 4: Command Line (with PlantUML JAR)

```powershell
# Download PlantUML JAR
# https://github.com/plantuml/plantuml/releases

# Generate PNG
java -jar plantuml.jar class_diagram.puml

# Generate SVG
java -jar plantuml.jar -tsvg class_diagram.puml

# Generate all diagrams in folder
java -jar plantuml.jar *.puml
```

## Diagram Descriptions

### Class Diagram
Shows the complete architecture with:
- All packages (core, entities, states, managers, exceptions)
- All classes with attributes and methods
- Relationships (inheritance, composition, dependency)
- Design patterns (Singleton, State, Factory, Observer)

### State Diagram
Visualizes the game flow:
- All game states (Menu, Playing, GameOver, Victory, etc.)
- State transitions and triggers
- Embedded states (Pause menu within Playing state)
- Notes on important behaviors

### Sequence Diagrams

#### Game Initialization
- Application startup
- Singleton initialization
- Sound system loading
- Timer setup (60 FPS game loop)

#### State Transition
- User input handling
- State change request
- TransitionManager animation
- Sound switching
- Frame-by-frame transition rendering

#### Bomb Explosion
- Bomb placement with validation
- Timer countdown (120 frames)
- Explosion trigger
- Directional blast calculation
- Wall destruction logic
- Damage application

#### Collision Detection
- Explosion vs Player collision with invincibility
- Explosion vs Enemy collision
- Enemy vs Player collision with AI movement
- Damage sound differentiation (damage vs death)
- Game over and victory conditions

#### Leaderboard
- Username input validation
- Duplicate name checking
- Score persistence to file
- Leaderboard loading and sorting
- Top 10 display with special formatting

## Tips for Reading Diagrams

### Class Diagram Symbols:
- `+` = public
- `-` = private
- `#` = protected
- `{static}` = static member
- `{abstract}` = abstract method
- `<<Stereotype>>` = class stereotype/category
- Solid arrow = inheritance/implementation
- Dashed arrow = dependency
- Diamond = composition/aggregation

### Sequence Diagram Symbols:
- Solid line with filled arrow = synchronous call
- Dashed line with open arrow = return
- Box on lifeline = activation (method execution)
- `alt` = conditional logic
- `loop` = repeated execution
- `note` = explanatory comment

### State Diagram Symbols:
- Rounded rectangle = state
- Arrow = transition
- `[condition]` = guard condition
- Black circle = start state
- Circle with dot = end state

## Export Options

To export diagrams as images for documentation:

### PNG (recommended for documents):
```powershell
java -jar plantuml.jar -tpng *.puml
```

### SVG (recommended for web, scalable):
```powershell
java -jar plantuml.jar -tsvg *.puml
```

### PDF (for printing):
```powershell
java -jar plantuml.jar -tpdf *.puml
```

## Integration with Documentation

These diagrams are referenced in:
- `DESIGN_DOCUMENTATION.md` - Contains embedded PlantUML code
- `README.md` - Project overview

For the best experience, view diagrams alongside the documentation to understand both the visual structure and detailed explanations.

