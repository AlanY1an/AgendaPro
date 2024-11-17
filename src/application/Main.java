package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        
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
        taskTypeComboBox.setPromptText("Task类型");

        TextField taskInput = new TextField();
        taskInput.setPromptText("Task");

        Button startButton = new Button("START");
        startButton.setStyle("-fx-background-color: #d0c1ff; -fx-font-size: 18;");

        root.getChildren().addAll(pomodoroButton, breakButton, timerText, taskTypeComboBox, taskInput, startButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pomodoro Timer");
        primaryStage.setResizable(false); 
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
