package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class PomodoroController {
	
	@FXML
    private Button pomodoroButton, breakButton, startButton;
    @FXML
    private TextField timerText;
    @FXML
    private ComboBox<String> taskTypeComboBox;
    @FXML
    private TextField taskInput;

    private Timeline timeline;
    private int timeRemaining;

    public void initialize() {
      
        taskTypeComboBox.getItems().addAll("Study", "Work", "Exercise");

        pomodoroButton.setOnAction(e -> switchMode(25 * 60)); 
        breakButton.setOnAction(e -> switchMode(5 * 60));     
        startButton.setOnAction(e -> startTimer());
        
        timeRemaining = parseTime(timerText.getText());
        updateTimerText();
        
        taskInput.setOnMouseClicked(e -> {
            if (taskInput.getText().equals("Task")) { 
                taskInput.clear(); // 清空内容
            }
        });
    }
    
    
    
    private int parseTime(String timeText) {
        String[] parts = timeText.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }
    
    private void switchMode(int timeInSeconds) {
        if (timeline != null) {
            timeline.stop();
        }
        timeRemaining = timeInSeconds;
        updateTimerText();
        startButton.setDisable(false);
    }

    private void startTimer() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return;
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (timeRemaining > 0) { 
                timeRemaining--;
                updateTimerText();
            } else {
                timeline.stop(); 
                startButton.setDisable(false); 
            }
        }));
        timeline.setCycleCount(timeRemaining);
        timeline.play();
        startButton.setDisable(true); 
    }

    private void updateTimerText() {
        if (timeRemaining < 0) {
            timeRemaining = 0; 
        }
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

}
