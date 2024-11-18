package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        PomodoroView pomodoroView = new PomodoroView();

        Scene scene = new Scene(pomodoroView.getView());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pomodoro Timer");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
