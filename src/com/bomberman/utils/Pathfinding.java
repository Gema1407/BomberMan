package com.bomberman.utils;

import java.util.*;

/**
 * A* pathfinding algorithm implementation for game AI.
 * Provides optimal path calculation from start to goal position.
 * 
 * @author BomberQuest Team
 * @version 2.0
 */
public class Pathfinding {
    
    /**
     * Represents a node in the pathfinding grid.
     */
    private static class Node implements Comparable<Node> {
        int x;
        int y;
        int gCost; // Distance from start
        int hCost; // Heuristic distance to goal
        Node parent;
        
        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        int fCost() {
            return gCost + hCost;
        }
        
        @Override
        public int compareTo(Node other) {
            int compare = Integer.compare(this.fCost(), other.fCost());
            if (compare == 0) {
                return Integer.compare(this.hCost, other.hCost);
            }
            return compare;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) return false;
            Node other = (Node) obj;
            return this.x == other.x && this.y == other.y;
        }
        
        @Override
        public int hashCode() {
            return x * 1000 + y;
        }
    }
    
    /**
     * Finds optimal path using A* algorithm.
     * 
     * @param startX Start X coordinate
     * @param startY Start Y coordinate
     * @param goalX Goal X coordinate
     * @param goalY Goal Y coordinate
     * @param gridW Grid width
     * @param gridH Grid height
     * @param isWalkable Lambda function to check if position is walkable
     * @return List of coordinates representing path, or null if no path exists
     */
    public static List<int[]> findPath(int startX, int startY, int goalX, int goalY, 
                                       int gridW, int gridH, 
                                       WalkableChecker isWalkable) {
        
        // Early exit if start or goal is invalid
        if (!isWalkable.isWalkable(startX, startY) || !isWalkable.isWalkable(goalX, goalY)) {
            return null;
        }
        
        // If already at goal
        if (startX == goalX && startY == goalY) {
            List<int[]> path = new ArrayList<>();
            path.add(new int[]{startX, startY});
            return path;
        }
        
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();
        Map<String, Node> allNodes = new HashMap<>();
        
        Node startNode = new Node(startX, startY);
        startNode.gCost = 0;
        startNode.hCost = heuristic(startX, startY, goalX, goalY);
        
        openSet.add(startNode);
        allNodes.put(startX + "," + startY, startNode);
        
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Up, Down, Left, Right
        
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            
            // Check if reached goal
            if (current.x == goalX && current.y == goalY) {
                return reconstructPath(current);
            }
            
            closedSet.add(current);
            
            // Check all neighbors
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                
                // Check bounds
                if (nx < 0 || nx >= gridW || ny < 0 || ny >= gridH) {
                    continue;
                }
                
                // Check if walkable
                if (!isWalkable.isWalkable(nx, ny)) {
                    continue;
                }
                
                String key = nx + "," + ny;
                Node neighbor = allNodes.getOrDefault(key, new Node(nx, ny));
                
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                
                int tentativeGCost = current.gCost + 1;
                
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGCost >= neighbor.gCost) {
                    continue;
                }
                
                neighbor.parent = current;
                neighbor.gCost = tentativeGCost;
                neighbor.hCost = heuristic(nx, ny, goalX, goalY);
                allNodes.put(key, neighbor);
            }
        }
        
        // No path found
        return null;
    }
    
    /**
     * Manhattan distance heuristic.
     */
    private static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    
    /**
     * Reconstructs path from goal to start.
     */
    private static List<int[]> reconstructPath(Node goal) {
        List<int[]> path = new ArrayList<>();
        Node current = goal;
        
        while (current != null) {
            path.add(new int[]{current.x, current.y});
            current = current.parent;
        }
        
        Collections.reverse(path);
        return path;
    }
    
    /**
     * Functional interface for checking if a position is walkable.
     */
    @FunctionalInterface
    public interface WalkableChecker {
        boolean isWalkable(int x, int y);
    }
}
