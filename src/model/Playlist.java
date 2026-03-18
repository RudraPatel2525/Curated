package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Playlist.java
 *
 * Represents a generated music playlist for the "Curated" project (ENSE 375).
 *
 * A Playlist is an ordered, immutable-after-generation collection of {@link Song}
 * objects produced by the {@link FilterEngine} and optionally shuffled by the
 * Shuffle utility. It is a pure in-memory, display-only structure — no data is
 * persisted to disk.
 *
 * Responsibilities:
 *   - Store and expose the ordered list of songs
 *   - Track aggregate metadata (total duration, song count)
 *   - Provide human-readable display output
 *   - Record the criteria that were used to build the playlist
 *
 * @author Rida Hashmi - 200504477
 * @version 1.0
 */
public class Playlist {

    // -----------------------------------------------------------------------
    // Fields
    // -----------------------------------------------------------------------

    /** Human-readable name for this playlist. */
    private final String name;

    /** Ordered list of songs in the playlist. */
    private final List<Song> songs;

    // Criteria snapshot — stored for display and traceability
    private final String criteriaGenre;
    private final String criteriaMood;
    private final boolean criteriaAllowExplicit;
    private final int     criteriaMaxSongs;
    private final int     criteriaMaxDurationSecs;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /**
     * Constructs a Playlist from a pre-filtered (and optionally pre-shuffled)
     * list of songs, recording the filter criteria used to produce it.
     *
     * @param name                   human-readable playlist name; must not be null or blank
     * @param songs                  ordered list of songs to include; must not be null
     * @param criteriaGenre          genre criterion that was applied
     * @param criteriaMood           mood criterion that was applied
     * @param criteriaAllowExplicit  whether explicit songs were permitted
     * @param criteriaMaxSongs       the song-count cap that was applied
     * @param criteriaMaxDurationSecs the duration cap (seconds) that was applied
     * @throws IllegalArgumentException if name is null/blank or songs is null
     */
    public Playlist(String name,
                    List<Song> songs,
                    String criteriaGenre,
                    String criteriaMood,
                    boolean criteriaAllowExplicit,
                    int criteriaMaxSongs,
                    int criteriaMaxDurationSecs) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Playlist name must not be null or blank.");
        }
        if (songs == null) {
            throw new IllegalArgumentException("Song list must not be null.");
        }

        this.name                    = name.trim();
        this.songs                   = new ArrayList<>(songs); // defensive copy
        this.criteriaGenre           = criteriaGenre;
        this.criteriaMood            = criteriaMood;
        this.criteriaAllowExplicit   = criteriaAllowExplicit;
        this.criteriaMaxSongs        = criteriaMaxSongs;
        this.criteriaMaxDurationSecs = criteriaMaxDurationSecs;
    }

    /**
     * Convenience constructor — criteria defaults are not recorded.
     * Useful for simple unit tests or manual playlist construction.
     *
     * @param name  playlist name
     * @param songs list of songs
     */
    public Playlist(String name, List<Song> songs) {
        this(name, songs,
             FilterEngine.ANY, FilterEngine.ANY,
             true,
             FilterEngine.NO_LIMIT,
             FilterEngine.NO_LIMIT);
    }

    // -----------------------------------------------------------------------
    // Aggregate queries
    // -----------------------------------------------------------------------

    /**
     * Returns the number of songs in this playlist.
     *
     * @return song count (0 if the playlist is empty)
     */
    public int getSongCount() {
        return songs.size();
    }

    /**
     * Returns {@code true} if this playlist contains no songs.
     *
     * @return {@code true} when empty
     */
    public boolean isEmpty() {
        return songs.isEmpty();
    }

    /**
     * Returns the total playback duration of all songs combined, in seconds.
     *
     * @return sum of {@link Song#getDurationSecs()} for every song in the list
     */
    public int getTotalDurationSecs() {
        int total = 0;
        for (Song song : songs) {
            total += song.getDurationSecs();
        }
        return total;
    }

    /**
     * Returns the total duration formatted as {@code MM:SS} (or {@code HH:MM:SS}
     * when the playlist exceeds one hour).
     *
     * @return formatted duration string
     */
    public String getFormattedDuration() {
        return formatSeconds(getTotalDurationSecs());
    }

    // -----------------------------------------------------------------------
    // Song access
    // -----------------------------------------------------------------------

    /**
     * Returns an unmodifiable view of the songs in this playlist.
     *
     * @return read-only list of songs in playlist order
     */
    public List<Song> getSongs() {
        return Collections.unmodifiableList(songs);
    }

    /**
     * Returns the song at the given zero-based position.
     *
     * @param index position in the playlist (0-based)
     * @return the song at that position
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public Song getSongAt(int index) {
        return songs.get(index);
    }

    /**
     * Returns {@code true} if this playlist contains the given song
     * (compared by object equality via {@link Song#equals(Object)}).
     *
     * @param song song to search for
     * @return {@code true} if found
     */
    public boolean contains(Song song) {
        return songs.contains(song);
    }

    // -----------------------------------------------------------------------
    // Display
    // -----------------------------------------------------------------------

    /**
     * Prints a formatted representation of this playlist to standard output.
     *
     * Example output:
     * <pre>
     * ============================================================
     *  PLAYLIST: Afternoon Vibes
     * ============================================================
     *  Genre   : Pop          Mood    : Happy
     *  Explicit: No           Max Songs: 10
     * ------------------------------------------------------------
     *  #   Title                     Artist               Duration
     * ------------------------------------------------------------
     *  1.  Blinding Lights           The Weeknd            3:20
     *  2.  Levitating                Dua Lipa              3:24
     * ------------------------------------------------------------
     *  Songs: 2    Total Duration: 6:44
     * ============================================================
     * </pre>
     */
    public void display() {
        System.out.println(toDisplayString());
    }

    /**
     * Returns the same formatted output as {@link #display()} but as a
     * {@code String}, making it easy to capture in tests.
     *
     * @return formatted playlist string
     */
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        String divider  = "=".repeat(64);
        String separator = "-".repeat(64);

        sb.append(divider).append("\n");
        sb.append(String.format(" PLAYLIST: %s%n", name));
        sb.append(divider).append("\n");

        // Criteria summary
        sb.append(String.format(" Genre   : %-14s Mood    : %s%n",
                criteriaGenre, criteriaMood));
        sb.append(String.format(" Explicit: %-14s Max Songs: %s%n",
                criteriaAllowExplicit ? "Yes" : "No",
                criteriaMaxSongs == FilterEngine.NO_LIMIT ? "Unlimited"
                                                          : String.valueOf(criteriaMaxSongs)));

        sb.append(separator).append("\n");

        if (songs.isEmpty()) {
            sb.append(" No songs matched the selected criteria.\n");
        } else {
            // Column header
            sb.append(String.format(" %-4s %-26s %-22s %s%n",
                    "#", "Title", "Artist", "Duration"));
            sb.append(separator).append("\n");

            for (int i = 0; i < songs.size(); i++) {
                Song s = songs.get(i);
                sb.append(String.format(" %-4d %-26s %-22s %s%n",
                        i + 1,
                        truncate(s.getTitle(),  25),
                        truncate(s.getArtist(), 21),
                        formatSeconds(s.getDurationSecs())));
            }
        }

        sb.append(separator).append("\n");
        sb.append(String.format(" Songs: %-5d  Total Duration: %s%n",
                getSongCount(), getFormattedDuration()));
        sb.append(divider).append("\n");

        return sb.toString();
    }

    // -----------------------------------------------------------------------
    // Getters — criteria
    // -----------------------------------------------------------------------

    public String getName()                  { return name; }
    public String getCriteriaGenre()         { return criteriaGenre; }
    public String getCriteriaMood()          { return criteriaMood; }
    public boolean isCriteriaAllowExplicit() { return criteriaAllowExplicit; }
    public int getCriteriaMaxSongs()         { return criteriaMaxSongs; }
    public int getCriteriaMaxDurationSecs()  { return criteriaMaxDurationSecs; }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Formats a duration in seconds to {@code M:SS} or {@code H:MM:SS}.
     */
    private static String formatSeconds(int totalSecs) {
        int hours   = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Truncates a string to at most {@code maxLen} characters, appending
     * an ellipsis if the string was shortened.
     */
    private static String truncate(String s, int maxLen) {
        if (s == null)          return "";
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 1) + "…";
    }

    // -----------------------------------------------------------------------
    // Object overrides
    // -----------------------------------------------------------------------

    @Override
    public String toString() {
        return "Playlist{"
                + "name='"    + name       + '\''
                + ", songs="  + getSongCount()
                + ", duration=" + getFormattedDuration()
                + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Playlist)) return false;
        Playlist other = (Playlist) obj;
        return name.equals(other.name) && songs.equals(other.songs);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + songs.hashCode();
        return result;
    }
}
