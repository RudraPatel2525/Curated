package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Playlist;
import model.SongDatabase;

class BoundaryValueTest {

    private static final int POP_NON_EXPLICIT_COUNT = 53;

    private PlaylistController controller;

    @BeforeEach
    void setUp() throws Exception {
        SongDatabase db = new SongDatabase("src/main/resources/songs.csv");
        controller = new PlaylistController(db);
    }

    @Test
    void bv1_maxSongsZero_treatedAsNoLimit() {
        Playlist result = controller.generatePlaylist("Pop", false, 0);

        assertNotNull(result);
        assertEquals(POP_NON_EXPLICIT_COUNT, result.getSongs().size());
    }

    @Test
    void bv2_maxSongsOne_returnsExactlyOneSong() {
        Playlist result = controller.generatePlaylist("Pop", false, 1);

        assertNotNull(result);
        assertEquals(1, result.getSongs().size());
    }

    @Test
    void bv3_maxSongsTwo_returnsExactlyTwoSongs() {
        Playlist result = controller.generatePlaylist("Pop", false, 2);

        assertNotNull(result);
        assertEquals(2, result.getSongs().size());
    }

    @Test
    void bv4_maxSongsNMinusOne_returnsTrimmedList() {
        Playlist result = controller.generatePlaylist("Pop", false, 52);

        assertNotNull(result);
        assertEquals(52, result.getSongs().size());
    }

    @Test
    void bv5_maxSongsEqualsAvailable_returnsAll() {
        Playlist result = controller.generatePlaylist("Pop", false, 53);

        assertNotNull(result);
        assertEquals(POP_NON_EXPLICIT_COUNT, result.getSongs().size());
    }

    @Test
    void bv6_maxSongsExceedsAvailable_returnsAll() {
        Playlist result = controller.generatePlaylist("Pop", false, 54);

        assertNotNull(result);
        assertEquals(POP_NON_EXPLICIT_COUNT, result.getSongs().size());
    }

    @Test
    void bv7_maxSongsNegativeOne_canonicalNoLimit() {
        Playlist result = controller.generatePlaylist("Pop", false, -1);

        assertNotNull(result);
        assertEquals(POP_NON_EXPLICIT_COUNT, result.getSongs().size());
    }
}
