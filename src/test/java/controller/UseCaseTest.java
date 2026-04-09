package controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

class UseCaseTest {

    private SongDatabase db;
    private PlaylistController controller;

    @BeforeEach
    void setUp() throws Exception {
        db = new SongDatabase("src/main/resources/songs.csv");
        controller = new PlaylistController(db);
    }

    @Test
    void uc1_mainFlow_generatePlaylistSuccessfully() {
        Playlist result = controller.generatePlaylist("Pop", false, 10);

        assertNotNull(result);
        assertTrue(result.getSongs().size() <= 10);
        assertTrue(result.getSongs().stream().allMatch(song -> song.getGenre().equalsIgnoreCase("Pop")));
        assertTrue(result.getSongs().stream().noneMatch(Song::isExplicit));
    }

    @Test
    void uc1_alternativeFlow_invalidGenreReturnsNull() {
        Playlist result = controller.generatePlaylist("Classical", false, 10);

        assertNull(result);
        assertFalse(controller.hasPlaylist());
    }

    @Test
    void uc1_include_filterSongs_explicitExcluded() {
        Playlist result = controller.generatePlaylist("Pop", false, Playlist.NO_LIMIT);

        assertNotNull(result);
        assertTrue(result.getSongs().stream().noneMatch(Song::isExplicit));
    }

    @Test
    void uc1_include_loadSongs_databaseLoadedCorrectly() {
        assertEquals(206, db.size());
    }

    @Test
    void uc2_mainFlow_viewExistingPlaylist() {
        controller.generatePlaylist("Rock", true, 5);

        assertTrue(controller.hasPlaylist());
        assertNotNull(controller.getCurrentPlaylist());
        assertFalse(controller.getCurrentPlaylist().getSongs().isEmpty());
    }

    @Test
    void uc2_alternativeFlow_noPlaylistToView() {
        assertFalse(controller.hasPlaylist());
        assertNull(controller.getCurrentPlaylist());
    }

    @Test
    void uc3_mainFlow_resetClearsPlaylist() {
        controller.generatePlaylist("Jazz", false, 5);

        controller.reset();

        assertFalse(controller.hasPlaylist());
        assertNull(controller.getCurrentPlaylist());
    }

    @Test
    void uc3_alternativeFlow_resetWithNoPlaylist_noCrash() {
        assertDoesNotThrow(() -> controller.reset());
        assertFalse(controller.hasPlaylist());
        assertNull(controller.getCurrentPlaylist());
    }
}
