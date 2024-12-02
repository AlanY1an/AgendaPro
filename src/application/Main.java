package application;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import controller.AchievementController;
import controller.DashboardController;
import controller.EventController;
import controller.MeditationController;
import controller.PomodoroController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Achievements;
import model.Event;
import model.Task;
import view.CalendarView;

import java.io.OutputStream;
import java.io.PrintStream;


public class Main extends Application {

    private BorderPane root; // 主布局容器
    private EventController eventController;
    private Achievements ac;
    
    @Override
    public void start(Stage primaryStage) {
        try {
        	
        	eventController = new EventController();
        	ac = new Achievements(eventController);
        	
        	initiateData(ac);
            // 主布局
            root = new BorderPane();

            // 左侧菜单栏
            VBox sidebar = createSidebar();

            // 默认内容区域
            VBox defaultContent = new VBox();
            defaultContent.setStyle("-fx-background-color: #e0e0e0;");
            
            // 设置布局
            root.setLeft(sidebar);
            root.setCenter(defaultContent);
            loadContent("/view/Dashboard.fxml");

            // 创建场景
            Scene scene = new Scene(root, 1010, 600);
            primaryStage.setMaxWidth(1010);
            primaryStage.setMaxHeight(690);
            primaryStage.setResizable(false);

            
            
            primaryStage.setTitle("AgendaPro");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void initiateData(Achievements ac) {
		// TODO Auto-generated method stub
		ac.getEventController().addEvent(new Event(1, "Study", LocalDate.now(), true));
        ac.getEventController().addEvent(new Event(2, "Work", LocalDate.now().minusDays(1), true));
        ac.getEventController().addEvent(new Event(3, "Exercise", LocalDate.now().minusDays(35), true));

        // Add default tasks
        ac.addTask(new Task("Complete Homework", convertToDate(LocalDate.now().minusDays(1)), true));
        ac.addTask(new Task("Attend Meeting", convertToDate(LocalDate.now().minusDays(5)), true));
        ac.addTask(new Task("Go Jogging", convertToDate(LocalDate.now().minusDays(40)), true));
	}


	private Date convertToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
    
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
        HBox dashboard = createMenuItem("DashBoard", "/resources/icons/dashboard.png", "/view/Dashboard.fxml");
        HBox calendar = createMenuItem("Calendar", "/resources/icons/calendar.png", "/view/CalendarView.fxml");
        
        HBox achievement = createMenuItem("Achievement", "/resources/icons/achievement.png", "/view/Achievement.fxml");
        HBox task = createMenuItem("Task", "/resources/icons/task.png", "/view/ToToTask.fxml");

        // Tools 分类的菜单项
        HBox meditation = createMenuItem("Meditation", "/resources/icons/cloud.png", "/view/Meditation.fxml");
        HBox pomodoroTimer = createMenuItem("Pomodoro Timer", "/resources/icons/clock.png", "/view/Pomodoro.fxml");

        // 将分类和菜单项添加到侧边栏
        sidebar.getChildren().addAll(logoContainer, planLabel, dashboard, calendar, achievement, task, toolsLabel, meditation, pomodoroTimer);

        // 默认选中 Dashboard 菜单项
        selectedMenuItem = dashboard; // 设置 Dashboard 为选中
        ((Label) dashboard.getChildren().get(1)).setTextFill(Color.web("#4CAF50"));
        ((ImageView) dashboard.getChildren().get(0)).setImage(new Image(getClass().getResourceAsStream("/resources/icons/dashboard1.png")));
        dashboard.setStyle("-fx-background-color: #E8F5E9; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");

        
        return sidebar;
    }


    private HBox selectedMenuItem;
    private HBox createMenuItem(String text, String iconPath, String fxmlPath) {
        // 加载图标
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(20);
        icon.setFitHeight(20);

        // 创建文字标签
        Label label = new Label(text);
        label.setFont(Font.font("Arial", 16));
        label.setTextFill(Color.BLACK);

        // 创建 HBox 并设置样式
        HBox menuItem = new HBox(10, icon, label);
        menuItem.setStyle("-fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");

        // 鼠标悬停效果
        menuItem.setOnMouseEntered(e -> {
            if (menuItem != selectedMenuItem) { // 非选中项才应用悬停效果
                label.setTextFill(Color.web("#4CAF50"));
                menuItem.setStyle("-fx-background-color: #E8F5E9; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
                
                // 切换为悬停图标
                String hoverIconPath = iconPath.replace(".png", "1.png");
                Image hoverImage = new Image(getClass().getResourceAsStream(hoverIconPath));
                if (!hoverImage.isError()) {
                    icon.setImage(hoverImage);
                } else {
                    System.err.println("Hover icon not found: " + hoverIconPath);
                }
            }
        });
        menuItem.setOnMouseExited(e -> {
            if (menuItem != selectedMenuItem) { // 非选中项恢复默认样式
                label.setTextFill(Color.BLACK);
                menuItem.setStyle("-fx-background-color: #ffffff; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
                
                // 恢复默认图标
                Image defaultImage = new Image(getClass().getResourceAsStream(iconPath));
                if (!defaultImage.isError()) {
                    icon.setImage(defaultImage);
                } else {
                    System.err.println("Default icon not found: " + iconPath);
                }
            }
            

        });

        
     // 点击事件：设置选中状态并加载内容
     // 点击事件：设置选中状态并加载内容
        menuItem.setOnMouseClicked(e -> {
            if (selectedMenuItem != null) { // 恢复上一个选中项的默认样式
                Label previousLabel = (Label) selectedMenuItem.getChildren().get(1);
                ImageView previousIcon = (ImageView) selectedMenuItem.getChildren().get(0);

                previousLabel.setTextFill(Color.BLACK);
                previousIcon.setImage(new Image(getClass().getResourceAsStream("/resources/icons/" + previousLabel.getText().toLowerCase() + ".png")));
                selectedMenuItem.setStyle("-fx-background-color: #ffffff; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
            }

            // 设置当前菜单项为选中状态
            selectedMenuItem = menuItem;
            label.setTextFill(Color.web("#4CAF50"));
            icon.setImage(new Image(getClass().getResourceAsStream("/resources/icons/" + text.toLowerCase() + "1.png")));
            menuItem.setStyle("-fx-background-color: #E8F5E9; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");

            // 加载指定 FXML 文件
            if (fxmlPath != null) {
                loadContent(fxmlPath);
            }
        });




        return menuItem;
    }


    private void loadContent(String fxmlPath) {
        try {
        	System.out.println("Loading FXML View: " + fxmlPath);
            // 使用 FXMLLoader 加载 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (fxmlPath.equals("/view/Achievement.fxml")) {
                loader.setControllerFactory(param -> new AchievementController(ac)); // 注入 Achievements
            } else if (fxmlPath.equals("/view/CalendarView.fxml")) {
                loader.setControllerFactory(param -> new CalendarView(eventController));
            } else if (fxmlPath.equals("/view/Pomodoro.fxml")) {
                loader.setControllerFactory(param -> {
                    PomodoroController controller = new PomodoroController();
                    controller.setEventController(eventController);
                    return controller;
                });
            } else if (fxmlPath.equals("/view/Meditation.fxml")) {
                loader.setControllerFactory(param -> {
                    MeditationController controller = new MeditationController();
                    controller.setEventController(eventController);
                    return controller;
                });
            }

            Parent content = loader.load();
            
            root.setCenter(content);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}
