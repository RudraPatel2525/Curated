package model;

import java.util.List;

/**
 * PlaylistController.java
 *
 * Acts as the controller in the MVC architecture for the "Curated" project
 * (ENSE 375). It coordinates the flow between the model components:
 *   - SongDatabase  (loads songs from file)
 *   - FilterEngine  (filters songs by user criteria)
 *   - Playlist      (holds and displays the final song list)
 *
 * The controller receives user input (genre, explicit preference, max songs),
 * drives the generation pipeline, and returns the resulting Playlist.
 * It also handles the case where no songs match the given criteria.
 *
 * @author Rida Hashmi
 */
public class PlaylistController {

    // -----------------------------------------------------------------------
    // Fields
    // -----------------------------------------------------------------------

    /** Database responsible for loading songs from a file. */
    private final SongDatabase songDatabase;

    /** The most recently generated playlist; null if none has been generated. */
    private Playlist currentPlaylist;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Constructs a PlaylistController with the given SongDatabase.
     *
     * @param songDatabase the data source for songs; must not be {@code null}
     * @throws IllegalArgumentException if {@code songDatabase} is {@code null}
     */
    public PlaylistController(SongDatabase songDatabase) {
        if (songDatabase == null) {
            throw new IllegalArgumentException("SongDatabase must not be null.");
        }
        this.songDatabase  = songDatabase;
        this.currentPlaylist = null;
    }

    // -----------------------------------------------------------------------
    // Core control flow
    // -----------------------------------------------------------------------

    /**
     * Generates a playlist based on the provided criteria.
     *
     * <p>Steps:
     * <ol>
     *   <li>Load all songs from the database.</li>
     *   <li>Apply filters (genre, explicit, maxSongs) via FilterEngine.</li>
     *   <li>If no songs match, return {@code null} and print an error message.</li>
     *   <li>Otherwise, wrap the filtered list in a Playlist and store it.</li>
     * </ol>
     *
     * @param genre         desired genre; use {@link FilterEngine#ANY} to skip
     * @param allowExplicit {@code true} if explicit songs are permitted
     * @param maxSongs      maximum number of songs; use {@link FilterEngine#NO_LIMIT} for no cap
     * @return the generated {@link Playlist}, or {@code null} if no songs matched
     */
    public Playlist generatePlaylist(String genre, boolean allowExplicit, int maxSongs) {
        // Step 1: Load all songs from the database
        List<Song> allSongs = songDatabase.loadSongs();

        // Step 2: Filter songs using FilterEngine
        FilterEngine filterEngine = new FilterEngine(genre, allowExplicit, maxSongs);
        List<Song> filteredSongs  = filterEngine.filter(allSongs);

        // Step 3: Handle case where no songs match the criteria
        if (filteredSongs.isEmpty()) {
            System.out.println("No songs found matching the selected criteria.");
            currentPlaylist = null;
            return null;
        }

        // Step 4: Create and store the playlist
        currentPlaylist = new Playlist(filteredSongs);
        return currentPlaylist;
    }

    /**
     * Prints the current playlist to standard output.
     * If no playlist has been generated yet, prints a message indicating so.
     */
    public void displayCurrentPlaylist() {
        if (currentPlaylist == null) {
            System.out.println("No playlist has been generated yet.");
            return;
        }
        currentPlaylist.printPlaylist();
    }

    /**
     * Returns {@code true} if a playlist has been successfully generated
     * and is available for display.
     *
     * @return {@code true} if a current playlist exists
     */
    public boolean hasPlaylist() {
        return currentPlaylist != null;
    }

    /**
     * Clears the current playlist, resetting the controller to its
     * initial state.
     */
    public void reset() {
        currentPlaylist = null;
    }

    // -----------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------

    /**
     * Returns the most recently generated playlist, or {@code null} if
     * none has been generated.
     *
     * @return the current {@link Playlist}, or {@code null}
     */
    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    /**
     * Returns the SongDatabase used by this controller.
     *
     * @return the {@link SongDatabase}
     */
    public SongDatabase getSongDatabase() {
        return songDatabase;
    }
}
