package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.List;

class PlaylistTest {

    private List<Song> sampleSongs() {
        return List.of(
                new Song("Blinding Lights", "The Weeknd", "Pop", false),
                new Song("Levitating", "Dua Lipa", "Pop", false),
                new Song("Paint The Town Red", "Doja Cat", "Pop", true),
                new Song("Everlong", "Foo Fighters", "Rock", false)
        );
    }

    @Test
    void createsPlaylistWithMatchingSongs() {
        Playlist playlist = new Playlist("Pop Picks", sampleSongs(), "Pop", true, Playlist.NO_LIMIT);

        assertEquals(3, playlist.getSongCount());
        assertFalse(playlist.isEmpty());
    }

    @Test
    void anyGenreIncludesAllSongs() {
        Playlist playlist = new Playlist("All Songs", sampleSongs(), Playlist.ANY, true, Playlist.NO_LIMIT);

        assertEquals(4, playlist.getSongCount());
    }

    @Test
    void explicitFilteringExcludesExplicitSongs() {
        Playlist playlist = new Playlist("Clean Mix", sampleSongs(), Playlist.ANY, false, Playlist.NO_LIMIT);

        assertEquals(3, playlist.getSongCount());
        assertTrue(playlist.getSongs().stream().noneMatch(Song::isExplicit));
    }

    @Test
    void noLimitKeepsAllMatchingSongs() {
        Playlist playlist = new Playlist("Pop Picks", sampleSongs(), "Pop", true, Playlist.NO_LIMIT);

        assertEquals(3, playlist.getSongCount());
    }

    @Test
    void positiveMaxSongsTruncatesPlaylist() {
        Playlist playlist = new Playlist("Short Mix", sampleSongs(), Playlist.ANY, true, 2);

        assertEquals(2, playlist.getSongCount());
    }

    @Test
    void isEmptyReturnsTrueWhenNoSongsMatch() {
        Playlist playlist = new Playlist("Jazz", sampleSongs(), "Jazz", false, Playlist.NO_LIMIT);

        assertTrue(playlist.isEmpty());
        assertEquals(0, playlist.getSongCount());
    }

    @Test
    void getSongCountMatchesReturnedSongs() {
        Playlist playlist = new Playlist("Pop Picks", sampleSongs(), "Pop", false, Playlist.NO_LIMIT);

        assertEquals(playlist.getSongs().size(), playlist.getSongCount());
    }

    @Test
    void toDisplayStringIncludesSongsForNonEmptyPlaylist() {
        Playlist playlist = new Playlist("Pop Picks", sampleSongs(), "Pop", false, Playlist.NO_LIMIT);

        String output = playlist.toDisplayString();

        assertTrue(output.contains("=== PLAYLIST: Pop Picks ==="));
        assertTrue(output.contains("Blinding Lights"));
        assertTrue(output.contains("Levitating"));
        assertTrue(output.contains("Total songs: 2"));
    }

    @Test
    void toDisplayStringReportsEmptyPlaylist() {
        Playlist playlist = new Playlist("Empty", sampleSongs(), "Jazz", false, Playlist.NO_LIMIT);

        String output = playlist.toDisplayString();

        assertTrue(output.contains("=== PLAYLIST: Empty ==="));
        assertTrue(output.contains("No songs found."));
    }
}
