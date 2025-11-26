# BomberQuest - Refactored Structure

This project has been refactored to use a standard Java package structure with complete design pattern implementation.

## 📚 Complete Documentation

For detailed design documentation, architecture diagrams, and UML visualizations:

- **[DESIGN_DOCUMENTATION.md](DESIGN_DOCUMENTATION.md)** - Complete design patterns & architecture
- **[DESIGN_VISUALIZATION.html](DESIGN_VISUALIZATION.html)** - Interactive HTML documentation (Open in browser)
- **[QUICK_REFERENCE.md](QUICK_REFERENCE.md)** - Quick reference guide for design patterns
- **[diagrams/](diagrams/)** - PlantUML diagrams (Class, State, Sequence diagrams)
- **[SOUND_ASSETS.md](SOUND_ASSETS.md)** - Sound effects documentation

### View Diagrams Online
1. Open any `.puml` file from `diagrams/` folder
2. Copy content
3. Paste to https://www.plantuml.com/plantuml/uml/
4. View generated diagram

## Project Structure

- `src/`: Contains the source code.
  - `com.bomberman`: Main package.
    - `BomberQuest.java`: Main entry point.
    - `core/`: Core game logic (GameManager, EntityFactory).
    - `entities/`: Game entities (Player, Enemy, Bomb, etc.).
    - `states/`: Game states (Menu, Playing, GameOver, etc.).
    - `managers/`: Managers (LeaderboardManager).
    - `exceptions/`: Custom exceptions.

## How to Compile and Run

### Using Command Line

1.  **Compile:**
    Open a terminal in the project root (`BomberMan/BomberMan`) and run:
    ```bash
    mkdir bin
    javac -d bin -sourcepath src src/com/bomberman/BomberQuest.java
    ```

2.  **Run:**
    ```bash
    java -cp bin com.bomberman.BomberQuest
    ```

### Using an IDE (VS Code, IntelliJ, Eclipse)

-   Open the project folder.
-   Ensure the `src` folder is marked as the "Source Root".
-   Run `BomberQuest.java`.

