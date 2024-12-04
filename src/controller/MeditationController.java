package controller;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.Event;

public class MeditationController {
    @FXML private Pane mainPane;  
    @FXML private Label statusLabel;
    @FXML private Label timerLabel;
    @FXML private Button tenMinButton;
    @FXML private Button thirtyMinButton;
    @FXML private Button oneHourButton;
    @FXML
    private Button resetButton;  // Add a button and set

    private long remainingSeconds;
    private int currentMeditationDuration;
    private static int meditationEventId = 1; 
    private EventController eventController;
    private Timeline breathAnimation;
    private Timeline durationTimer;
    private Group flowerPattern;
    
    private final String[] BREATH_STAGES = {
        "Breathe In Deeply",
        "Hold Your Breath",
        "Breathe Out Slowly",
        "Hold Your Breath"
    };

    private final double INITIAL_RADIUS = 45; 
    private final double MIN_RADIUS = 30;      
    private final double MAX_RADIUS = 95;
    private int currentStage = 0;
    private final int PETAL_COUNT = 12;

    private final Color LIGHT_CENTER = Color.web("#7FFFD4", 0.9);  // color of the circle
    private final Color LIGHT_EDGE = Color.web("#48D1CC", 0.7);    // 
    private final Color DARK_CENTER = Color.web("#60C4C0", 0.9);   // change #40B4B0 to #60C4C0
    private final Color DARK_EDGE = Color.web("#40B4B0", 0.7); 

    @FXML
    public void initialize() {
        setupFlowerPattern();
        setupBreathAnimation();
        setupButtons();
        resetMeditation();
        timerLabel.setText("");
    }
    
    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    private void setupFlowerPattern() {
        flowerPattern = new Group();
        
        for (int i = 0; i < PETAL_COUNT; i++) {
            double angle = (360.0 / PETAL_COUNT) * i;
        
        for (int j = 0; j < 3; j++) {
            Circle petal = new Circle(
                INITIAL_RADIUS * 0.37 * (j + 1),  // Adjusted to 0.37 to match the new initial radius
                createGradient(LIGHT_CENTER.deriveColor(0, 1, 1, 0.3),
                             LIGHT_EDGE.deriveColor(0, 1, 1, 0.2))
            );
            
            double offset = INITIAL_RADIUS * 1.1;  // Adjust to 1.1 to make the petals more appropriately distributed
            petal.setTranslateX(Math.cos(Math.toRadians(angle)) * offset);
            petal.setTranslateY(Math.sin(Math.toRadians(angle)) * offset);
            petal.setEffect(new GaussianBlur(7));  // Adjusted to 7 to match the new size
            
            flowerPattern.getChildren().add(petal);
        }
    }

        Circle mainCircle = new Circle(INITIAL_RADIUS);
        mainCircle.setFill(createGradient(LIGHT_CENTER, LIGHT_EDGE));
        mainCircle.setEffect(new GaussianBlur(3));
        flowerPattern.getChildren().add(mainCircle);

        flowerPattern.translateXProperty().bind(mainPane.widthProperty().divide(2));
           flowerPattern.translateYProperty().bind(mainPane.heightProperty().divide(2));
        

        mainPane.getChildren().add(flowerPattern);
    }

    private void setupButtons() {
     tenMinButton.setOnAction(e -> startMeditation(10, 10)); 
     thirtyMinButton.setOnAction(e -> startMeditation(30, 30));
     oneHourButton.setOnAction(e -> startMeditation(60, 60)); 
     resetButton.setOnAction(e -> resetMeditation());
    }

    private RadialGradient createGradient(Color centerColor, Color edgeColor) {
        return new RadialGradient(
            0, 0, 0.5, 0.5, 0.7, true, CycleMethod.NO_CYCLE,
            new Stop(0, centerColor.deriveColor(0, 1, 1.2, 1)),
            new Stop(0.3, centerColor),
            new Stop(0.7, edgeColor),
            new Stop(1, edgeColor.deriveColor(0, 1, 0.9, 1))
        );
    }

    private void updateFlowerPattern(double scale, double progress) {
        Color currentCenterColor, currentEdgeColor;
        
        if (progress <= 0.5) {
            double brightProgress = progress * 2;
            currentCenterColor = interpolateColor(DARK_CENTER, LIGHT_CENTER, brightProgress);
            currentEdgeColor = interpolateColor(DARK_EDGE, LIGHT_EDGE, brightProgress);
        } else {
            double darkProgress = (progress - 0.5) * 2;
            currentCenterColor = interpolateColor(LIGHT_CENTER, DARK_CENTER, darkProgress);
            currentEdgeColor = interpolateColor(LIGHT_EDGE, DARK_EDGE, darkProgress);
        }

        for (int i = 0; i < flowerPattern.getChildren().size() - 1; i++) {
            Circle petal = (Circle) flowerPattern.getChildren().get(i);
            double baseRadius = INITIAL_RADIUS * 0.3 * ((i % 3) + 1) * scale;
            petal.setRadius(baseRadius);
            petal.setFill(createGradient(
                currentCenterColor.deriveColor(0, 1, 1, 0.3),
                currentEdgeColor.deriveColor(0, 1, 1, 0.2)
            ));
        }

        Circle mainCircle = (Circle) flowerPattern.getChildren().get(flowerPattern.getChildren().size() - 1);
        mainCircle.setRadius(INITIAL_RADIUS * scale);
        mainCircle.setFill(createGradient(currentCenterColor, currentEdgeColor));
    }

    private Color interpolateColor(Color startColor, Color endColor, double progress) {
        return new Color(
            startColor.getRed() + (endColor.getRed() - startColor.getRed()) * progress,
            startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * progress,
            startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * progress,
            startColor.getOpacity() + (endColor.getOpacity() - startColor.getOpacity()) * progress
        );
    }

    private void setupBreathAnimation() {
        breathAnimation = new Timeline();
        List<KeyFrame> frames = new ArrayList<>();
        double timeOffset = 0;

        // Inhalation phase (5 seconds) - from small to large
        double breatheInDuration = 5.0;
        for (int i = 0; i <= 50; i++) {
            double progress = i / 50.0;
            double time = timeOffset + (breatheInDuration * progress);
            final double progressCopy = progress;
            
            KeyFrame frame = new KeyFrame(
                Duration.seconds(time),
                event -> updateFlowerPattern(1.0 + progressCopy * 0.7, progressCopy)
            );
            frames.add(frame);
        }
        frames.add(new KeyFrame(Duration.seconds(timeOffset), event -> updateStage(0)));
        timeOffset += breatheInDuration;

        //First breath hold (5 seconds) - hold at maximum
        frames.add(new KeyFrame(
            Duration.seconds(timeOffset),
            event -> {
                updateStage(1);
                updateFlowerPattern(1.7, 1.0);
            }
        ));
        frames.add(new KeyFrame(Duration.seconds(timeOffset + 5.0)));
        timeOffset += 5.0;

        // Exhalation phase (5 seconds) - from large to small
        double breatheOutDuration = 5.0;
        for (int i = 0; i <= 50; i++) {
            double progress = i / 50.0;
            double time = timeOffset + (breatheOutDuration * progress);
            final double progressCopy = progress;
            
            KeyFrame frame = new KeyFrame(
                Duration.seconds(time),
                event -> updateFlowerPattern(1.7 - (progressCopy * 0.7), 1.0 - progressCopy)
            );
            frames.add(frame);
        }
        frames.add(new KeyFrame(Duration.seconds(timeOffset), event -> updateStage(2)));
        timeOffset += breatheOutDuration;

        // Second breath hold (5 seconds) - Keep it at a minimum
        frames.add(new KeyFrame(
            Duration.seconds(timeOffset),
            event -> {
                updateStage(3);  // Modify to the correct stage
                updateFlowerPattern(1.0, 0.0);  // Keep it minimal
            }
        ));
        frames.add(new KeyFrame(Duration.seconds(timeOffset + 5.0)));
        timeOffset += 5.0;

        breathAnimation.getKeyFrames().addAll(frames);
        breathAnimation.setCycleCount(Animation.INDEFINITE);
    }

   
    private void startMeditation(double runMinutes, double logMinutes) { // Change to double type
     if (breathAnimation.getStatus() == Animation.Status.RUNNING) {
     return;
     }
     if (durationTimer != null) {
     durationTimer.stop();
     }

     durationTimer = new Timeline(
     new KeyFrame(Duration.seconds(runMinutes * 60), event -> {
     stopMeditation();
     statusLabel.setText("Meditation Complete");
     // Testing: Print log
     System.out.println("Creating meditation event");
     System.out.println("EventController is " + (eventController != null ? "not null" : "null"));
     Event meditationEvent = new Event(
     meditationEventId++,
     "Meditation",
     "Meditation",
     "Meditation session completed",
     LocalDate.now(),
     0,
     (int)(logMinutes) // Convert to seconds
     );
     meditationEvent.setFinished(true);
     eventController.addEvent(meditationEvent);
     System.out.println("Event added: " + meditationEvent);
     })
     );
     durationTimer.setCycleCount(1);
     timerLabel.setText("");
     updateStage(0);
     breathAnimation.play();
     durationTimer.play();
     disableButtons(true);
     startTimeDisplay(runMinutes);
     }

    private void startTimeDisplay(double totalMinutes) {
     // Initialize the remaining seconds
        remainingSeconds = (long)(totalMinutes * 60);
        
        // Display the initial time
        updateTimerDisplay();
        
        timeDisplay = new Timeline();
        
        KeyFrame kf = new KeyFrame(Duration.seconds(1), event -> {
            remainingSeconds--;
            if (remainingSeconds >= 0) {
                updateTimerDisplay();
            }
        });
        
        timeDisplay.getKeyFrames().add(kf);
        timeDisplay.setCycleCount((int)(totalMinutes * 60));
        timeDisplay.play();
    }

    private void updateTimerDisplay() {
        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
       

    private Timeline timeDisplay;
    
    private void stopMeditation() {
        if (breathAnimation != null) {
            breathAnimation.stop();
        }
        if (durationTimer != null) {
            durationTimer.stop();
        }
        remainingSeconds = 0;
        disableButtons(false);
        timerLabel.setText("");
    }

    private void disableButtons(boolean disabled) {
        tenMinButton.setDisable(disabled);
        thirtyMinButton.setDisable(disabled);
        oneHourButton.setDisable(disabled);
        resetButton.setDisable(false); 
    }

    @FXML
    public void resetMeditation() {
        if (timeDisplay != null) {
            timeDisplay.stop();  // Add this line to stop the time display
        }
        stopMeditation();
        updateFlowerPattern(1.0, 0.0);
        statusLabel.setText("Ready to Start");
        timerLabel.setText("");
        disableButtons(false);
    }
    
    private void updateStage(int stageIndex) {
        currentStage = stageIndex;
        statusLabel.setText(BREATH_STAGES[stageIndex]);
    }
}