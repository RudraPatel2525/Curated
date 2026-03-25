package controller;

import java.util.ArrayList;
import java.util.List;

import model.Playlist;
import model.Shuffle;
import model.Song;
import model.SongDatabase;

public class PlaylistController {

    private final SongDatabase songDatabase;
    private Playlist currentPlaylist;

    public PlaylistController(SongDatabase songDatabase) {
        if (songDatabase == null) {
            throw new IllegalArgumentException("SongDatabase must not be null.");
        }
        this.songDatabase = songDatabase;
        this.currentPlaylist = null;
    }

    /**
     * Generates a playlist based on:
     * - genre
     * - explicit preference
     * - max number of songs
     */
    public Playlist generatePlaylist(String genre, boolean allowExplicit, int maxSongs) {

        List<Song> allSongs = songDatabase.getAllSongs();
        List<Song> filteredSongs = new ArrayList<>();

        // Step 1: Filter songs manually
        for (Song song : allSongs) {
            // Genre match (case insensitive)
            boolean genreMatch = genre.equalsIgnoreCase("ANY") ||
                song.getGenre().equalsIgnoreCase(genre);

            // Explicit filter
            boolean explicitMatch = allowExplicit || !song.isExplicit();

            if (genreMatch && explicitMatch) {
                filteredSongs.add(song);
            }
        }

        // if no songs match criteria, return null 
        if (filteredSongs.isEmpty()) {
            System.out.println("No songs found matching criteria.");
            currentPlaylist = null;
            return null;
        }

        // Step 2: Apply maxSongs constraint, Boundary Value
        if (maxSongs > 0 && maxSongs < filteredSongs.size()) {
            filteredSongs = filteredSongs.subList(0, maxSongs);
        }

        // Step 3: Shuffle playlist (using your Shuffle class)
        Shuffle shuffle = new Shuffle(42); // fixed seed for deterministic testing
        List<Song> shuffled = shuffle.shuffle(filteredSongs);

        //Step 4: Create playlist (using the Playlist.java class)
        currentPlaylist = new Playlist("Generated Playlist", shuffled, genre, allowExplicit, maxSongs);

        return currentPlaylist;
    }

    public void displayCurrentPlaylist() {
        if (currentPlaylist == null) {
            System.out.println("No playlist generated.");
            return;
        }
       System.out.println(currentPlaylist.toDisplayString()); // Assuming Playlist has a display method to print songs
    }

    public boolean hasPlaylist() { // checks if a playlist has been generated
        return currentPlaylist != null;
    }

    public void reset() { // resets the controller state, clearing the current playlist
        currentPlaylist = null;
    }
    // playlist controller should not have direct access to songs, it should only interact through the song database and playlist
    public Playlist getCurrentPlaylist() { // returns the current playlist, useful for testing
        return currentPlaylist;
    }
}