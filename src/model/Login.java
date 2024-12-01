package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class Login {
	
	private Main mainApp;
	private Scene scene;
	
	public Login(Main mainApp) {
        this.mainApp = mainApp;
        initialize();       
    }
	
	private void initialize() {
        try {
            // Load the FXML file for Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            // Get the controller and set the main application reference
            LoginController controller = loader.getController();
            controller.setMainApp(mainApp);

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