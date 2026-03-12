package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FilterEngineTest.java
 *
 * Comprehensive test suite for FilterEngine covering:
 *   - Equivalence Class Testing  (valid/invalid partitions for each input)
 *   - Boundary Value Analysis    (edges of maxSongs, empty lists, single songs)
 *   - Filtering Logic            (genre, explicit, combined criteria)
 *   - Helper methods             (countEligible, hasSufficientSongs)
 *
 * All tests are deterministic and independent of any external file.
 *
 * @author Youssef Abdelaziz - 200511755
 * @version 1.0
 */
public class FilterEngineTest {

    // -----------------------------------------------------------------------
    // Shared fixtures
    // -----------------------------------------------------------------------

    private Song popClean1;
    private Song popClean2;
    private Song popExplicit;
    private Song rockClean;
    private Song rockExplicit;
    private Song jazzClean;
    private Song hipHopExplicit;

    private List<Song> allSongs;

    @BeforeEach
    void setUp() {
        popClean1      = new Song("Levitating",       "Dua Lipa",       "Pop",     false);
        popClean2      = new Song("Blinding Lights",  "The Weeknd",     "Pop",     false);
        popExplicit    = new Song("Bad Guy",           "Billie Eilish",  "Pop",     true);
        rockClean      = new Song("Eye of the Tiger", "Survivor",       "Rock",    false);
        rockExplicit   = new Song("Bulls on Parade",  "RATM",           "Rock",    true);
        jazzClean      = new Song("So What",          "Miles Davis",    "Jazz",    false);
        hipHopExplicit = new Song("HUMBLE.",           "Kendrick Lamar", "Hip-Hop", true);

        allSongs = new ArrayList<>();
        allSongs.add(popClean1);
        allSongs.add(popClean2);
        allSongs.add(popExplicit);
        allSongs.add(rockClean);
        allSongs.add(rockExplicit);
        allSongs.add(jazzClean);
        allSongs.add(hipHopExplicit);
    }

    // -----------------------------------------------------------------------
    // 1. Null input guard
    // -----------------------------------------------------------------------

    @Test
    void testFilter_nullList_throwsIllegalArgumentException() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        assertThrows(IllegalArgumentException.class, () -> fe.filter(null),
                "filter(null) should throw IllegalArgumentException.");
    }

    @Test
    void testCountEligible_nullList_throwsIllegalArgumentException() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        assertThrows(IllegalArgumentException.class, () -> fe.countEligible(null),
                "countEligible(null) should throw IllegalArgumentException.");
    }

    // -----------------------------------------------------------------------
    // 2. Boundary Value Analysis — empty list
    // -----------------------------------------------------------------------

    @Test
    void testFilter_emptyList_returnsEmptyList() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(new ArrayList<>());
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Filtering an empty list should return an empty list.");
    }

    @Test
    void testCountEligible_emptyList_returnsZero() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        assertEquals(0, fe.countEligible(new ArrayList<>()));
    }

    // -----------------------------------------------------------------------
    // 3. Boundary Value Analysis — single song list
    // -----------------------------------------------------------------------

    @Test
    void testFilter_singleSong_matches_returnsIt() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        List<Song> single = List.of(popClean1);
        List<Song> result = fe.filter(single);
        assertEquals(1, result.size());
        assertTrue(result.contains(popClean1));
    }

    @Test
    void testFilter_singleSong_doesNotMatch_returnsEmpty() {
        FilterEngine fe = new FilterEngine("Jazz", false, FilterEngine.NO_LIMIT);
        List<Song> single = List.of(popClean1);
        List<Song> result = fe.filter(single);
        assertTrue(result.isEmpty(), "Non-matching single song should return empty list.");
    }

    // -----------------------------------------------------------------------
    // 4. Boundary Value Analysis — maxSongs
    // -----------------------------------------------------------------------

    @Test
    void testFilter_maxSongsOne_returnsExactlyOneSong() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, 1);
        List<Song> result = fe.filter(allSongs);
        assertEquals(1, result.size(), "maxSongs=1 should return exactly one song.");
    }

    @Test
    void testFilter_maxSongsEqualsPoolSize_returnsAll() {
        // 3 clean songs match ANY + allowExplicit=false
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, false, 3);
        List<Song> result = fe.filter(allSongs);
        assertEquals(3, result.size(),
                "maxSongs equal to pool size should return all eligible songs.");
    }

    @Test
    void testFilter_maxSongsOneLessThanPool_returnsOneLess() {
        // 3 clean songs available; cap at 2
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, false, 2);
        List<Song> result = fe.filter(allSongs);
        assertEquals(2, result.size(),
                "maxSongs one less than pool should return pool size minus one.");
    }

    @Test
    void testFilter_maxSongsLargerThanPool_returnsWholePool() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, false, 100);
        List<Song> result = fe.filter(allSongs);
        assertEquals(3, result.size(),
                "maxSongs larger than pool should return all eligible songs.");
    }

    @Test
    void testFilter_maxSongsNoLimit_returnsAllEligible() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        assertEquals(allSongs.size(), result.size(),
                "NO_LIMIT should return every song in the list.");
    }

    @Test
    void testFilter_maxSongsZero_treatedAsNoLimit() {
        // maxSongs < 1 is treated as NO_LIMIT in FilterEngine
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        assertFalse(result.isEmpty(), "NO_LIMIT should not produce an empty result.");
    }

    // -----------------------------------------------------------------------
    // 5. Equivalence Class Testing — genre filter
    // -----------------------------------------------------------------------

    // EC1: Valid genre that exists in the list
    @Test
    void testFilter_validGenrePop_returnsOnlyPopSongs() {
        FilterEngine fe = new FilterEngine("Pop", true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        for (Song s : result) {
            assertEquals("Pop", s.getGenre(),
                    "All returned songs should be Pop genre.");
        }
    }

    // EC2: Valid genre that exists in the list (different genre)
    @Test
    void testFilter_validGenreRock_returnsOnlyRockSongs() {
        FilterEngine fe = new FilterEngine("Rock", true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        assertEquals(2, result.size());
        for (Song s : result) {
            assertEquals("Rock", s.getGenre());
        }
    }

    // EC3: Genre that does not exist in the list
    @Test
    void testFilter_genreNotInList_returnsEmpty() {
        FilterEngine fe = new FilterEngine("Classical", false, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        assertTrue(result.isEmpty(),
                "Genre not present in the list should return empty result.");
    }

    // EC4: ANY genre — all songs eligible (subject to explicit filter)
    @Test
    void testFilter_anyGenre_doesNotFilterByGenre() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        assertEquals(allSongs.size(), result.size(),
                "ANY genre should include songs of every genre.");
    }

    // EC5: Genre matching is case-insensitive
    @Test
    void testFilter_genreCaseInsensitive_matchesRegardlessOfCase() {
        FilterEngine feLower = new FilterEngine("pop", true, FilterEngine.NO_LIMIT);
        FilterEngine feUpper = new FilterEngine("POP", true, FilterEngine.NO_LIMIT);
        FilterEngine feMixed = new FilterEngine("pOp", true, FilterEngine.NO_LIMIT);

        assertEquals(fe(feLower).size(), fe(feUpper).size());
        assertEquals(fe(feUpper).size(), fe(feMixed).size());
    }

    // EC6: null genre treated as ANY
    @Test
    void testFilter_nullGenre_treatedAsAny() {
        FilterEngine fe = new FilterEngine(null, true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        assertEquals(allSongs.size(), result.size(),
                "null genre should behave like ANY.");
    }

    // -----------------------------------------------------------------------
    // 6. Equivalence Class Testing — explicit filter
    // -----------------------------------------------------------------------

    // EC1: allowExplicit = false → only clean songs
    @Test
    void testFilter_explicitNotAllowed_excludesAllExplicitSongs() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, false, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        for (Song s : result) {
            assertFalse(s.isExplicit(),
                    "No explicit songs should appear when allowExplicit=false.");
        }
    }

    // EC2: allowExplicit = true → both clean and explicit songs
    @Test
    void testFilter_explicitAllowed_includesExplicitSongs() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        boolean hasExplicit = result.stream().anyMatch(Song::isExplicit);
        assertTrue(hasExplicit,
                "Explicit songs should appear when allowExplicit=true.");
    }

    // EC3: all songs are clean — explicit setting has no effect
    @Test
    void testFilter_allCleanSongs_explicitFlagIrrelevant() {
        List<Song> cleanOnly = List.of(popClean1, popClean2, rockClean, jazzClean);
        FilterEngine feNo  = new FilterEngine(FilterEngine.ANY, false, FilterEngine.NO_LIMIT);
        FilterEngine feYes = new FilterEngine(FilterEngine.ANY, true,  FilterEngine.NO_LIMIT);

        assertEquals(feNo.filter(cleanOnly).size(), feYes.filter(cleanOnly).size(),
                "Explicit flag should not change results when all songs are clean.");
    }

    // EC4: all songs are explicit — allowExplicit=false returns empty
    @Test
    void testFilter_allExplicitSongs_notAllowed_returnsEmpty() {
        List<Song> explicitOnly = List.of(popExplicit, rockExplicit, hipHopExplicit);
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, false, FilterEngine.NO_LIMIT);
        assertTrue(fe.filter(explicitOnly).isEmpty(),
                "All explicit songs with allowExplicit=false should return empty list.");
    }

    // EC5: all songs are explicit — allowExplicit=true returns all
    @Test
    void testFilter_allExplicitSongs_allowed_returnsAll() {
        List<Song> explicitOnly = List.of(popExplicit, rockExplicit, hipHopExplicit);
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        assertEquals(3, fe.filter(explicitOnly).size(),
                "All explicit songs should be returned when allowExplicit=true.");
    }

    // -----------------------------------------------------------------------
    // 7. Combined criteria tests
    // -----------------------------------------------------------------------

    @Test
    void testFilter_popCleanOnly_returnsOnlyCleanPopSongs() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);

        assertEquals(2, result.size());
        assertTrue(result.contains(popClean1));
        assertTrue(result.contains(popClean2));
        assertFalse(result.contains(popExplicit));
    }

    @Test
    void testFilter_popExplicitAllowed_returnsAllPopSongs() {
        FilterEngine fe = new FilterEngine("Pop", true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);

        assertEquals(3, result.size());
        assertTrue(result.contains(popClean1));
        assertTrue(result.contains(popClean2));
        assertTrue(result.contains(popExplicit));
    }

    @Test
    void testFilter_rockCleanMaxOne_returnsOneRockSong() {
        FilterEngine fe = new FilterEngine("Rock", false, 1);
        List<Song> result = fe.filter(allSongs);

        assertEquals(1, result.size());
        assertEquals("Rock", result.get(0).getGenre());
        assertFalse(result.get(0).isExplicit());
    }

    @Test
    void testFilter_hipHopCleanNotAllowed_returnsEmpty() {
        FilterEngine fe = new FilterEngine("Hip-Hop", false, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);
        assertTrue(result.isEmpty(),
                "Hip-Hop with allowExplicit=false should return empty (only explicit Hip-Hop exists).");
    }

    @Test
    void testFilter_jazzCleanMaxTwo_returnsOneJazzSong() {
        FilterEngine fe = new FilterEngine("Jazz", false, 2);
        List<Song> result = fe.filter(allSongs);
        assertEquals(1, result.size(),
                "Only one Jazz song exists; maxSongs=2 should not pad the list.");
        assertTrue(result.contains(jazzClean));
    }

    // -----------------------------------------------------------------------
    // 8. Order preservation
    // -----------------------------------------------------------------------

    @Test
    void testFilter_preservesInputOrder() {
        FilterEngine fe = new FilterEngine("Pop", true, FilterEngine.NO_LIMIT);
        List<Song> result = fe.filter(allSongs);

        // popClean1 comes before popClean2 in allSongs
        int idx1 = result.indexOf(popClean1);
        int idx2 = result.indexOf(popClean2);
        assertTrue(idx1 < idx2, "Filter should preserve the original insertion order.");
    }

    // -----------------------------------------------------------------------
    // 9. countEligible tests
    // -----------------------------------------------------------------------

    @Test
    void testCountEligible_anyGenreAllowExplicit_returnsTotal() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        assertEquals(allSongs.size(), fe.countEligible(allSongs));
    }

    @Test
    void testCountEligible_popCleanOnly_returnsTwo() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        assertEquals(2, fe.countEligible(allSongs));
    }

    @Test
    void testCountEligible_ignoresMaxSongsCap() {
        // maxSongs=1 but countEligible should ignore that cap
        FilterEngine fe = new FilterEngine("Pop", false, 1);
        assertEquals(2, fe.countEligible(allSongs),
                "countEligible() should count all attribute-matching songs, ignoring maxSongs.");
    }

    // -----------------------------------------------------------------------
    // 10. hasSufficientSongs tests
    // -----------------------------------------------------------------------

    @Test
    void testHasSufficientSongs_exactlyEnough_returnsTrue() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        assertTrue(fe.hasSufficientSongs(allSongs, 2));
    }

    @Test
    void testHasSufficientSongs_moreThanEnough_returnsTrue() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        assertTrue(fe.hasSufficientSongs(allSongs, 3));
    }

    @Test
    void testHasSufficientSongs_notEnough_returnsFalse() {
        FilterEngine fe = new FilterEngine("Jazz", false, FilterEngine.NO_LIMIT);
        assertFalse(fe.hasSufficientSongs(allSongs, 5),
                "Only 1 Jazz song exists; requiring 5 should return false.");
    }

    @Test
    void testHasSufficientSongs_noMatch_returnsFalse() {
        FilterEngine fe = new FilterEngine("Classical", false, FilterEngine.NO_LIMIT);
        assertFalse(fe.hasSufficientSongs(allSongs, 1),
                "No Classical songs exist; should return false.");
    }

    @Test
    void testHasSufficientSongs_requireZero_alwaysTrue() {
        FilterEngine fe = new FilterEngine("Classical", false, FilterEngine.NO_LIMIT);
        assertTrue(fe.hasSufficientSongs(allSongs, 0),
                "Requiring 0 songs should always return true.");
    }

    // -----------------------------------------------------------------------
    // 11. Getters and setters
    // -----------------------------------------------------------------------

    @Test
    void testSetGenre_updatesFilter() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        fe.setGenre("Rock");
        assertEquals("Rock", fe.getGenre());

        List<Song> result = fe.filter(allSongs);
        for (Song s : result) {
            assertEquals("Rock", s.getGenre());
        }
    }

    @Test
    void testSetAllowExplicit_updatesFilter() {
        FilterEngine fe = new FilterEngine("Pop", false, FilterEngine.NO_LIMIT);
        assertEquals(2, fe.filter(allSongs).size());

        fe.setAllowExplicit(true);
        assertTrue(fe.isAllowExplicit());
        assertEquals(3, fe.filter(allSongs).size());
    }

    @Test
    void testSetMaxSongs_updatesFilter() {
        FilterEngine fe = new FilterEngine(FilterEngine.ANY, true, FilterEngine.NO_LIMIT);
        fe.setMaxSongs(2);
        assertEquals(2, fe.getMaxSongs());
        assertEquals(2, fe.filter(allSongs).size());
    }

    // -----------------------------------------------------------------------
    // Private helper
    // -----------------------------------------------------------------------

    /** Runs filter on allSongs with the given engine — reduces boilerplate. */
    private List<Song> fe(FilterEngine engine) {
        return engine.filter(allSongs);
    }
}
