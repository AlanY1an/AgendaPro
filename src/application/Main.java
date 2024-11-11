package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Text;

 
	


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		Text text = new Text();
		text.setText("Hello World");
		
		primaryStage.setWidth(420);
		primaryStage.setHeight(420);
		text.setX((primaryStage.getWidth() - text.getLayoutBounds().getWidth()) / 2);
		text.setY((primaryStage.getWidth() - text.getLayoutBounds().getWidth()) / 2);
		root.getChildren().add(text);
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Stage Demo Program");
		
		// Disable Resize
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
