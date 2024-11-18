package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DemoPage {
	
	private Scene scene;
    private Main mainApp;

    public DemoPage(Main mainApp) {
        this.mainApp = mainApp;
        initialize();
    }
    
    private void initialize() {
    	try {
            // Load the FXML file for Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Todo.fxml"));
            Parent root = loader.load();

            // Get the controller and set the main application reference
//            TodoController controller = loader.getController();
//            controller.setMainApp(mainApp);

            // Set the scene for the Login view
            scene = new Scene(root, 600, 400);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Scene getScene() {
        return scene;
    }
}