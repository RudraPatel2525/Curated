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
        System.out.println("🎵 Curated Playlist Generator 🎵");

        boolean running = true;

        while (running) {
            System.out.println("\n1. Generate Playlist");
            System.out.println("2. View Playlist");
            System.out.println("3. Reset");
            System.out.println("0. Exit");

            int choice = getInt("Choose: ");

            switch (choice) {
                case 1 -> generate();
                case 2 -> view();
                case 3 -> controller.reset();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void generate() {
        System.out.print("Genre (or ANY): ");
        String genre = scanner.nextLine().trim();
        if (genre.isEmpty()) genre = Playlist.ANY;

        System.out.print("Allow explicit (yes/no): ");
        boolean explicit = scanner.nextLine().equalsIgnoreCase("yes");

        int max = getInt("Max songs (0 = no limit): ");
        if (max <= 0) max = Playlist.NO_LIMIT;

        Playlist p = controller.generatePlaylist(genre, explicit, max);

        if (p == null) {
            System.out.println("No songs found.");
        } else {
            System.out.println(p.toDisplayString());
        }
    }

    private void view() {
        if (!controller.hasPlaylist()) {
            System.out.println("No playlist yet.");
            return;
        }
        System.out.println(controller.getCurrentPlaylist().toDisplayString());
    }

    private int getInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid number.");
            }
        }
    }
}