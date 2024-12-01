package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MeditationView {

    private AnchorPane root;

    public MeditationView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Meditation.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML file for MeditationView.");
        }
    }

    public AnchorPane getView() {
        return root;
    }
}
