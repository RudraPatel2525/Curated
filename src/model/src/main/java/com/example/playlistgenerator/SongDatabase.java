package com.example.playlistgenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongDatabase {
    private final List<Song> songs = new ArrayList<>();

    public SongDatabase(String filePath) throws IOException {
        loadSongs(filePath);
    }

    private void loadSongs(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().startsWith("id,")) {
                        continue;
                    }
                }

                Song song = parseSong(line, lineNumber);
                songs.add(song);
            }
        }
    }

    private Song parseSong(String line, int lineNumber) {
        String[] parts = line.split(",");

        if (parts.length != 8) {
            throw new IllegalArgumentException(
                "Invalid song data at line " + lineNumber +
                ". Expected 8 fields but found " + parts.length
            );
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            String title = parts[1].trim();
            String artist = parts[2].trim();
            String genre = parts[3].trim();
            String mood = parts[4].trim();
            boolean explicit = Boolean.parseBoolean(parts[5].trim());
            int minAge = Integer.parseInt(parts[6].trim());
            int durationSeconds = Integer.parseInt(parts[7].trim());

            return new Song(id, title, artist, genre, mood, explicit, minAge, durationSeconds);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid number format at line " + lineNumber + ": " + line, e
            );
        }
    }

    public List<Song> getAllSongs() {
        return Collections.unmodifiableList(songs);
    }

    public int size() {
        return songs.size();
    }
}
