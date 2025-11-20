package com.bomberman.managers;

import java.io.*;
import java.util.*;

public class LeaderboardManager {
    private static final String FILE_NAME = "leaderboard.dat";
    private Map<String, Integer> scores;

    private static LeaderboardManager instance;

    private LeaderboardManager() {
        scores = new HashMap<>();
        loadScores();
    }

    public static LeaderboardManager getInstance() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }

    private void loadScores() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    scores.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addScore(String name, int time) {
        if (scores.containsKey(name)) {
            if (time < scores.get(name)) {
                scores.put(name, time); // Update if better time (lower is better)
            }
        } else {
            scores.put(name, time);
        }
        saveScores();
    }

    private void saveScores() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Map.Entry<String, Integer>> getTopScores() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());
        list.sort(Map.Entry.comparingByValue()); // Ascending order (lower time is better)
        return list;
    }

    public boolean isNameTaken(String name) {
        return scores.containsKey(name);
    }
}
