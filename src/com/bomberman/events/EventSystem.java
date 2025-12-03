package com.bomberman.events;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Generic event system for type-safe event handling.
 * Uses JCF (HashMap, ArrayList, PriorityQueue) for efficient event management.
 * Demonstrates Generic Programming with wildcard bounds.
 * 
 * @param <T> Base type of events this system handles
 * 
 * @author BomberQuest Team
 * @version 1.0
 */
public class EventSystem<T extends GameEvent> {
    /** Map of event types to their registered listeners */
    private final Map<Class<? extends T>, List<EventListener<? super T>>> listeners;
    
    /** Priority queue for delayed event processing */
    private final PriorityQueue<T> eventQueue;
    
    /**
     * Creates a new event system.
     */
    public EventSystem() {
        this.listeners = new HashMap<>();
        this.eventQueue = new PriorityQueue<>();
    }
    
    /**
     * Subscribes a listener to a specific event type.
     * Demonstrates bounded wildcards for type safety.
     * 
     * @param <E> Specific event type (must extend T)
     * @param eventType Class object for the event type
     * @param listener Listener to be notified when event occurs
     */
    @SuppressWarnings("unchecked")
    public <E extends T> void subscribe(Class<E> eventType, EventListener<E> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add((EventListener<? super T>) listener);
    }
    
    /**
     * Publishes an event immediately to all registered listeners.
     * 
     * @param event Event to publish
     */
    @SuppressWarnings("unchecked")
    public void publish(T event) {
        Class<? extends T> eventType = (Class<? extends T>) event.getClass();
        List<EventListener<? super T>> eventListeners = listeners.get(eventType);
        
        if (eventListeners != null) {
            for (EventListener<? super T> listener : eventListeners) {
                listener.onEvent(event);
            }
        }
    }
    
    /**
     * Queues an event for delayed processing based on priority.
     * 
     * @param event Event to queue
     */
    public void queue(T event) {
        eventQueue.offer(event);
    }
    
    /**
     * Processes all queued events in priority order.
     */
    public void processQueue() {
        while (!eventQueue.isEmpty()) {
            T event = eventQueue.poll();
            event.handle();
            publish(event);
        }
    }
    
    /**
     * Gets the number of queued events waiting to be processed.
     * 
     * @return Number of events in queue
     */
    public int getQueuedEventCount() {
        return eventQueue.size();
    }
    
    /**
     * Removes all listeners for a specific event type.
     * 
     * @param eventType Event type to unsubscribe all listeners from
     */
    public void unsubscribeAll(Class<? extends T> eventType) {
        listeners.remove(eventType);
    }
    
    /**
     * Clears all listeners and queued events.
     */
    public void clear() {
        listeners.clear();
        eventQueue.clear();
    }
}
