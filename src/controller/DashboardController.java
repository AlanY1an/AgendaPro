package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import view.Clock;
import java.util.Random;


public class DashboardController {

    @FXML
    private AnchorPane clockAnchorPane;
    
    @FXML
    private Label inspirationLabel;
    
    @FXML
    private Label greetingLabel;
    
    private final List<String> quotes = List.of(
            "Believe in yourself!",
            "Stay positive, work hard, make it happen.",
            "Your limitation—it's only your imagination.",
            "Push yourself, because no one else is going to do it for you.",
            "Dream it. Wish it. Do it.",
            "Success doesn’t just find you. You have to go out and get it.",
            "The harder you work for something, the greater you’ll feel when you achieve it.",
            "Don’t stop when you’re tired. Stop when you’re done."
    );
    
    @FXML
    public void initialize() {
        System.out.println("DashboardController initialized");
        // Greeting
        LocalTime now = LocalTime.now();
        String greeting = getGreeting(now);
        greetingLabel.setText(greeting);
        
        // Inspiration Label
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateInspirationText()),
                new KeyFrame(Duration.seconds(5)) // 每 5 秒更新一次
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        
        // Clock
        assert clockAnchorPane != null : "clockAnchorPane is null!";
        Clock clock = new Clock();
        VBox clockView = clock.createClock();
        // 确保 VBox 填充整个 AnchorPane
        AnchorPane.setTopAnchor(clockView, 0.0);
        AnchorPane.setBottomAnchor(clockView, 0.0);
        AnchorPane.setLeftAnchor(clockView, 0.0);
        AnchorPane.setRightAnchor(clockView, 0.0);
        clockAnchorPane.getChildren().add(clockView);
    }
    
    
    private String getGreeting(LocalTime time) {
        if (time.isBefore(LocalTime.NOON)) {
            return "Hello, Good Morning!";
        } else if (time.isBefore(LocalTime.of(18, 0))) {
            return "Hello, Good Afternoon!";
        } else {
            return "Hello, Good Evening!";
        }
    }
    
    private void updateInspirationText() {
        Random random = new Random();
        String newQuote = quotes.get(random.nextInt(quotes.size()));
        inspirationLabel.setText(newQuote);
    }
}
