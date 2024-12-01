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
import controller.EventController;

public class MeditationController {
	    @FXML private Pane mainPane;  
	    @FXML private Label statusLabel;
	    @FXML private Label timerLabel;
	    @FXML private Button tenMinButton;
	    @FXML private Button thirtyMinButton;
	    @FXML private Button oneHourButton;

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

	    private final Color LIGHT_CENTER = Color.web("#7FFFD4", 0.9);  // 保持不变
	    private final Color LIGHT_EDGE = Color.web("#48D1CC", 0.7);    // 保持不变
	    private final Color DARK_CENTER = Color.web("#60C4C0", 0.9);   // 从#40B4B0改为#60C4C0，使其更亮
	    private final Color DARK_EDGE = Color.web("#40B4B0", 0.7); 

	    @FXML
	    public void initialize() {
	    	this.eventController = new EventController();
	        setupFlowerPattern();
	        setupBreathAnimation();
	        setupButtons();
	        resetMeditation();
	        timerLabel.setText("");
	    }

	    private void setupFlowerPattern() {
	        flowerPattern = new Group();
	        
	        for (int i = 0; i < PETAL_COUNT; i++) {
	            double angle = (360.0 / PETAL_COUNT) * i;
	        
	        for (int j = 0; j < 3; j++) {
	            Circle petal = new Circle(
	                INITIAL_RADIUS * 0.37 * (j + 1),  // 调整为0.37，与新的初始半径相配合
	                createGradient(LIGHT_CENTER.deriveColor(0, 1, 1, 0.3),
	                             LIGHT_EDGE.deriveColor(0, 1, 1, 0.2))
	            );
	            
	            double offset = INITIAL_RADIUS * 1.1;  // 调整为1.1，让花瓣分布更合适
	            petal.setTranslateX(Math.cos(Math.toRadians(angle)) * offset);
	            petal.setTranslateY(Math.sin(Math.toRadians(angle)) * offset);
	            petal.setEffect(new GaussianBlur(7));  // 调整为7，与新的大小相称
	            
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
	        tenMinButton.setOnAction(e -> startMeditation(10));
	        thirtyMinButton.setOnAction(e -> startMeditation(30));
	        oneHourButton.setOnAction(e -> startMeditation(60));
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

	        // 吸气阶段 (5秒) - 从小变大
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

	        // 第一次屏息 (5秒) - 保持最大状态
	        frames.add(new KeyFrame(
	            Duration.seconds(timeOffset),
	            event -> {
	                updateStage(1);
	                updateFlowerPattern(1.7, 1.0);
	            }
	        ));
	        frames.add(new KeyFrame(Duration.seconds(timeOffset + 5.0)));
	        timeOffset += 5.0;

	        // 呼气阶段 (5秒) - 从大变小
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

	        // 第二次屏息 (5秒) - 保持最小状态
	        frames.add(new KeyFrame(
	            Duration.seconds(timeOffset),
	            event -> {
	                updateStage(3);  // 修改为正确的阶段
	                updateFlowerPattern(1.0, 0.0);  // 保持最小状态
	            }
	        ));
	        frames.add(new KeyFrame(Duration.seconds(timeOffset + 5.0)));
	        timeOffset += 5.0;

	        breathAnimation.getKeyFrames().addAll(frames);
	        breathAnimation.setCycleCount(Animation.INDEFINITE);
	    }

	   
	    private void startMeditation(int minutes) {
	        if (breathAnimation.getStatus() == Animation.Status.RUNNING) {
	            return;
	        }
	        
	        currentMeditationDuration = minutes;
	        
	        if (durationTimer != null) {
	            durationTimer.stop();
	        }

	        durationTimer = new Timeline(
	            new KeyFrame(Duration.minutes(minutes), event -> {
	                stopMeditation();
	                statusLabel.setText("Meditation Complete");
	            
	            Event meditationEvent = new Event(
	                    meditationEventId++,
	                    "Meditation Session",
	                    "Meditation",
	                    minutes + " minute meditation session completed",
	                    LocalDate.now(),
	                    0,  // duration for pomodoro
	                    minutes  // meditationMinutes
	                );
	                eventController.addEvent(meditationEvent);
	            })
	        );
	        durationTimer.setCycleCount(1);
	        timerLabel.setText("");
	        updateStage(0);
	        breathAnimation.play();
	        durationTimer.play();
	        disableButtons(true);
	        startTimeDisplay(minutes);
	    }

	    private void startTimeDisplay(int totalMinutes) {
	        // 初始化剩余秒数
	        remainingSeconds = totalMinutes * 60;
	        
	        // 显示初始时间
	        timerLabel.setText(String.format("%02d:00", totalMinutes));
	        
	        timeDisplay = new Timeline();
	        
	        // 创建每秒更新的关键帧
	        KeyFrame kf = new KeyFrame(Duration.seconds(1), event -> {
	            remainingSeconds--;
	            if (remainingSeconds >= 0) {
	                long minutes = remainingSeconds / 60;
	                long seconds = remainingSeconds % 60;
	                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
	            }
	        });
	        
	        timeDisplay.getKeyFrames().add(kf);
	        timeDisplay.setCycleCount(totalMinutes * 60);
	        timeDisplay.play();
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
	    }

	    @FXML
	    public void resetMeditation() {
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


