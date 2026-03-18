package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PlaylistControllerTest.java
 *
 * Test suite for PlaylistController covering:
 *   - MVC control flow (SongDatabase → FilterEngine → Shuffle → Playlist)
 *   - Use case testing (normal generation, no matches, explicit filtering,
 *     genre filtering, max size enforcement, reset behaviour)
 *
 * Tests use small, hand-crafted Song lists to keep them fast,
 * deterministic, and independent of any external file.
 *
 * @author Person 3
 * @version 1.0
 */
public class PlaylistControllerTest {

    // -----------------------------------------------------------------------
    // Shared fixtures
    // -----------------------------------------------------------------------

    /** A mixed song list reused across tests. */
    private List<Song> sampleSongs;

    /** Pop, clean */
    private Song popClean1;
    /** Pop, clean */
    private Song popClean2;
    /** Pop, explicit */
    private Song popExplicit;
    /** Rock, clean */
    private Song rockClean;
    /** Hip-Hop, explicit */
    private Song hipHopExplicit;

    @BeforeEach
    void setUp() {
        popClean1     = new Song("Levitating",      "Dua Lipa",        "Pop",     false);
        popClean2     = new Song("Blinding Lights",  "The Weeknd",      "Pop",     false);
        popExplicit   = new Song("Bad Guy",          "Billie Eilish",   "Pop",     true);
        rockClean     = new Song("Eye of the Tiger", "Survivor",        "Rock",    false);
        hipHopExplicit = new Song("HUMBLE.",         "Kendrick Lamar",  "Hip-Hop", true);

        sampleSongs = new ArrayList<>();
        sampleSongs.add(popClean1);
        sampleSongs.add(popClean2);
        sampleSongs.add(popExplicit);
        sampleSongs.add(rockClean);
        sampleSongs.add(hipHopExplicit);
    }

    // -----------------------------------------------------------------------
    // Helper: builds a controller backed by a fixed in-memory song list
    // -----------------------------------------------------------------------

    /**
     * Creates a PlaylistController whose SongDatabase returns {@code songs}
     * instead of reading from disk.
     */
    private PlaylistController controllerWith(List<Song> songs) {
        SongDatabase db = new SongDatabase("dummy.txt") {
            @Override
            public List<Song> loadSongs() {
                return new ArrayList<>(songs);
            }
        };
        return new PlaylistController(db);
    }

    // -----------------------------------------------------------------------
    // 1. Constructor & initial state
    // -----------------------------------------------------------------------

    @Test
    void testInitialState_noPlaylistExists() {
        PlaylistController controller = controllerWith(sampleSongs);
        assertFalse(controller.hasPlaylist(),
                "Controller should have no playlist before generation.");
        assertNull(controller.getCurrentPlaylist(),
                "getCurrentPlaylist() should return null before generation.");
    }

    @Test
    void testConstructor_nullDatabase_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new PlaylistController(null),
                "Null SongDatabase should throw IllegalArgumentException.");
    }

    // -----------------------------------------------------------------------
    // 2. Normal playlist generation (MVC control flow)
    // -----------------------------------------------------------------------

    @Test
    void testGeneratePlaylist_returnsNonNullPlaylist() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist("Pop", false, FilterEngine.NO_LIMIT);
        assertNotNull(result, "generatePlaylist() should return a Playlist when songs match.");
    }

    @Test
    void testGeneratePlaylist_controllerHasPlaylistAfterGeneration() {
        PlaylistController controller = controllerWith(sampleSongs);
        controller.generatePlaylist("Pop", false, FilterEngine.NO_LIMIT);
        assertTrue(controller.hasPlaylist(),
                "hasPlaylist() should be true after successful generation.");
        assertNotNull(controller.getCurrentPlaylist());
    }

    @Test
    void testGeneratePlaylist_playlistContainsCorrectSongs() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist("Pop", false, FilterEngine.NO_LIMIT);

        List<Song> songs = result.getSongs();
        assertTrue(songs.contains(popClean1), "Playlist should contain popClean1.");
        assertTrue(songs.contains(popClean2), "Playlist should contain popClean2.");
        assertFalse(songs.contains(rockClean),       "Playlist should not contain rockClean.");
        assertFalse(songs.contains(hipHopExplicit),  "Playlist should not contain hipHopExplicit.");
    }

    // -----------------------------------------------------------------------
    // 3. Genre filtering use cases
    // -----------------------------------------------------------------------

    @Test
    void testGeneratePlaylist_genreRock_returnsOnlyRockSongs() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist("Rock", false, FilterEngine.NO_LIMIT);

        assertNotNull(result);
        List<Song> songs = result.getSongs();
        assertEquals(1, songs.size(), "Only one Rock song should be in the playlist.");
        assertTrue(songs.contains(rockClean));
    }

    @Test
    void testGeneratePlaylist_genreHipHop_allowExplicit_returnsHipHopSong() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist("Hip-Hop", true, FilterEngine.NO_LIMIT);

        assertNotNull(result);
        assertTrue(result.getSongs().contains(hipHopExplicit));
    }

    @Test
    void testGeneratePlaylist_anyGenre_returnsAllEligibleSongs() {
        PlaylistController controller = controllerWith(sampleSongs);
        // allowExplicit=false → explicit songs excluded; 3 clean songs total
        Playlist result = controller.generatePlaylist(FilterEngine.ANY, false, FilterEngine.NO_LIMIT);

        assertNotNull(result);
        assertEquals(3, result.getSongs().size(),
                "Should contain all 3 clean songs when genre is ANY.");
    }

    // -----------------------------------------------------------------------
    // 4. Explicit content filtering use cases
    // -----------------------------------------------------------------------

    @Test
    void testGeneratePlaylist_explicitNotAllowed_excludesExplicitSongs() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist(FilterEngine.ANY, false, FilterEngine.NO_LIMIT);

        assertNotNull(result);
        for (Song song : result.getSongs()) {
            assertFalse(song.isExplicit(),
                    "No explicit songs should appear when allowExplicit=false.");
        }
    }

    @Test
    void testGeneratePlaylist_explicitAllowed_includesExplicitSongs() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);

        assertNotNull(result);
        boolean hasExplicit = result.getSongs().stream().anyMatch(Song::isExplicit);
        assertTrue(hasExplicit, "Explicit songs should be present when allowExplicit=true.");
    }

    @Test
    void testGeneratePlaylist_popExplicitNotAllowed_excludesPopExplicit() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist("Pop", false, FilterEngine.NO_LIMIT);

        assertNotNull(result);
        assertFalse(result.getSongs().contains(popExplicit),
                "Explicit Pop song should be excluded when allowExplicit=false.");
    }

    @Test
    void testGeneratePlaylist_popExplicitAllowed_includesPopExplicit() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist("Pop", true, FilterEngine.NO_LIMIT);

        assertNotNull(result);
        assertTrue(result.getSongs().contains(popExplicit),
                "Explicit Pop song should be included when allowExplicit=true.");
    }

    // -----------------------------------------------------------------------
    // 5. Max size enforcement use cases
    // -----------------------------------------------------------------------

    @Test
    void testGeneratePlaylist_maxSongs_limitsPlaylistSize() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist(FilterEngine.ANY, true, 2);

        assertNotNull(result);
        assertEquals(2, result.getSongs().size(),
                "Playlist should contain at most 2 songs when maxSongs=2.");
    }

    @Test
    void testGeneratePlaylist_maxSongsOne_returnsExactlyOneSong() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist(FilterEngine.ANY, true, 1);

        assertNotNull(result);
        assertEquals(1, result.getSongs().size());
    }

    @Test
    void testGeneratePlaylist_maxSongsLargerThanPool_returnsWholePool() {
        PlaylistController controller = controllerWith(sampleSongs);
        // 3 clean songs available; cap set higher than pool
        Playlist result = controller.generatePlaylist(FilterEngine.ANY, false, 100);

        assertNotNull(result);
        assertEquals(3, result.getSongs().size(),
                "Should not exceed the available eligible song count.");
    }

    // -----------------------------------------------------------------------
    // 6. No songs match — insufficient data use case
    // -----------------------------------------------------------------------

    @Test
    void testGeneratePlaylist_noMatch_returnsNull() {
        PlaylistController controller = controllerWith(sampleSongs);
        Playlist result = controller.generatePlaylist("Jazz", false, FilterEngine.NO_LIMIT);

        assertNull(result, "generatePlaylist() should return null when no songs match.");
    }

    @Test
    void testGeneratePlaylist_noMatch_hasPlaylistIsFalse() {
        PlaylistController controller = controllerWith(sampleSongs);
        controller.generatePlaylist("Jazz", false, FilterEngine.NO_LIMIT);
        assertFalse(controller.hasPlaylist(),
                "hasPlaylist() should be false when no songs matched.");
    }

    @Test
    void testGeneratePlaylist_emptyDatabase_returnsNull() {
        PlaylistController controller = controllerWith(new ArrayList<>());
        Playlist result = controller.generatePlaylist(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        assertNull(result, "Empty database should result in null playlist.");
    }

    // -----------------------------------------------------------------------
    // 7. Reset use case
    // -----------------------------------------------------------------------

    @Test
    void testReset_clearsCurrentPlaylist() {
        PlaylistController controller = controllerWith(sampleSongs);
        controller.generatePlaylist("Pop", false, FilterEngine.NO_LIMIT);
        assertTrue(controller.hasPlaylist(), "Playlist should exist before reset.");

        controller.reset();
        assertFalse(controller.hasPlaylist(), "hasPlaylist() should be false after reset.");
        assertNull(controller.getCurrentPlaylist(), "getCurrentPlaylist() should be null after reset.");
    }

    @Test
    void testReset_thenGenerateAgain_works() {
        PlaylistController controller = controllerWith(sampleSongs);
        controller.generatePlaylist("Pop", false, FilterEngine.NO_LIMIT);
        controller.reset();

        Playlist result = controller.generatePlaylist("Rock", false, FilterEngine.NO_LIMIT);
        assertNotNull(result, "Should be able to generate a new playlist after reset.");
        assertTrue(result.getSongs().contains(rockClean));
    }

    // -----------------------------------------------------------------------
    // 8. Multiple sequential generations
    // -----------------------------------------------------------------------

    @Test
    void testGenerateTwice_secondCallOverridesFirst() {
        PlaylistController controller = controllerWith(sampleSongs);

        Playlist first = controller.generatePlaylist("Rock", false, FilterEngine.NO_LIMIT);
        assertNotNull(first);

        Playlist second = controller.generatePlaylist("Pop", false, FilterEngine.NO_LIMIT);
        assertNotNull(second);

        assertEquals(second, controller.getCurrentPlaylist(),
                "getCurrentPlaylist() should reflect the most recent generation.");
        assertFalse(controller.getCurrentPlaylist().getSongs().contains(rockClean),
                "Previous Rock playlist should be replaced.");
    }
}
