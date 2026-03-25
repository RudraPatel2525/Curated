import controller.PlaylistController;
import model.SongDatabase;
import view.CLIview;

public class App {
    public static void main(String[] args) {

        try {
            // load songs from csv
            SongDatabase db = new SongDatabase("songs.csv");

            // Create controller (handles business logic)
            PlaylistController controller = new PlaylistController(db);
            // Start CLI view
            CLIview view = new CLIview(controller);
            view.start();

        } catch (Exception e) {
            System.out.println("Failed to load songs.");
            e.printStackTrace();
        }
    }
}