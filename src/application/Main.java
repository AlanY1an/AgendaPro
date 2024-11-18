package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import application.Login;
import application.LoginController;
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
    	Login loginView = new Login(this);
        primaryStage.setScene(loginView.getScene());
        primaryStage.setTitle("Agendapro");
        primaryStage.show();
    }

    public void showSecondaryView() {
        // 创建 SecondaryView 对象
    	DemoPage secondaryView = new DemoPage(this);
    	primaryStage.setScene(secondaryView.getScene());
        primaryStage.setTitle("Agendapro");
        primaryStage.show();
    }

	public static void main(String[] args) {
		launch(args);
	}
}
