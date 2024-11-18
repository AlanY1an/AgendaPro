package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PomodoroView {

    private AnchorPane root;

    public PomodoroView() {
        try {
        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pomodoro.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML file for PomodoroView.");
        }
    }

    public AnchorPane getView() {
        return root;
    }
}
