package com.bomberman.events;

/**
 * Functional interface for listening to game events.
 * Demonstrates Generic Programming with type constraints.
 * 
 * @param <T> Type of event this listener handles (must extend GameEvent)
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
@FunctionalInterface
public interface EventListener<T extends GameEvent> {
    /**
     * Called when an event of type T occurs.
     * 
     * @param event The event that occurred
     */
    void onEvent(T event);
}
