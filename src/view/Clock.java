package view;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import javafx.scene.layout.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Clock {

    public VBox createClock() {
        // Construct the analogue clock pieces
        final Circle face = new Circle(100, 100, 100);
        face.setId("face");
        final Line hourHand = new Line(0, 0, 0, -50);
        hourHand.setTranslateX(100);
        hourHand.setTranslateY(100);
        hourHand.setId("hourHand");
        final Line minuteHand = new Line(0, 0, 0, -75);
        minuteHand.setTranslateX(100);
        minuteHand.setTranslateY(100);
        minuteHand.setId("minuteHand");
        final Line secondHand = new Line(0, 15, 0, -88);
        secondHand.setTranslateX(100);
        secondHand.setTranslateY(100);
        secondHand.setId("secondHand");
        final Circle spindle = new Circle(100, 100, 5);
        spindle.setId("spindle");

        Group ticks = new Group();
        for (int i = 0; i < 12; i++) {
            Line tick = new Line(0, -83, 0, -93);
            tick.setTranslateX(100);
            tick.setTranslateY(100);
            tick.getStyleClass().add("tick");
            tick.getTransforms().add(new Rotate(i * (360 / 12)));
            ticks.getChildren().add(tick);
        }

        final Group analogueClock = new Group(face, ticks, spindle, hourHand, minuteHand, secondHand);
       


        // Construct the digital clock
        final Label digitalClock = new Label();
        digitalClock.setId("digitalClock");

        // Determine the starting time
        Calendar calendar = GregorianCalendar.getInstance();
        final double seedSecondDegrees = calendar.get(Calendar.SECOND) * (360 / 60);
        final double seedMinuteDegrees = (calendar.get(Calendar.MINUTE) + seedSecondDegrees / 360.0) * (360 / 60);
        final double seedHourDegrees = (calendar.get(Calendar.HOUR) + seedMinuteDegrees / 360.0) * (360 / 12);

        // Define rotations
        final Rotate hourRotate = new Rotate(seedHourDegrees);
        final Rotate minuteRotate = new Rotate(seedMinuteDegrees);
        final Rotate secondRotate = new Rotate(seedSecondDegrees);
        hourHand.getTransforms().add(hourRotate);
        minuteHand.getTransforms().add(minuteRotate);
        secondHand.getTransforms().add(secondRotate);

        // Create animations
        Timeline hourTime = new Timeline(new KeyFrame(Duration.hours(12),
                new KeyValue(hourRotate.angleProperty(), 360 + seedHourDegrees, Interpolator.LINEAR)));
        hourTime.setCycleCount(Animation.INDEFINITE);
        hourTime.play();

        Timeline minuteTime = new Timeline(new KeyFrame(Duration.minutes(60),
                new KeyValue(minuteRotate.angleProperty(), 360 + seedMinuteDegrees, Interpolator.LINEAR)));
        minuteTime.setCycleCount(Animation.INDEFINITE);
        minuteTime.play();

        Timeline secondTime = new Timeline(new KeyFrame(Duration.seconds(60),
                new KeyValue(secondRotate.angleProperty(), 360 + seedSecondDegrees, Interpolator.LINEAR)));
        secondTime.setCycleCount(Animation.INDEFINITE);
        secondTime.play();

        Timeline digitalTime = new Timeline(new KeyFrame(Duration.seconds(0),
                e -> {
                    Calendar now = GregorianCalendar.getInstance();
                    String hour = String.format("%02d", now.get(Calendar.HOUR) == 0 ? 12 : now.get(Calendar.HOUR));
                    String minute = String.format("%02d", now.get(Calendar.MINUTE));
                    String second = String.format("%02d", now.get(Calendar.SECOND));
                    String ampm = now.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                    digitalClock.setText(hour + ":" + minute + ":" + second + " " + ampm);
                }),
                new KeyFrame(Duration.seconds(1)));
        digitalTime.setCycleCount(Animation.INDEFINITE);
        digitalTime.play();

        // Add mouse effects
        final Glow glow = new Glow();
        analogueClock.setOnMouseEntered(e -> analogueClock.setEffect(glow));
        analogueClock.setOnMouseExited(e -> analogueClock.setEffect(null));

        // Layout
        final VBox layout = new VBox();
        layout.getChildren().addAll(analogueClock,digitalClock);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(-0); // 减少间距


        layout.setPadding(new Insets(0, 30, 30, 30)); // 上20px，右30px，下20px，左30px

        
        analogueClock.setScaleX(0.7);
        analogueClock.setScaleY(0.7);

        // Load CSS
        String css = getClass().getResource("clock.css").toExternalForm();
        layout.getStylesheets().add(css);
        return layout;
    }
}
