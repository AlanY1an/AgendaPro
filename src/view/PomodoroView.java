package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import controller.EventController;
import controller.PomodoroController;

public class PomodoroView {

    private AnchorPane root;
    private EventController eventController;

    public PomodoroView(EventController eventController) {
        this.eventController = eventController; 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pomodoro.fxml"));
            root = loader.load();
            PomodoroController controller = loader.getController();
            controller.setEventController(eventController); 
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML file for PomodoroView.");
        }
    }

    public AnchorPane getView() {
        return root;
    }
    
    public EventController getEventController() {
        return eventController;
    }
    
}
