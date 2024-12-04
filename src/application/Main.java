package application;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import controller.AchievementController;
import controller.DashboardController;
import controller.EventController;
import controller.MeditationController;
import controller.PomodoroController;
import controller.TaskController;
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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;


public class Main extends Application {

    private BorderPane root; 
    private EventController eventController;
    private TaskController taskController;
    private Achievements ac;
    
    @Override
    public void start(Stage primaryStage) {
        try {
        	eventController = new EventController();
        	taskController= new TaskController();
        	ac = new Achievements(eventController,taskController);

            root = new BorderPane();

            VBox sidebar = createSidebar();

            VBox defaultContent = new VBox();
            defaultContent.setStyle("-fx-background-color: #e0e0e0;");
            
            root.setLeft(sidebar);
            root.setCenter(defaultContent);
            loadContent("/view/Dashboard.fxml");

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


	private Date convertToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
    
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-spacing: 10;");

     
        VBox logoContainer = new VBox();
        logoContainer.setStyle("-fx-padding: 20 0 20 5;"); 
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/resources/AgendaPro.png")));
        logo.setFitWidth(125);
        logo.setFitHeight(43);
        logoContainer.getChildren().add(logo);

        
        Label planLabel = new Label("Plan");
        planLabel.setFont(Font.font("Arial", 14));
        planLabel.setTextFill(Color.GRAY);
        planLabel.setStyle("-fx-padding: 10 0 5 0;");

        Label toolsLabel = new Label("Tools");
        toolsLabel.setFont(Font.font("Arial", 14));
        toolsLabel.setTextFill(Color.GRAY);
        toolsLabel.setStyle("-fx-padding: 10 0 5 0;");

    
        HBox dashboard = createMenuItem("DashBoard", "/resources/icons/dashboard.png", "/view/Dashboard.fxml");
        HBox calendar = createMenuItem("Calendar", "/resources/icons/calendar.png", "/view/CalendarView.fxml");
        
        HBox achievement = createMenuItem("Achievement", "/resources/icons/achievement.png", "/view/Achievement.fxml");
        HBox task = createMenuItem("Task", "/resources/icons/task.png", "/view/Todolist.fxml");


        HBox meditation = createMenuItem("Meditation", "/resources/icons/cloud.png", "/view/Meditation.fxml");
        HBox pomodoroTimer = createMenuItem("Pomodoro Timer", "/resources/icons/clock.png", "/view/Pomodoro.fxml");

 
        sidebar.getChildren().addAll(logoContainer, planLabel, dashboard, calendar, achievement, task, toolsLabel, meditation, pomodoroTimer);

       
        selectedMenuItem = dashboard;
        ((Label) dashboard.getChildren().get(1)).setTextFill(Color.web("#4CAF50"));
        ((ImageView) dashboard.getChildren().get(0)).setImage(loadImage("/resources/icons/dashboard1.png"));
        dashboard.setStyle("-fx-background-color: #E8F5E9; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
        
        return sidebar;
    }


    private HBox selectedMenuItem;
    private HBox createMenuItem(String text, String iconPath, String fxmlPath) {
        ImageView icon = new ImageView(loadImage(iconPath));
        icon.setFitWidth(20);
        icon.setFitHeight(20);

        Label label = new Label(text);
        label.setFont(Font.font("Arial", 16));
        label.setTextFill(Color.BLACK);

        HBox menuItem = new HBox(10, icon, label);
        menuItem.setStyle("-fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");

   
        menuItem.setOnMouseEntered(e -> {
            if (menuItem != selectedMenuItem) {
                label.setTextFill(Color.web("#4CAF50"));
                menuItem.setStyle("-fx-background-color: #E8F5E9; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
                icon.setImage(loadImage(getIconPath(text, true)));
            }
        });
        menuItem.setOnMouseExited(e -> {
            if (menuItem != selectedMenuItem) {
                label.setTextFill(Color.BLACK);
                menuItem.setStyle("-fx-background-color: #ffffff; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
                icon.setImage(loadImage(getIconPath(text, false)));
            }
        });

       
        menuItem.setOnMouseClicked(e -> {
            if (selectedMenuItem != null) {
                Label prevLabel = (Label) selectedMenuItem.getChildren().get(1);
                ImageView prevIcon = (ImageView) selectedMenuItem.getChildren().get(0);

                prevLabel.setTextFill(Color.BLACK);
                prevIcon.setImage(loadImage(getIconPath(prevLabel.getText(), false)));
                selectedMenuItem.setStyle("-fx-background-color: #ffffff; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");
            }

            selectedMenuItem = menuItem;
            label.setTextFill(Color.web("#4CAF50"));
            icon.setImage(loadImage(getIconPath(text, true)));
            menuItem.setStyle("-fx-background-color: #E8F5E9; -fx-alignment: center-left; -fx-padding: 10; -fx-background-radius: 8;");

            if (fxmlPath != null) {
                loadContent(fxmlPath);
            }
        });

        return menuItem;
    }

    private static final Map<String, String> ICON_MAP = Map.of(
    	    "DashBoard", "/resources/icons/dashboard.png",
    	    "Calendar", "/resources/icons/calendar.png",
    	    "Achievement", "/resources/icons/achievement.png",
    	    "Task", "/resources/icons/task.png",
    	    "Meditation", "/resources/icons/cloud.png",
    	    "Pomodoro Timer", "/resources/icons/clock.png"
    	);

	private String getIconPath(String key, boolean selected) {
	    String base = ICON_MAP.getOrDefault(key, "/resources/icons/default.png");
	    return selected ? base.replace(".png", "1.png") : base;
	}

	private Image loadImage(String path) {
	    InputStream stream = getClass().getResourceAsStream(path);
	    if (stream == null) {
	        System.err.println("Failed to load resource: " + path);
	        return null;
	    }
	    return new Image(stream);
	}


    private void loadContent(String fxmlPath) {
        try {
        	System.out.println("Loading FXML View: " + fxmlPath);
          
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
            }else if (fxmlPath.equals("/view/Dashboard.fxml")) {
                    loader.setControllerFactory(param -> new DashboardController(ac));
            }else if (fxmlPath.equals("/view/ToToTask.fxml")) {
                loader.setControllerFactory(param ->taskController );
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
