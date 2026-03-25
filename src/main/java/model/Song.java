package model;

public class Song {
    // attributes are final to ensure they can't be changed after creation
    private final int id;
    private final String title;
    private final String artist;
    private final String genre;
    private final boolean explicit;

    public Song(String title, String artist, String genre, boolean explicit) { // constructor to initialize song attributes
        this.id = System.identityHashCode(this); // Simple ID generation
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.explicit = explicit;
    }

    public int getId() {
        return id;
    }

    // getters for song attributes
    public String getTitle() { 
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isExplicit() {
        return explicit;
    }

    @Override // helps to view song details
    public String toString() {
        return title + " - " + artist + " (" + genre + ")" +
                (explicit ? " [Explicit]" : "");
    }
}

