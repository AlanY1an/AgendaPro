package application;
	
import controller.EventController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.PomodoroView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
    	EventController eventController = new EventController(); 
        PomodoroView pomodoroView = new PomodoroView(eventController); 

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
