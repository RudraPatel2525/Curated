package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    @Test
    void testSongConstructor() {
        Song song = new Song(1, "HUMBLE", "Kendrick Lamar", "HipHop", true);

        assertEquals(1, song.getId());
        assertEquals("HUMBLE", song.getTitle());
        assertEquals("Kendrick Lamar", song.getArtist());
        assertEquals("HipHop", song.getGenre());
        assertTrue(song.isExplicit());
    }

    @Test
    void testSongsHaveDifferentIDs() {
        Song song1 = new Song(1, "SongA", "Artist", "Pop", false);
        Song song2 = new Song(2, "SongA", "Artist", "Pop", false);

        assertNotEquals(song1.getId(), song2.getId());
    }

    @Test
    void testToStringContainsID() {
        Song song = new Song(10, "Stay", "Artist", "Pop", false);

        String result = song.toString();

        assertTrue(result.contains("10"));
        assertTrue(result.contains("Stay"));
    }
}