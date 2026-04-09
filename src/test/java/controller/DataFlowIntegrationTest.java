package controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Playlist;
import model.Shuffle;
import model.Song;
import model.SongDatabase;

class DataFlowIntegrationTest {

    private SongDatabase db;
    private PlaylistController controller;

    @BeforeEach
    void setUp() throws Exception {
        db = new SongDatabase("src/main/resources/songs.csv");
        controller = new PlaylistController(db);
    }

    @Test
    void intDF1_songDatabaseDefinitionsReachControllerGetAllSongsUse() {
        List<Song> manuallyFiltered = filterSongs(db.getAllSongs(), "Pop", false);

        Playlist result = controller.generatePlaylist("Pop", false, Playlist.NO_LIMIT);

        assertNotNull(result);
        assertEquals(manuallyFiltered.size(), result.getSongs().size());
    }

    @Test
    void intDF2_controllerFilteredSongsReachShuffleUse() {
        List<Song> filtered = filterSongs(db.getAllSongs(), "Pop", false);
        List<String> originalOrder = titlesOf(filtered.subList(0, 5));

        Playlist result = controller.generatePlaylist("Pop", false, 5);

        assertNotNull(result);
        assertNotEquals(originalOrder, titlesOf(result.getSongs()));
    }

    @Test
    void intDF3_shuffleMutatedOrderReachesPlaylistStorage() {
        List<Song> filtered = filterSongs(db.getAllSongs(), "Rock", true);
        List<Song> trimmed = new ArrayList<>(filtered.subList(0, 5));
        List<String> expectedOrder = titlesOf(new Shuffle(42).shuffle(trimmed));

        Playlist result = controller.generatePlaylist("Rock", true, 5);

        assertNotNull(result);
        assertEquals(expectedOrder, titlesOf(result.getSongs()));
    }

    @Test
    void intDF4_playlistDefinitionsReachControllerCurrentPlaylistUse() {
        Playlist result = controller.generatePlaylist("Jazz", false, 4);

        assertNotNull(result);
        assertSame(result, controller.getCurrentPlaylist());
        assertEquals(titlesOf(result.getSongs()), titlesOf(controller.getCurrentPlaylist().getSongs()));
        assertThrows(UnsupportedOperationException.class,
                () -> controller.getCurrentPlaylist().getSongs().add(new Song("Fake", "Artist", "Pop", false)));
    }

    private List<Song> filterSongs(List<Song> songs, String genre, boolean allowExplicit) {
        List<Song> filtered = new ArrayList<>();

        for (Song song : songs) {
            boolean genreMatch = genre.equalsIgnoreCase("ANY")
                    || song.getGenre().equalsIgnoreCase(genre);
            boolean explicitMatch = allowExplicit || !song.isExplicit();

            if (genreMatch && explicitMatch) {
                filtered.add(song);
            }
        }

        return filtered;
    }

    private List<String> titlesOf(List<Song> songs) {
        return songs.stream().map(Song::getTitle).toList();
    }
}
