package model;

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

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) continue;

                // Skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().contains("title")) continue;
                }

                Song song = parseSong(line);
                songs.add(song);
            }
        }
    }

    private Song parseSong(String line) {

        String[] parts = line.split(",");

        if (parts.length != 4) {
            throw new IllegalArgumentException(
                "Invalid song format. Expected: title,artist,genre,explicit"
            );
        }

        String title = parts[0].trim();
        String artist = parts[1].trim();
        String genre = parts[2].trim();
        boolean explicit = Boolean.parseBoolean(parts[3].trim());

        return new Song(title, artist, genre, explicit);
    }

    public List<Song> getAllSongs() {
        return Collections.unmodifiableList(songs); // Return unmodifiable list to prevent external modification
    }

    public List<Song> loadSongs() { // For testing purposes, allows overriding the loading mechanism
        return getAllSongs();
    }
    public int size() {
        return songs.size();
    }
}