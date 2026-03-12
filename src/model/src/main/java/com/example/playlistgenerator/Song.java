package src.main.java.com.example.playlistgenerator;

public class Song {
    private final int id;
    private final String title;
    private final String artist;
    private final String genre;
    private final String mood;
    private final boolean explicit;
    private final int minAge;
    private final int durationSeconds;

    public Song(int id, String title, String artist, String genre,
                String mood, boolean explicit, int minAge, int durationSeconds) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.mood = mood;
        this.explicit = explicit;
        this.minAge = minAge;
        this.durationSeconds = durationSeconds;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getMood() {
        return mood;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

   @Override
    public String toString() {
        return "Song{id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", mood='" + mood + '\'' +
                ", explicit=" + explicit +
                ", minAge=" + minAge +
                ", durationSeconds=" + durationSeconds +
                '}';
    }
}
