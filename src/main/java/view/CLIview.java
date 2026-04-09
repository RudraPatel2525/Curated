package view;

import java.util.Scanner;

import controller.PlaylistController;
import model.Playlist;

public class CLIview {

    private final PlaylistController controller;
    private final Scanner scanner;

    public CLIview(PlaylistController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Curated Playlist Generator ===");

        boolean running = true;

        while (running) {
            System.out.println("\n1. Generate Playlist");
            System.out.println("2. View Playlist");
            System.out.println("3. Reset Playlist");
            System.out.println("0. Exit");

            int choice = getInt("Choose option: ");

            switch (choice) {
                case 1 -> generate();
                case 2 -> view();
                case 3 -> {
                    controller.reset();
                    System.out.println("Playlist reset.");
                }
                case 0 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void generate() {
        System.out.print("Genre (EDM, Pop, Hip-Hop, Rock, Jazz): ");
        String genre = scanner.nextLine().trim();
        if (genre.isEmpty()) genre = Playlist.ANY;

        System.out.print("Allow explicit? (yes/no): ");
        boolean allowExplicit = scanner.nextLine().equalsIgnoreCase("yes");

        int maxSongs = getInt("Max songs (0 = no limit): ");
        if (maxSongs <= 0) maxSongs = Playlist.NO_LIMIT;

        Playlist playlist = controller.generatePlaylist(genre, allowExplicit, maxSongs);

        if (playlist == null || playlist.isEmpty()) {
            System.out.println("No songs found matching your criteria.");
        } else {
            System.out.println(playlist.toDisplayString());
        }
    }

    private void view() {
        if (!controller.hasPlaylist()) {
            System.out.println("No playlist generated yet.");
            return;
        }

        System.out.println(controller.getCurrentPlaylist().toDisplayString());
    }

    private int getInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}