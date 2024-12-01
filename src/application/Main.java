package application;

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

public class Main extends Application {

    private BorderPane root; // 主布局容器

    @Override
    public void start(Stage primaryStage) {
        try {
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

            // 创建场景
            Scene scene = new Scene(root, 1000, 600);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(690);
            
            primaryStage.setTitle("AgendaPro");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        HBox dashboard = createMenuItem("DashBoard", "/resources/icons/dashboard.png", null);
        HBox calendar = createMenuItem("Calendar", "/resources/icons/calendar.png", "/view/CalendarView.fxml");
        HBox achievement = createMenuItem("Achievement", "/resources/icons/achievement.png", "/view/Achievement.fxml");
        HBox task = createMenuItem("Task", "/resources/icons/task.png", null);

        // Tools 分类的菜单项
        HBox meditation = createMenuItem("Meditation", "/resources/icons/cloud.png", "/view/Meditation.fxml");
        HBox pomodoroTimer = createMenuItem("Pomodoro Timer", "/resources/icons/clock.png", "/view/Pomodoro.fxml");

        // 将分类和菜单项添加到侧边栏
        sidebar.getChildren().addAll(logoContainer, planLabel, dashboard, calendar, achievement, task, toolsLabel, meditation, pomodoroTimer);

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
            }
        });
        menuItem.setOnMouseExited(e -> {
            if (menuItem != selectedMenuItem) { // 非选中项恢复默认样式
                label.setTextFill(Color.BLACK);
                menuItem.setStyle("-fx-background-color: #ffffff; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
            }
        });

        // 点击事件：设置选中状态并加载内容
        menuItem.setOnMouseClicked(e -> {
            if (selectedMenuItem != null) { // 恢复上一个选中项的默认样式
                ((Label) selectedMenuItem.getChildren().get(1)).setTextFill(Color.BLACK);
                selectedMenuItem.setStyle("-fx-background-color: #ffffff; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
            }

            // 设置当前菜单项为选中状态
            selectedMenuItem = menuItem;
            label.setTextFill(Color.web("#4CAF50"));
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
