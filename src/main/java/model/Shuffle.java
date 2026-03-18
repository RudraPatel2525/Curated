import java.util.*;

public class Shuffle {

    private final Random random; // random object to shuffle songs based on seed

    public Shuffle(long seed) { 
        this.random = new Random(seed);
    }

    public List<Song> shuffle(List<Song> playlist) { // method to shuffle the playlist
        Collections.shuffle(playlist, random);
        return playlist;
    }
}