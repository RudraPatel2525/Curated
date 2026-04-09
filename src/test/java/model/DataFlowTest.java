package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class DataFlowTest {

    @Test
    void tcS1_constructorDefinitionsReachGetterUses() {
        Song song = new Song("HUMBLE", "Kendrick Lamar", "HipHop", true);

        assertEquals("HUMBLE", song.getTitle());
        assertEquals("Kendrick Lamar", song.getArtist());
        assertEquals("HipHop", song.getGenre());
        assertTrue(song.isExplicit());
    }

    @Test
    void tcS2_constructorDefinitionsReachToStringUses() {
        Song song = new Song("Stay", "Artist", "Pop", false);

        String output = song.toString();

        assertTrue(output.contains("Stay"));
        assertTrue(output.contains("Artist"));
        assertTrue(output.contains("Pop"));
        assertFalse(output.contains("[Explicit]"));
    }

    @Test
    void tcDB1_addedSongsReachGetAllSongs() throws IOException {
        SongDatabase db = new SongDatabase(writeCsv(
                "title,artist,genre,explicit",
                "Blinding Lights,The Weeknd,Pop,false",
                "Heat Waves,Glass Animals,Indie,false"
        ).toString());

        List<Song> songs = db.getAllSongs();

        assertEquals(2, songs.size());
        assertEquals("Blinding Lights", songs.get(0).getTitle());
        assertEquals("Heat Waves", songs.get(1).getTitle());
    }

    @Test
    void tcDB2_addedSongsReachSize() throws IOException {
        SongDatabase db = new SongDatabase(writeCsv(
                "title,artist,genre,explicit",
                "Song1,Artist1,Pop,false",
                "Song2,Artist2,Rock,true"
        ).toString());

        assertEquals(2, db.size());
    }

    @Test
    void tcDB3_emptyDefinitionAndBlankLinesReachGetAllSongs() throws IOException {
        SongDatabase db = new SongDatabase(writeCsv(
                "title,artist,genre,explicit",
                "",
                "",
                "  "
        ).toString());

        assertTrue(db.getAllSongs().isEmpty());
    }

    @Test
    void tcDB4_getAllSongsReturnsUnmodifiableView() throws IOException {
        SongDatabase db = new SongDatabase(writeCsv(
                "title,artist,genre,explicit",
                "Song,Artist,Pop,false"
        ).toString());

        assertThrows(UnsupportedOperationException.class,
                () -> db.getAllSongs().add(new Song("Fake", "Fake", "Rock", false)));
    }

    @Test
    void tcSH1_callerDefinitionReachesShuffleParameterAndReturn() {
        List<Song> songs = new ArrayList<>(List.of(
                new Song("A", "Artist", "Pop", false),
                new Song("B", "Artist", "Pop", false),
                new Song("C", "Artist", "Pop", false)
        ));

        Shuffle shuffle = new Shuffle(42);
        List<Song> result = shuffle.shuffle(songs);

        assertSame(songs, result);
        assertEquals(3, result.size());
    }

    @Test
    void tcSH2_mutatedOrderDefinitionReachesReturn() {
        List<Song> original = List.of(
                new Song("A", "Artist", "Pop", false),
                new Song("B", "Artist", "Pop", false),
                new Song("C", "Artist", "Pop", false)
        );
        List<Song> shuffledSongs = new ArrayList<>(original);

        Shuffle shuffle = new Shuffle(42);
        List<Song> result = shuffle.shuffle(shuffledSongs);

        assertNotEquals(original, result);
        assertIterableEquals(original.stream().map(Song::getTitle).sorted().toList(),
                result.stream().map(Song::getTitle).sorted().toList());
    }

    @Test
    void tcSH3_emptyListReachesReturnWithoutMutation() {
        List<Song> songs = new ArrayList<>();

        Shuffle shuffle = new Shuffle(42);
        List<Song> result = shuffle.shuffle(songs);

        assertSame(songs, result);
        assertTrue(result.isEmpty());
    }

    private Path writeCsv(String... lines) throws IOException {
        Path file = Files.createTempFile("songs-data-flow", ".csv");
        Files.write(file, List.of(lines));
        return file;
    }
}
