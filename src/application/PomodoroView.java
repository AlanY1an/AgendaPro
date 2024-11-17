package application;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PomodoroView {

    public VBox getView() {
        VBox root = new VBox(15); 
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f4f4f4;");

        Text timerText = new Text("25:00");
        timerText.setFont(Font.font("Arial", 50));

        Button pomodoroButton = new Button("Pomodoro");
        Button breakButton = new Button("Break");
        pomodoroButton.setStyle("-fx-background-color: #d0c1ff; -fx-font-size: 14;");
        breakButton.setStyle("-fx-background-color: #d0c1ff; -fx-font-size: 14;");

        ComboBox<String> taskTypeComboBox = new ComboBox<>();
        taskTypeComboBox.getItems().addAll("Study", "Work", "Exercise");
        taskTypeComboBox.setPromptText("Task Type");

        TextField taskInput = new TextField();
        taskInput.setPromptText("Task");

        Button startButton = new Button("START");
        startButton.setStyle("-fx-background-color: #d0c1ff; -fx-font-size: 18;");

        root.getChildren().addAll(pomodoroButton, breakButton, timerText, taskTypeComboBox, taskInput, startButton);

        return root;
    }
}
