package com.bomberman.events;

/**
 * Base class for all game events.
 * Implements Comparable for priority-based event processing.
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public abstract class GameEvent implements Comparable<GameEvent> {
    /** Timestamp when event was created */
    private final long timestamp;
    
    /** Priority of this event (lower values = higher priority) */
    private final int priority;
    
    /** Default priority for events */
    protected static final int DEFAULT_PRIORITY = 100;
    
    /**
     * Creates a new game event with default priority.
     */
    protected GameEvent() {
        this(DEFAULT_PRIORITY);
    }
    
    /**
     * Creates a new game event with specified priority.
     * 
     * @param priority Event priority (lower = higher priority)
     */
    protected GameEvent(int priority) {
        this.timestamp = System.currentTimeMillis();
        this.priority = priority;
    }
    
    /**
     * Gets the timestamp when this event was created.
     * 
     * @return Event creation timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Gets the priority of this event.
     * 
     * @return Event priority value
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * Compares events by priority, then by timestamp.
     * Events with lower priority values come first.
     * 
     * @param other Event to compare to
     * @return Negative if this event has higher priority, positive if lower
     */
    @Override
    public int compareTo(GameEvent other) {
        int priorityComparison = Integer.compare(this.priority, other.priority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        return Long.compare(this.timestamp, other.timestamp);
    }
    
    /**
     * Handles this event. Subclasses should implement event-specific logic.
     */
    public abstract void handle();
}
