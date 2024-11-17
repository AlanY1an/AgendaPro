package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import application.Login;
import application.DemoPage;

public class Main extends Application {
	
	private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // 显示主界面
        showLoginView();
    }

    public void showLoginView() {
        // 创建 MainView 对象
        Login mainView = new Login(this);
        Scene scene = new Scene(mainView.getView(), 400, 300);
       
        primaryStage.setTitle("Agendapro");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showSecondaryView() {
        // 创建 SecondaryView 对象
    	DemoPage secondaryView = new DemoPage(this);
        Scene scene = new Scene(secondaryView.getView(), 400, 300);
        
        primaryStage.setTitle("Demo View");
        primaryStage.setScene(scene);
    }

	public static void main(String[] args) {
		launch(args);
	}
}
