package com.example.playlistgenerator;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/songs.csv";

        try {
            SongDatabase database = new SongDatabase(filePath);

            System.out.println("Songs loaded: " + database.size());
            System.out.println("----------------------------------");

            for (Song song : database.getAllSongs()) {
                System.out.println(song);
            }

        } catch (Exception e) {
            System.out.println("Failed to load songs.");
            e.printStackTrace();
        }
    }
}