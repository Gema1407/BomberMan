# BomberQuest - Generic Programming Documentation

## Overview

This document describes the generic programming features implemented in BomberQuest, demonstrating type-safe, reusable components using Java Generics.

## Generic Utilities

### 1. ObjectPool\<T\>

**Purpose**: Efficient object reuse with garbage collection reduction

**Location**: `com.bomberman.utils.ObjectPool`

**Type Parameter**:
- `<T>` - Type of objects to pool

**Usage Example**:
```java
// Create pool for Explosion objects
ObjectPool<Explosion> explosionPool = new ObjectPool<>(
    () -> new Explosion(0, 0),  // Factory
    50                          // Max pool size
);

// Acquire object from pool
Explosion explosion = explosionPool.acquire();
explosion.setPosition(x, y);

// Release back to pool when done
explosionPool.release(explosion);
```

**Benefits**:
- Reduces object creation overhead
- Type-safe - compile-time checking
- Customizable factory and capacity
- Automatic object recycling

---

### 2. Grid\<T\>

**Purpose**: Type-safe 2D grid data structure

**Location**: `com.bomberman.utils.Grid`

**Type Parameter**:
- `<T>` - Type of elements stored in grid

**Usage Example**:
```java
// Create grid for storing game tiles
Grid<GameObject> gameGrid = new Grid<>(25, 15);

// Set values
gameGrid.set(5, 5, new Wall(5, 5, true));

// Get values
GameObject tile = gameGrid.get(5, 5);

// Stream operations
List<Wall> walls = gameGrid.stream()
    .filter(obj -> obj instanceof Wall)
    .map(obj -> (Wall) obj)
    .collect(Collectors.toList());

// Iterate over all positions
gameGrid.forEach((x, y) -> {
    if (gameGrid.get(x, y) == null) {
        System.out.println("Empty at: " + x + ", " + y);
    }
});
```

**Benefits**:
- Type-safe element access
- Bounds checking
- Stream API integration
- Functional operations

---

### 3. EventSystem\<T extends GameEvent\>

**Purpose**: Type-safe event handling with priority queue

**Location**: `com.bomberman.events.EventSystem`

**Type Parameters**:
- `<T extends GameEvent>` - Base event type with upper bound
- Uses bounded wildcards for flexibility

**Usage Example**:
```java
// Create event system for game events
EventSystem<GameEvent> eventSystem = new EventSystem<>();

// Subscribe to specific event types
eventSystem.subscribe(BombExplodedEvent.class, event -> {
    System.out.println("Bomb exploded at: " + event.getPosition());
});

eventSystem.subscribe(PlayerDamagedEvent.class, event -> {
    player.damage();
});

// Publish event immediately
eventSystem.publish(new BombExplodedEvent(5, 5));

// Queue event for later processing
eventSystem.queue(new DelayedEvent(100));

// Process all queued events
eventSystem.processQueue();
```

**Benefits**:
- Type-safe event handling
- Priority-based processing
- Bounded wildcards for listener flexibility
- Separation of immediate vs. queued events

---

### 4. EventListener\<T extends GameEvent\>

**Purpose**: Functional interface for event handling

**Location**: `com.bomberman.events.EventListener`

**Type Parameter**:
- `<T extends GameEvent>` - Type of event to handle (bounded)

**Usage Example**:
```java
// Lambda expression
EventListener<BombEvent> listener = event -> {
    handleBombEvent(event);
};

// Method reference
EventListener<PlayerEvent> playerListener = this::onPlayerEvent;

// Anonymous class
EventListener<GameEvent> genericListener = new EventListener<GameEvent>() {
    @Override
    public void onEvent(GameEvent event) {
        // Handle event
    }
};
```

**Benefits**:
- Functional programming support
- Works with lambdas and method references
- Type-safe event handling
- Clean, readable code

---

## Advanced Generic Concepts

### Type Bounds

**Upper Bounds** (`<T extends SomeClass>`):
```java
public class EventSystem<T extends GameEvent> {
    // T must be GameEvent or subclass
    public void publish(T event) {
        event.handle();  // Can call GameEvent methods
    }
}
```

**Multiple Bounds**:
```java
// Example (not used in project but demonstrates concept)
public <T extends Comparable<T> & Serializable> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### Wildcards

**Upper Bounded Wildcard** (`? extends T`):
```java
// EventSystem.java
public <E extends T> void subscribe(
    Class<E> eventType, 
    EventListener<E> listener
) {
    // E can be any subtype of T
}
```

**Lower Bounded Wildcard** (`? super T`):
```java
// Used in EventSystem for flexible listener storage
List<EventListener<? super T>> listeners;
// Can accept listeners for T or any supertype
```

### Generic Methods

```java
// Grid.java - Generic method with different type parameter
public <R> R transform(Function<T, R> mapper, int x, int y) {
    T value = get(x, y);
    return mapper.apply(value);
}
```

---

## Design Patterns with Generics

### Factory Pattern + Generics

```java
// ObjectPool uses generic factory (Supplier<T>)
public class ObjectPool<T> {
    private final Supplier<T> factory;
    
    public ObjectPool(Supplier<T> factory, int maxSize) {
        this.factory = factory;
    }
    
    public T acquire() {
        return factory.get();  // Type-safe creation
    }
}
```

### Observer Pattern + Generics

```java
// EventListener is generic observer
@FunctionalInterface
public interface EventListener<T extends GameEvent> {
    void onEvent(T event);
}

// EventSystem is generic subject
public class EventSystem<T extends GameEvent> {
    private Map<Class<? extends T>, List<EventListener<? super T>>> listeners;
}
```

---

## Type Erasure and Limitations

### What Java Erases

```java
// At compile time:
List<String> strings = new ArrayList<String>();

// At runtime (type erased):
List strings = new ArrayList();
```

### Workarounds Used

**1. Class Tokens** (for event types):
```java
// Pass Class<E> to preserve type information
public <E extends T> void subscribe(Class<E> eventType, EventListener<E> listener)
```

**2. Generic Array Creation**:
```java
// Grid.java - Use Object array with suppression
@SuppressWarnings("unchecked")
public Grid(int width, int height) {
    this.data = (T[][]) new Object[height][width];
}
```

---

## Best Practices Applied

### 1. Use Meaningful Type Names
```java
public class ObjectPool<T>           // Good: T for Type
public class Grid<T>                 // Good: T for element Type
public class EventSystem<T extends GameEvent>  // Good: T with bound
```

### 2. Prefer Bounded Types
```java
// Instead of:
public class EventSystem<T>

// Use:
public class EventSystem<T extends GameEvent>
// Provides more type safety and enables calling GameEvent methods
```

### 3. Use Wildcards for Flexibility
```java
// Flexible listener storage
List<EventListener<? super T>> listeners;

// Allows adding listeners for T or any supertype
```

### 4. Document Type Parameters
```java
/**
 * Generic object pool.
 * 
 * @param <T> The type of objects to pool
 */
public class ObjectPool<T> { }
```

---

## Performance Considerations

### Benefits of Generics

1. **No Boxing/Unboxing**:
   ```java
   ObjectPool<Explosion>  // Direct object references
   // vs
   List numbers            // Would require Integer boxing
   ```

2. **Compile-Time Checking**:
   - Errors caught at compile time
   - No runtime ClassCastException
   - Better IDE support

3. **Code Reuse**:
   - Single ObjectPool for all types
   - Single Grid for all element types
   - Reduces code duplication

### Type Erasure Impact

- **Minimal Runtime Overhead**: Generics are compile-time only
- **No Additional Memory**: Type parameters don't exist at runtime
- **Same Performance**: Generic and non-generic code perform identically

---

## Future Enhancements

Potential additions to demonstrate more generic concepts:

### 1. Builder Pattern with Generics
```java
public class GameObjectBuilder<T extends GameObject> {
    public T build();
}
```

### 2. Generic Comparators
```java
public class ScoreComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        return Integer.compare(p1.getScore(), p2.getScore());
    }
}
```

### 3. Generic State Machine
```java
public class StateMachine<S extends State, E extends Event> {
    public void transition(S from, S to, E event);
}
```

---

## Testing Generics

See test examples in:
- `ObjectPoolTest.java` - Tests generic pool with String type
- `GridTest.java` - Tests generic grid with String type

Both demonstrate that generics work with any type while maintaining type safety.

---

**Summary**:
- ✅ 4 generic utility classes
- ✅ Type bounds and wildcards
- ✅ Generic methods and interfaces
- ✅ Integration with JCF (Map, List, Queue)
- ✅ Comprehensive documentation and tests

**Last Updated**: 2025-12-03
**Version**: 1.0
