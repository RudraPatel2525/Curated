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
    public Playlist(String name,
                    List<Song> allSongs,
                    String genre,
                    boolean allowExplicit,
                    int maxSongs) {

        this.name = name;
        this.songs = new ArrayList<>();

        for (Song song : allSongs) {

            // Genre filter
            boolean genreMatch =
                    genre == null ||
                    genre.equalsIgnoreCase(ANY) ||
                    song.getGenre().equalsIgnoreCase(genre);

            // Explicit filter
            boolean explicitMatch =
                    allowExplicit || !song.isExplicit();

            if (genreMatch && explicitMatch) {
                songs.add(song);
            }

            // Enforce maxSongs
            if (maxSongs != NO_LIMIT && songs.size() >= maxSongs) {
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