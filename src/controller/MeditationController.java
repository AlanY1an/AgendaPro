package controller;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
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

    // 改进的颜色方案
    private final Color LIGHT_CENTER = Color.web("#7FFFD4");    // 浅绿松石色
    private final Color LIGHT_EDGE = Color.web("#48D1CC");      // 中绿松石色
    private final Color DARK_CENTER = Color.web("#20B2AA");     // 深绿松石色
    private final Color DARK_EDGE = Color.web("#008B8B");       // 深青色

    @FXML
    public void initialize() {
        setupBreathAnimation();
        resetMeditation();
    }

    private RadialGradient createGradient(Color centerColor, Color edgeColor) {
        return new RadialGradient(
            0,                      // focusAngle
            0,                      // focusDistance
            0.5,                    // centerX
            0.5,                    // centerY
            0.7,                    // radius (增大范围使渐变更明显)
            true,                   // proportional
            CycleMethod.NO_CYCLE,   
            new Stop(0, centerColor.deriveColor(0, 1.2, 1.1, 1)),    // 让中心更亮
            new Stop(0.3, centerColor),                               // 添加中间过渡色
            new Stop(0.7, edgeColor),                                 // 添加中间过渡色
            new Stop(1, edgeColor.deriveColor(0, 1, 0.9, 1))         // 让边缘更深
        );
    }

    private RadialGradient interpolateGradient(double progress) {
        // 增强渐变效果
        double enhancedProgress = Math.pow(progress, 0.8); // 使渐变更加明显
        
        // 在两组颜色之间进行插值，添加额外的亮度变化
        Color currentCenterColor = interpolateColor(LIGHT_CENTER, DARK_CENTER, enhancedProgress)
            .deriveColor(0, 1 + (0.2 * (1 - enhancedProgress)), 1, 1);
        Color currentEdgeColor = interpolateColor(LIGHT_EDGE, DARK_EDGE, enhancedProgress)
            .deriveColor(0, 1 - (0.1 * enhancedProgress), 1, 1);
            
        return createGradient(currentCenterColor, currentEdgeColor);
    }

    private Color interpolateColor(Color startColor, Color endColor, double progress) {
        // 增加色彩饱和度的变化
        double saturationChange = Math.sin(progress * Math.PI) * 0.1;
        return new Color(
            startColor.getRed() + (endColor.getRed() - startColor.getRed()) * progress,
            startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * progress,
            startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * progress,
            startColor.getOpacity() + (endColor.getOpacity() - startColor.getOpacity()) * progress
        ).deriveColor(0, 1 + saturationChange, 1, 1);
    }

    // setupBreathAnimation 方法保持不变，但效果会因为新的颜色和渐变设置而改变
    private void setupBreathAnimation() {
        breathAnimation = new Timeline();
        List<KeyFrame> frames = new ArrayList<>();
        double timeOffset = 0;

        // 初始状态
        frames.add(new KeyFrame(
            Duration.seconds(0),
            new KeyValue(breathIndicator.radiusProperty(), INITIAL_RADIUS, Interpolator.DISCRETE),
            new KeyValue(breathIndicator.fillProperty(), createGradient(LIGHT_CENTER, LIGHT_EDGE), Interpolator.DISCRETE)
        ));

        // 吸气阶段 (4秒) - 变小，颜色渐变到深色
        double breatheInDuration = 4.0;
        for (int i = 0; i <= 40; i++) {
            double progress = i / 40.0;
            double time = timeOffset + (breatheInDuration * progress);
            double radius = MAX_RADIUS - (progress * (MAX_RADIUS - MIN_RADIUS));
            
            RadialGradient currentGradient = interpolateGradient(progress);
            
            KeyFrame frame = new KeyFrame(
                Duration.seconds(time),
                new KeyValue(breathIndicator.radiusProperty(), radius, Interpolator.EASE_BOTH),
                new KeyValue(breathIndicator.fillProperty(), currentGradient, Interpolator.EASE_BOTH)
            );
            frames.add(frame);
        }
        frames.add(new KeyFrame(Duration.seconds(timeOffset), event -> updateStage(0)));
        timeOffset += breatheInDuration;

        // 屏息阶段1 (3秒)
        frames.add(new KeyFrame(
            Duration.seconds(timeOffset),
            event -> updateStage(1),
            new KeyValue(breathIndicator.radiusProperty(), MIN_RADIUS, Interpolator.DISCRETE),
            new KeyValue(breathIndicator.fillProperty(), createGradient(DARK_CENTER, DARK_EDGE), Interpolator.DISCRETE)
        ));
        timeOffset += 3.0;

        // 呼气阶段 (4秒)
        double breatheOutDuration = 4.0;
        for (int i = 0; i <= 40; i++) {
            double progress = i / 40.0;
            double time = timeOffset + (breatheOutDuration * progress);
            double radius = MIN_RADIUS + (progress * (MAX_RADIUS - MIN_RADIUS));
            
            RadialGradient currentGradient = interpolateGradient(1 - progress);
            
            KeyFrame frame = new KeyFrame(
                Duration.seconds(time),
                new KeyValue(breathIndicator.radiusProperty(), radius, Interpolator.EASE_BOTH),
                new KeyValue(breathIndicator.fillProperty(), currentGradient, Interpolator.EASE_BOTH)
            );
            frames.add(frame);
        }
        frames.add(new KeyFrame(Duration.seconds(timeOffset), event -> updateStage(2)));
        timeOffset += breatheOutDuration;

        // 屏息阶段2 (3秒)
        frames.add(new KeyFrame(
            Duration.seconds(timeOffset),
            event -> updateStage(3),
            new KeyValue(breathIndicator.radiusProperty(), MAX_RADIUS, Interpolator.DISCRETE),
            new KeyValue(breathIndicator.fillProperty(), createGradient(LIGHT_CENTER, LIGHT_EDGE), Interpolator.DISCRETE)
        ));
        
        frames.add(new KeyFrame(
            Duration.seconds(timeOffset + 3.0),
            new KeyValue(breathIndicator.radiusProperty(), MAX_RADIUS, Interpolator.DISCRETE),
            new KeyValue(breathIndicator.fillProperty(), createGradient(LIGHT_CENTER, LIGHT_EDGE), Interpolator.DISCRETE)
        ));
        timeOffset += 3.0;

        breathAnimation.getKeyFrames().addAll(frames);
        breathAnimation.setCycleCount(Animation.INDEFINITE);
    }

    // startMeditation, resetMeditation, 和 updateStage 方法保持不变
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
        breathIndicator.setFill(createGradient(LIGHT_CENTER, LIGHT_EDGE));
        
        statusLabel.setText("Ready to Start");
        statusLabel.setTextFill(Color.BLACK);
        
        startButton.setDisable(false);
    }

    private void updateStage(int stageIndex) {
        currentStage = stageIndex;
        statusLabel.setText(BREATH_STAGES[stageIndex]);
    }
}

