package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Playlist class
 *
 * Handles:
 *  - Filtering songs
 *  - Enforcing maxSongs
 *  - Storing results
 */
public class Playlist {

    public static final String ANY = "ANY";
    public static final int NO_LIMIT = -1;

    private final String name;
    private final List<Song> songs;

    /**
     * Constructor performs filtering
     */

    private String normalize(String input) {
        if (input == null) return "";

        return input.toLowerCase().replaceAll("[^a-z0-9]", ""); // removes spaces, hyphens, etc.
    }
    
    public Playlist(String name,
                    List<Song> allSongs,
                    String genre,
                    boolean allowExplicit,
                    int maxSongs) {

        this.name = name;
        this.songs = new ArrayList<>();

        int effectiveMaxSongs = maxSongs <= 0 ? NO_LIMIT : maxSongs;

        for (Song song : allSongs) {

        // Genre filter
        // Normalize user input + song genre for flexible matching
        boolean genreMatch =
                genre == null ||
                normalize(genre).equals(normalize(ANY)) ||
                normalize(song.getGenre()).equals(normalize(genre));

            // Explicit filter
            boolean explicitMatch =
                    allowExplicit || !song.isExplicit();

            if (genreMatch && explicitMatch) {
                songs.add(song);
            }

            // Enforce maxSongs
            if (effectiveMaxSongs != NO_LIMIT && songs.size() >= effectiveMaxSongs) {
                break;
            }
        }
    }

    public List<Song> getSongs() {
        return Collections.unmodifiableList(songs);
    }

    public boolean isEmpty() {
        return songs.isEmpty();
    }

    public int getSongCount() {
        return songs.size();
    }

    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=== PLAYLIST: ").append(name).append(" ===\n");

        if (songs.isEmpty()) {
            sb.append("No songs found.\n");
            return sb.toString();
        }

        int i = 1;
        for (Song s : songs) {
            sb.append(i++).append(". ").append(s.toString()).append("\n");
        }

        sb.append("Total songs: ").append(songs.size()).append("\n");

        return sb.toString();
    }
    
}
