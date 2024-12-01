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

<<<<<<< Updated upstream
    public void showLoginView() {
    	Login loginView = new Login(this);
        primaryStage.setScene(loginView.getScene());
        primaryStage.setTitle("Agendapro");
        primaryStage.show();
=======
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-spacing: 10;");

        // 添加 Logo
        VBox logoContainer = new VBox();
        logoContainer.setStyle("-fx-padding: 20 0 20 5;"); // 应用内边距
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/resources/AgendaPro.png")));
        logo.setFitWidth(125);
        logo.setFitHeight(43);
        logoContainer.getChildren().add(logo);

        // 创建分类标题
        Label planLabel = new Label("Plan");
        planLabel.setFont(Font.font("Arial", 14));
        planLabel.setTextFill(Color.GRAY);
        planLabel.setStyle("-fx-padding: 10 0 5 0;");

        Label toolsLabel = new Label("Tools");
        toolsLabel.setFont(Font.font("Arial", 14));
        toolsLabel.setTextFill(Color.GRAY);
        toolsLabel.setStyle("-fx-padding: 10 0 5 0;");

        // Plan 分类的菜单项
        HBox dashboard = createMenuItem("DashBoard", "/resources/icons/dashboard.png", null);
        HBox calendar = createMenuItem("Calendar", "/resources/icons/calendar.png", "/view/CalendarView.fxml");
        HBox achievement = createMenuItem("Achievement", "/resources/icons/achievement.png", "/view/Achievement.fxml");
        HBox task = createMenuItem("Task", "/resources/icons/task.png", "/view/ToToTask.fxml");

        // Tools 分类的菜单项
        HBox meditation = createMenuItem("Meditation", "/resources/icons/cloud.png", null);
        HBox pomodoroTimer = createMenuItem("Pomodoro Timer", "/resources/icons/clock.png", null);

        // 将分类和菜单项添加到侧边栏
        sidebar.getChildren().addAll(logoContainer, planLabel, dashboard, calendar, achievement, task, toolsLabel, meditation, pomodoroTimer);

        return sidebar;
>>>>>>> Stashed changes
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
