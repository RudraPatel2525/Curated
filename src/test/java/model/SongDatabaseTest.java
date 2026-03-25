package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 * SongDatabaseTest.java
 *
 * Tests the SongDatabase class.
 *
 * IMPORTANT:
 * - Matches UPDATED Song class (ONLY 4 fields)
 * - CSV format: title,artist,genre,explicit
 */
class SongDatabaseTest {

    /**
     * Test loading valid songs from CSV.
     */
    @Test
    void loadsValidSongsFromCsv() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "title,artist,genre,explicit\n" +
                "Blinding Lights,The Weeknd,Pop,false\n" +
                "Heat Waves,Glass Animals,Indie,false\n"
        );

        SongDatabase db = new SongDatabase(tempFile.toString());

        assertEquals(2, db.size());

        List<Song> songs = db.getAllSongs();

        // Validate parsed data
        assertEquals("Blinding Lights", songs.get(0).getTitle());
        assertEquals("The Weeknd", songs.get(0).getArtist());
        assertEquals("Pop", songs.get(0).getGenre());
        assertFalse(songs.get(0).isExplicit());
    }

    /**
     * Test header row is skipped.
     */
    @Test
    void skipsHeaderRow() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "title,artist,genre,explicit\n" +
                "Lofi Rain,Study Beats,LoFi,false\n"
        );

        SongDatabase db = new SongDatabase(tempFile.toString());

        assertEquals(1, db.size());
    }

    // Test blank lines are ignored.
    @Test
    void skipsBlankLines() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "title,artist,genre,explicit\n" +
                "\n" +
                "Song1,Artist1,Pop,false\n" +
                "\n" +
                "Song2,Artist2,Rock,true\n"
        );

        SongDatabase db = new SongDatabase(tempFile.toString());

        assertEquals(2, db.size());
    }

    // Test invalid CSV format (wrong number of fields).
    @Test
    void throwsExceptionForWrongNumberOfFields() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "title,artist,genre,explicit\n" +
                "Bad Row,Artist,Pop\n" // only 3 fields
        );

        assertThrows(IllegalArgumentException.class,
                () -> new SongDatabase(tempFile.toString()));
    }

    // Test file not found case.
    @Test
    void throwsExceptionWhenFileDoesNotExist() {
        assertThrows(IOException.class,
                () -> new SongDatabase("no_such_file.csv"));
    }

    // Test returned list is immutable.
    @Test
    void returnedSongListCannotBeModified() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "title,artist,genre,explicit\n" +
                "Song,Artist,Pop,false\n"
        );

        SongDatabase db = new SongDatabase(tempFile.toString());

        assertThrows(UnsupportedOperationException.class, () -> {
            db.getAllSongs().add(
                    new Song("Fake", "Fake", "Rock", false)
            );
        });
    }
}