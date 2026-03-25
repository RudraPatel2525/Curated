package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * SongTest.java
 * - ID is auto-generated using identityHashCode, Constructor does NOT take an ID anymore
 */
public class SongTest {

    // Test that constructor correctly sets all fields.
    @Test
    void testSongConstructor() {
        Song song = new Song("HUMBLE", "Kendrick Lamar", "HipHop", true);

        assertEquals("HUMBLE", song.getTitle());
        assertEquals("Kendrick Lamar", song.getArtist());
        assertEquals("HipHop", song.getGenre());
        assertTrue(song.isExplicit());
    }

    // Test that each song gets a unique ID automatically.
    @Test
    void testSongsHaveDifferentIDs() {
        Song song1 = new Song("SongA", "Artist", "Pop", false);
        Song song2 = new Song("SongA", "Artist", "Pop", false);

        assertNotEquals(song1.getId(), song2.getId(),
                "Each Song should have a unique generated ID.");
    }

    /**
     * Test that toString contains key info (title, artist, genre).
     *
     * NOTE:
     * - ID is NOT included in toString anymore
     */
    @Test
    void testToStringContainsInfo() {
        Song song = new Song("Stay", "Artist", "Pop", false);

        String result = song.toString();

        assertTrue(result.contains("Stay"));
        assertTrue(result.contains("Artist"));
        assertTrue(result.contains("Pop"));
    }
}