package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import model.Event;
import view.MeditationView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class PomodoroController {

    @FXML
    private Button pomodoroButton, breakButton, startButton;
    @FXML
    private TextField timerText, taskInput;
    @FXML
    private ComboBox<String> taskTypeComboBox;

    private Timeline timeline;
    private int timeRemaining;
    private EventController eventController = new EventController(); 
    
    @FXML
    private void goToMeditation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Meditation.fxml"));
            Parent meditationRoot = loader.load();

            // 获取 MeditationController（如果需要与其交互）
            MeditationController meditationController = loader.getController();

            // 设置新场景
            Scene meditationScene = new Scene(meditationRoot);
            Stage currentStage = (Stage) startButton.getScene().getWindow();
            currentStage.setTitle("Meditation");
            currentStage.setScene(meditationScene);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Meditation view.");
        }
    }

    public void initialize() {
    	
    	taskTypeComboBox.getItems().addAll(
    		    Arrays.stream(Event.CATEGORIES) 
    		          .filter(category -> !category.equals("Entertainment")) 
    		          .toList() 
    		);

        pomodoroButton.setOnAction(e -> switchMode(25 * 60)); 
        breakButton.setOnAction(e -> switchMode(5 * 60));     
        startButton.setOnAction(e -> startTimer());

        taskInput.setOnMouseClicked(e -> {
            if (taskInput.getText().equals("Task")) {
                taskInput.clear();
            }
        });

        timeRemaining = parseTime(timerText.getText());
        updateTimerText();
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

                String category = taskTypeComboBox.getValue();
                String title = taskInput.getText().trim();
                if (category != null && !category.isEmpty() && !title.isEmpty()) {
                    LocalDate today = LocalDate.now();
                    int duration = parseTime(timerText.getText());
                    Event newEvent = new Event(
                        eventController.getAllEvents().size() + 1,
                        title,
                        category,
                        "Focus session",
                        today,
                        duration,
                        0
                    );
                    eventController.addEvent(newEvent);
                    System.out.println("Event Added: " + newEvent);
                }
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

    public void setEventController(EventController eventController) { 
        this.eventController = eventController; 
    }
    
}