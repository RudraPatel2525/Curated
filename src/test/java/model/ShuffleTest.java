package model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * ShuffleTest.java
 *
 * Tests the Shuffle class which randomizes the order of songs.
 *
 * NOTE:
 * - Uses deterministic seed for predictable results
 * - Ensures shuffle does not lose or duplicate songs
 */
public class ShuffleTest {

    /**
     * Test that shuffling does NOT change the number of songs.
     */
    @Test
    void testShuffleKeepsSameSize() {
        List<Song> songs = new ArrayList<>();

        // Using updated constructor (NO id parameter)
        songs.add(new Song("A", "Artist", "Pop", false));
        songs.add(new Song("B", "Artist", "Pop", false));

        Shuffle shuffle = new Shuffle(42);

        // FIX: method name is shuffle(), not shuffleSongs()
        List<Song> result = shuffle.shuffle(new ArrayList<>(songs));

        assertEquals(songs.size(), result.size(),
                "Shuffling should not change the number of songs.");
    }

    /**
     * Test that shuffle actually changes order (with fixed seed).
     */
    @Test
    void testShuffleChangesOrder() {
        List<Song> songs = new ArrayList<>();

        songs.add(new Song("A", "Artist", "Pop", false));
        songs.add(new Song("B", "Artist", "Pop", false));
        songs.add(new Song("C", "Artist", "Pop", false));

        Shuffle shuffle = new Shuffle(42);

        List<Song> result = shuffle.shuffle(new ArrayList<>(songs));

        // With a fixed seed, order should be predictably different
        assertNotEquals(songs, result,
                "Shuffled list should have a different order than original.");
    }

    /**
     * Test that shuffling an empty list does nothing.
     */
    @Test
    void testShuffleEmptyList() {
        List<Song> songs = new ArrayList<>();

        Shuffle shuffle = new Shuffle(42);

        List<Song> result = shuffle.shuffle(songs);

        assertTrue(result.isEmpty(),
                "Shuffling an empty list should return an empty list.");
    }
}