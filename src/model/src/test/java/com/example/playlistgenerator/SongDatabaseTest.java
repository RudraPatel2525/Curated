package com.example.playlistgenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SongDatabaseTest {

    @Test
    void loadsValidSongsFromCsv() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "id,title,artist,genre,mood,explicit,minAge,durationSeconds\n" +
                "1,Blinding Lights,The Weeknd,Pop,Happy,false,0,200\n" +
                "2,Heat Waves,Glass Animals,Indie,Calm,false,0,238\n");

        SongDatabase db = new SongDatabase(tempFile.toString());

        assertEquals(2, db.size());

        List<Song> songs = db.getAllSongs();
        assertEquals("Blinding Lights", songs.get(0).getTitle());
        assertEquals("The Weeknd", songs.get(0).getArtist());
        assertEquals("Pop", songs.get(0).getGenre());
        assertEquals("Happy", songs.get(0).getMood());
        assertFalse(songs.get(0).isExplicit());
        assertEquals(0, songs.get(0).getMinAge());
        assertEquals(200, songs.get(0).getDurationSeconds());
    }

    @Test
    void skipsHeaderRow() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "id,title,artist,genre,mood,explicit,minAge,durationSeconds\n" +
                "1,Lofi Rain,Study Beats,LoFi,Calm,false,0,240\n");

        SongDatabase db = new SongDatabase(tempFile.toString());

        assertEquals(1, db.size());
        assertEquals("Lofi Rain", db.getAllSongs().get(0).getTitle());
    }

    @Test
    void skipsBlankLines() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "id,title,artist,genre,mood,explicit,minAge,durationSeconds\n" +
                "\n" +
                "1,Lofi Rain,Study Beats,LoFi,Calm,false,0,240\n" +
                "\n" +
                "2,Metal Storm,Iron Unit,Metal,Angry,true,18,210\n");

        SongDatabase db = new SongDatabase(tempFile.toString());

        assertEquals(2, db.size());
    }

    @Test
    void throwsExceptionForWrongNumberOfFields() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "id,title,artist,genre,mood,explicit,minAge,durationSeconds\n" +
                "1,Bad Row,Artist,Pop,Happy,false,0\n");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SongDatabase(tempFile.toString())
        );

        assertTrue(exception.getMessage().contains("Expected 8 fields"));
    }

    @Test
    void throwsExceptionForInvalidNumberFormat() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "id,title,artist,genre,mood,explicit,minAge,durationSeconds\n" +
                "abc,Song,Artist,Pop,Happy,false,0,200\n");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SongDatabase(tempFile.toString())
        );

        assertTrue(exception.getMessage().contains("Invalid number format"));
    }

    @Test
    void throwsExceptionWhenFileDoesNotExist() {
        assertThrows(IOException.class, () -> new SongDatabase("no_such_file.csv"));
    }

    @Test
    void returnedSongListCannotBeModified() throws IOException {
        Path tempFile = Files.createTempFile("songs", ".csv");

        Files.writeString(tempFile,
                "id,title,artist,genre,mood,explicit,minAge,durationSeconds\n" +
                "1,Blinding Lights,The Weeknd,Pop,Happy,false,0,200\n");

        SongDatabase db = new SongDatabase(tempFile.toString());

        assertThrows(UnsupportedOperationException.class, () -> {
            db.getAllSongs().add(
                    new Song(2, "Fake Song", "Fake Artist", "Rock", "Energetic", false, 0, 180)
            );
        });
    }
}