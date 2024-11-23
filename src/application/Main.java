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

//        // 标题
//        Label title = new Label("AgendaPro");
//        title.setFont(Font.font("Arial", 24));
//        title.setTextFill(Color.BLACK);
//        title.setStyle("-fx-padding: 20 20 50 20; -fx-background-radius: 8;");
        VBox logoContainer = new VBox();
        logoContainer.setStyle("-fx-padding: 0 0 50 -20;"); // 应用内边距
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/resources/AgendaPro.png")));
        logo.setFitWidth(200);
        logo.setFitHeight(80);
        logoContainer.getChildren().add(logo); // 将 Logo 添加到容器中

        // 菜单项
        HBox dashboard = createMenuItem("DashBoard", "/resources/icons/dashboard.png", null);
        HBox calendar = createMenuItem("Calendar", "/resources/icons/calendar.png", "/view/CalendarView.fxml");
        HBox achievement = createMenuItem("Achievement", "/resources/icons/achievement.png", "/view/Achievement.fxml");
        HBox task = createMenuItem("Task", "/resources/icons/task.png", null);

        // 添加菜单项
        sidebar.getChildren().addAll(logoContainer, dashboard, calendar, achievement, task);

        return sidebar;
    }

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
            label.setTextFill(Color.web("#4CAF50"));
            menuItem.setStyle("-fx-background-color: #E8F5E9; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
        });
        menuItem.setOnMouseExited(e -> {
            label.setTextFill(Color.BLACK);
            menuItem.setStyle("-fx-background-color: transparent; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
        });

        // 点击事件：加载指定 FXML 文件到右侧内容区域
        if (fxmlPath != null) {
            menuItem.setOnMouseClicked(e -> loadContent(fxmlPath));
        }

        return menuItem;
    }

    private void loadContent(String fxmlPath) {
        try {
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
