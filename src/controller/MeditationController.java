package controller;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MeditationController {
    @FXML private Circle breathIndicator;
    @FXML private Label statusLabel;
    @FXML private Button startButton;
    @FXML private Button resetButton;

    private Timeline breathAnimation;
    private final String[] BREATH_STAGES = {"Breathe In", "Hold", "Breathe Out", "Hold"};
    private final double INITIAL_RADIUS = 30;
    private final double MIN_RADIUS = 20;
    private final double MAX_RADIUS = 60;
    private int currentStage = 0;

    @FXML
    public void initialize() {
        setupBreathAnimation();
        resetMeditation();
    }

    private void setupBreathAnimation() {
        breathAnimation = new Timeline();
        
        List<KeyFrame> frames = new ArrayList<>();
        double timeOffset = 0;

        // 第一次循环从初始大小开始
        frames.add(new KeyFrame(
            Duration.seconds(0),
            new KeyValue(breathIndicator.radiusProperty(), INITIAL_RADIUS, Interpolator.DISCRETE)
        ));

        // 吸气阶段 (4秒) - 变小
        double breatheInDuration = 4.0;
        for (int i = 0; i <= 40; i++) {
            double progress = i / 40.0;
            double time = timeOffset + (breatheInDuration * progress);
            double radius = MAX_RADIUS - (progress * (MAX_RADIUS - MIN_RADIUS));
            
            KeyFrame frame = new KeyFrame(
                Duration.seconds(time),
                new KeyValue(breathIndicator.radiusProperty(), radius, Interpolator.EASE_BOTH)
            );
            frames.add(frame);
        }
        frames.add(new KeyFrame(Duration.seconds(timeOffset), event -> updateStage(0)));
        timeOffset += breatheInDuration;

        // 屏息阶段1 (3秒) - 保持最小大小
        frames.add(new KeyFrame(
            Duration.seconds(timeOffset),
            event -> updateStage(1),
            new KeyValue(breathIndicator.radiusProperty(), MIN_RADIUS, Interpolator.DISCRETE)
        ));
        timeOffset += 3.0;

        // 呼气阶段 (4秒) - 从最小变大
        double breatheOutDuration = 4.0;
        for (int i = 0; i <= 40; i++) {
            double progress = i / 40.0;
            double time = timeOffset + (breatheOutDuration * progress);
            double radius = MIN_RADIUS + (progress * (MAX_RADIUS - MIN_RADIUS));
            
            KeyFrame frame = new KeyFrame(
                Duration.seconds(time),
                new KeyValue(breathIndicator.radiusProperty(), radius, Interpolator.EASE_BOTH)
            );
            frames.add(frame);
        }
        frames.add(new KeyFrame(Duration.seconds(timeOffset), event -> updateStage(2)));
        timeOffset += breatheOutDuration;

        // 屏息阶段2 (3秒) - 保持最大大小
        frames.add(new KeyFrame(
            Duration.seconds(timeOffset),
            event -> updateStage(3),
            new KeyValue(breathIndicator.radiusProperty(), MAX_RADIUS, Interpolator.DISCRETE)
        ));
        
        // 在hold阶段结束时添加一个关键帧，确保大小保持不变
        frames.add(new KeyFrame(
            Duration.seconds(timeOffset + 3.0),
            new KeyValue(breathIndicator.radiusProperty(), MAX_RADIUS, Interpolator.DISCRETE)
        ));
        timeOffset += 3.0;

        // 添加所有关键帧到动画时间线
        breathAnimation.getKeyFrames().addAll(frames);
        breathAnimation.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    public void startMeditation() {
        if (breathAnimation.getStatus() == Animation.Status.RUNNING) {
            return;
        }
        updateStage(0);
        breathAnimation.play();
        startButton.setDisable(true);
    }

    @FXML
    public void resetMeditation() {
        if (breathAnimation != null && breathAnimation.getStatus() == Animation.Status.RUNNING) {
            breathAnimation.stop();
        }

        breathIndicator.setRadius(INITIAL_RADIUS);
        breathIndicator.setFill(Color.web("#6EC3E9"));
        
        statusLabel.setText("Ready to Start");
        statusLabel.setTextFill(Color.BLACK);
        
        startButton.setDisable(false);
    }

    private void updateStage(int stageIndex) {
        currentStage = stageIndex;
        statusLabel.setText(BREATH_STAGES[stageIndex]);
        
        // 根据不同阶段设置不同的颜色
        Color stageColor;
        switch (stageIndex) {
            case 0: // Breathe In
                stageColor = Color.web("#6EC3E9");
                break;
            case 1: // Hold
                stageColor = Color.web("#4A90E2");
                break;
            case 2: // Breathe Out
                stageColor = Color.web("#6EC3E9");
                break;
            case 3: // Hold
                stageColor = Color.web("#4A90E2");
                break;
            default:
                stageColor = Color.web("#6EC3E9");
        }
        breathIndicator.setFill(stageColor);
    }
    
}

