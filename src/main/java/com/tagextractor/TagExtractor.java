package main.java.com.tagextractor;

import java.util.*;
import java.io.*;

public class TagExtractor {
    private Map<String, Integer> tagFrequency;
    private Set<String> stopWords;

    public TagExtractor() {
        tagFrequency = new HashMap<>();
        stopWords = new HashSet<>();
    }

    public void loadStopWords(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.trim().toLowerCase());
            }
        }
    }

    public void extractTags(String filename) throws IOException {
        tagFrequency.clear(); // Clear previous counts
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("\\W+"); // Split by non-word characters
                for (String word : words) {
                    if (!stopWords.contains(word) && !word.isEmpty()) {
                        tagFrequency.put(word, tagFrequency.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }
    }

    public Map<String, Integer> getTagFrequency() {
        return tagFrequency;
    }
}
