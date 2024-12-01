package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {

    private Main mainApp;

    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField passwordField;

    // Method to set the main app reference
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    // Event handler for the button click
    @FXML
    private void handleSwitch() {
        if (mainApp != null) {
            mainApp.showSecondaryView();
        }
    }
}