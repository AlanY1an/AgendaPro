package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Event;
import java.time.LocalDate;

public class AddEventDialogController {

    @FXML
    private TextField titleField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private EventController eventController;

//    private EventAddedCallback callback;
//
//    public void setCallback(EventAddedCallback callback) {
////    	Take that lambda and create anonymous object
//        this.callback = callback; 
//    }
    
    @FXML
    public void initialize() {
        categoryComboBox.getItems().addAll(Event.CATEGORIES);
        categoryComboBox.setValue(Event.CATEGORIES[0]); 
    }

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    @FXML
    private void handleSave() {
        String title = titleField.getText().trim();
        LocalDate date = datePicker.getValue();
        String description = descriptionField.getText().trim();
        String category = categoryComboBox.getValue();

        if (title.isEmpty() || date == null || category == null) {
            showErrorAlert("Input Error", "Please fill out all fields.");
            return;
        }

        Event newEvent = new Event(
            eventController.getAllEvents().size() + 1,
            title,
            category,
            description,
            date
        );

        eventController.addEvent(newEvent);

        System.out.println("Event added: " + newEvent);
        

        closeWindow();
    }

    @FXML
    private void handleCancel() {
    	System.out.println("Cancel button clicked");
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
