package application;
	
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
	 	private static final int WORK_TIME = 25 * 60;
	    private static final int BREAK_TIME = 5 * 60; 

	    private int timeRemaining = WORK_TIME; 
	    private Timeline timeline;
	    
    @Override
    public void start(Stage primaryStage) {
        
    	VBox root = new VBox(15); 
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f4f4f4;");

        Text timerText = new Text("25:00");
        timerText.setFont(Font.font("Arial", 50));

        Button pomodoroButton = new Button("Pomodoro");
        Button startButton = new Button("Start");
        Button breakButton = new Button("Break");
        
        pomodoroButton.setStyle("-fx-background-color: #d0c1ff; -fx-font-size: 14;");
        startButton.setStyle("-fx-background-color: #d0c1ff; -fx-font-size: 14;");
        breakButton.setStyle("-fx-background-color: #d0c1ff; -fx-font-size: 14;");
        
        startButton.setOnAction(e -> {
            if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
                return; 
            }
            
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                timeRemaining--;
                timerText.setText(formatTime(timeRemaining));
            if (timeRemaining <= 0) {
                    timeline.stop();
                    startButton.setDisable(false); 
                }
            }));
            
            timeline.setCycleCount(timeRemaining); 
            timeline.play();
            startButton.setDisable(true); 
        });
        
        breakButton.setOnAction(e -> {
            
            if (timeline != null) {
                timeline.stop();
            }

            timeRemaining = BREAK_TIME;
            timerText.setText(formatTime(timeRemaining));
            startButton.setDisable(false); 
        });
        
        pomodoroButton.setOnAction(e -> {
            if (timeline != null) {
                timeline.stop();
            }

            timeRemaining = WORK_TIME;
            timerText.setText(formatTime(timeRemaining));
            startButton.setDisable(false);
        });

        ComboBox<String> taskTypeComboBox = new ComboBox<>();
        taskTypeComboBox.getItems().addAll("Study", "Work", "Exercise");
        taskTypeComboBox.setPromptText("Task Type");

        TextField taskInput = new TextField();
        taskInput.setPromptText("Task");

        startButton.setStyle("-fx-background-color: #d0c1ff; -fx-font-size: 18;");

        root.getChildren().addAll(pomodoroButton, breakButton, timerText, taskTypeComboBox, taskInput, startButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pomodoro Timer");
        primaryStage.setResizable(false); 
        primaryStage.show();
    }
    
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
