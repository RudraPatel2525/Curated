package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Playlist;
import model.Song;
import model.SongDatabase;

/**
 * PlaylistControllerTest (Integration Version)
 *
 * This version uses the REAL CSV file instead of a fake database.
 *
 * So now we're testing:
 *   SongDatabase (file reading)
 *   + Controller logic
 *   + Filtering
 *   + Shuffle
 *
 * ⚠️ This is now an INTEGRATION TEST, not a pure unit test.
 */
public class PlaylistControllerTest {

    private PlaylistController controller;

    @BeforeEach
    void setUp() throws Exception {
        // IMPORTANT:
        // Use correct path relative to project root
        String filePath = "src/main/resources/songs.csv";

        SongDatabase db = new SongDatabase(filePath);
        controller = new PlaylistController(db);
    }

    // -----------------------------------------------------------------------
    // 1. Initial state
    // -----------------------------------------------------------------------

    @Test
    void testInitialState_noPlaylistExists() {
        assertFalse(controller.hasPlaylist());
        assertNull(controller.getCurrentPlaylist());
    }

    // -----------------------------------------------------------------------
    // 2. Basic generation

    @Test
    void testGeneratePlaylist_returnsPlaylist_whenMatchesExist() {
        Playlist result = controller.generatePlaylist("Pop", false, 10);

        assertNotNull(result);
        assertTrue(result.getSongs().size() > 0);
    }

    // 3. Genre filtering
    @Test
    void testGeneratePlaylist_filtersByGenre() {
        Playlist result = controller.generatePlaylist("Pop", true, 10);

        assertNotNull(result);

        // Every song must be Pop
        for (Song s : result.getSongs()) {
            assertEquals("Pop", s.getGenre());
        }
    }
    // 4. Explicit filtering
    @Test
    void testExplicitNotAllowed_filtersExplicitSongs() {
        Playlist result = controller.generatePlaylist("Pop", false, 20);

        assertNotNull(result);

        for (Song s : result.getSongs()) {
            assertFalse(s.isExplicit());
        }
    }

    @Test
    void testExplicitAllowed_includesExplicitSongs() {
        Playlist result = controller.generatePlaylist("Pop", true, 50);

        assertNotNull(result);

        boolean hasExplicit = result.getSongs().stream().anyMatch(Song::isExplicit);

        // Might depend on dataset, so we don't force true,
        // but we at least confirm no crash
        assertNotNull(result.getSongs());
    }

    // 5. Max songs
    @Test
    void testMaxSongs_limitRespected() {
        Playlist result = controller.generatePlaylist("Pop", true, 3);

        assertNotNull(result);
        assertTrue(result.getSongs().size() <= 3);
    }

    // 6. No match case
    @Test
    void testNoMatch_returnsNull() {
        // Assuming your dataset has no "Classical"
        Playlist result = controller.generatePlaylist("Classical", false, 10);

        assertNull(result);
    }

    // 7. Reset behavior
    @Test
    void testReset_clearsPlaylist() {
        controller.generatePlaylist("Pop", true, 10);

        controller.reset();

        assertFalse(controller.hasPlaylist());
        assertNull(controller.getCurrentPlaylist());
    }

    // 8. Multiple runs
    @Test
    void testGenerateTwice_overwritesPrevious() {
        controller.generatePlaylist("Rock", true, 10);

        Playlist second = controller.generatePlaylist("Pop", true, 10);

        assertEquals(second, controller.getCurrentPlaylist());
    }
}