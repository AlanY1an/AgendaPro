package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.Achievements;
import view.Clock;
import java.util.Random;
import java.util.Scanner;
import java.util.prefs.Preferences;



public class DashboardController {

	// Clock Component
    @FXML
    private AnchorPane clockAnchorPane;
    
    // Inspiration Component
    @FXML
    private Label inspirationLabel;
    
    // Greeting Component
    @FXML
    private Label greetingLabel;
    
    // Little Calendar Component
    @FXML
    private AnchorPane todayCalendarPane;
    @FXML
    private Label dateLabel;
    @FXML
    private GridPane miniCalendarGrid;
    
    // Weather Component
    @FXML
    private StackPane weatherIcon;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label weatherLabel;
    @FXML
    private ProgressIndicator processBar;
    @FXML
    private Label finishedeEvents;
    @FXML
    private Label allEvents;
    @FXML
    private Label focusTimeToday;
    @FXML
    private Label MeditationTimeToday;
    
    // Todo List Component
    @FXML
    private VBox taskContainer;
    private static final String TASK_FILE_PATH = "tasks.txt";
    
    
    // Achievement Component
    private Achievements achievements;
    
    // Note
    @FXML
    private TextArea memoArea;
    
    // Image
    @FXML
    private ImageView focusImage;
    @FXML
    private ImageView meditationImage;
    
    private static final String API_KEY = "78ee7cedb6926adb9593055fb371ce53";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=42.43&lon=71.06&appid=" + API_KEY + "&units=imperial";
    private static boolean isWeatherDataFetched = false;
    private static String cachedTemperature = null;
    private static String cachedWeatherDescription = null;
    private static String cachedIconCode = null;

    
    private final List<String> quotes = List.of(
            "Believe in yourself!",
            "Stay positive, work hard, make it happen.",
            "Your limitation—it's only your imagination.",
            "Push yourself, because no one else is going to do it for you.",
            "Dream it. Wish it. Do it.",
            "Success doesn’t just find you. You have to go out and get it.",
            "The harder you work for something, the greater you’ll feel when you achieve it.",
            "Don’t stop when you’re tired. Stop when you’re done."
    );
    
    
    public DashboardController(Achievements as) {
   	 this.achievements=as;
   	 
   }
    @FXML
    public void initialize() {
        System.out.println("Dashboard Controller initialized");
        // Image
        Image image1 = new Image(getClass().getResource("/resources/focus.png").toExternalForm());
        focusImage.setImage(image1);
        Image image2 = new Image(getClass().getResource("/resources/meditation.png").toExternalForm());
        meditationImage.setImage(image2);
        
        // Greeting
        LocalTime now = LocalTime.now();
        String greeting = getGreeting(now);
        greetingLabel.setText(greeting);
        
        // Note
        initializeMemo();
        
        // Inspiration Label
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateInspirationText()),
                new KeyFrame(Duration.seconds(5)) // 每 5 秒更新一次
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        
        // Clock
        assert clockAnchorPane != null : "clockAnchorPane is null!";
        Clock clock = new Clock();
        VBox clockView = clock.createClock();
        // 确保 VBox 填充整个 AnchorPane
        AnchorPane.setTopAnchor(clockView, 0.0);
        AnchorPane.setBottomAnchor(clockView, 0.0);
        AnchorPane.setLeftAnchor(clockView, 0.0);
        AnchorPane.setRightAnchor(clockView, 0.0);
        clockAnchorPane.getChildren().add(clockView);
        
        
        // Little Calendar
        LocalDate currentDate = LocalDate.now();
        dateLabel.setText(currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentDate.getYear());
        populateMiniCalendar(currentDate);
        
        // fetchWeatherData();
        if (!isWeatherDataFetched) {
            fetchWeatherData();
        } else {
            updateWeatherUI();
        }
        
        // taskList
        if (taskContainer != null) {
         taskContainer.getChildren().clear();
        }
        loadTasksForDate(currentDate);
        
        //achievement pane
        processBar.setStyle("-fx-stroke-width: 6px;-fx-background-image: url('../resources/icons/achievement.png');-fx-background-size:cover;-fx-background-position: center;");
        populateAchievementsAndMeditation();
    }
     
	private void populateAchievementsAndMeditation() {
		 int eventsOnToday = achievements.countAllEventsOnCurrentDate();
    	 int eventsFinishedOnToday = achievements.countFinishedEventsOnCurrentDate();
         int maxTasks = 10; // Assuming 100 is the maximum for normalization

         // Safeguard progress values between 0 and 1
         processBar.setProgress((double)eventsFinishedOnToday/eventsOnToday);
         finishedeEvents.setText(String.valueOf(eventsFinishedOnToday));
         allEvents.setText(String.valueOf(eventsOnToday));
    	
         int focusTimeOnToday = achievements.countFocusTimeOnCurrentDate();
    	 int meditationTimeOnToday = achievements.getTotalMeditationMinutesToday();
    	 
    	 focusTimeToday.setText(String.valueOf(focusTimeOnToday));
    	 MeditationTimeToday.setText(String.valueOf(meditationTimeOnToday));
		
	}
	// Weather Methods
    private void fetchWeatherData() {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                String jsonResponse = new Scanner(inputStream).useDelimiter("\\A").next();
                inputStream.close();

                parseWeatherData(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    temperatureLabel.setText("Error fetching weather");
                    weatherLabel.setText("Error fetching weather");
                });
            }
        }).start();
    }

    private void parseWeatherData(String jsonResponse) {
        try {
            String temperatureKey = "\"temp\":";
            String descriptionKey = "\"description\":\"";
            String iconKey = "\"icon\":\"";

            cachedTemperature = String.format("%.1f°F", extractDoubleValue(jsonResponse, temperatureKey));
            cachedWeatherDescription = "Mostly " + extractStringValue(jsonResponse, descriptionKey);
            cachedIconCode = extractStringValue(jsonResponse, iconKey);

            isWeatherDataFetched = true;

            Platform.runLater(this::updateWeatherUI);

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                temperatureLabel.setText("Error parsing weather");
                weatherLabel.setText("Error parsing weather");
            });
        }
    }

    
    private void updateWeatherUI() {
        if (cachedTemperature != null && cachedWeatherDescription != null && cachedIconCode != null) {
            temperatureLabel.setText(cachedTemperature);
            weatherLabel.setText(cachedWeatherDescription);
            updateWeatherIcon(cachedIconCode);
        }
    }

    private double extractDoubleValue(String jsonResponse, String key) {
        int startIndex = jsonResponse.indexOf(key) + key.length();
        int endIndex = jsonResponse.indexOf(",", startIndex);
        return Double.parseDouble(jsonResponse.substring(startIndex, endIndex).trim());
    }

    private String extractStringValue(String jsonResponse, String key) {
        int startIndex = jsonResponse.indexOf(key) + key.length();
        int endIndex = jsonResponse.indexOf("\"", startIndex);
        return jsonResponse.substring(startIndex, endIndex);
    }

    private void updateWeatherIcon(String iconCode) {
        try {
            // Construct the icon URL
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
            Image image = new Image(iconUrl);
            // Display the icon in the AnchorPane
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            
            weatherIcon.getChildren().clear();
            weatherIcon.getChildren().add(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // MiniCalendar Methods
    private void populateMiniCalendar(LocalDate currentDate) {
        // clear
        miniCalendarGrid.getChildren().clear();

        // get info
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 周一=1, 周日=7

        String[] weekDays = {"  M", "  T", "  W", "  Th", "  F", "  S", "  S"};
        for (int i = 0; i < weekDays.length; i++) {
            Label dayLabel = new Label(weekDays[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gray;");
            miniCalendarGrid.add(dayLabel, i, 0);
        }

        // last month
        YearMonth previousMonth = yearMonth.minusMonths(1);
        LocalDate lastDayOfPreviousMonth = previousMonth.atEndOfMonth();
        int lastDayOfPreviousMonthDate = lastDayOfPreviousMonth.getDayOfMonth();
        int daysToFill = firstDayOfWeek - 1; // 需要填充的空白天数
        for (int i = daysToFill - 1; i >= 0; i--) {
            Label dayLabel = new Label(String.valueOf(lastDayOfPreviousMonthDate - i));
            dayLabel.setStyle("-fx-padding: 5px; -fx-alignment: center; -fx-font-size: 14px; -fx-text-fill: lightgray; -fx-font-family: 'Trebuchet MS';");
            miniCalendarGrid.add(dayLabel, daysToFill - i - 1, 1); // 放入第一行的空白处
        }

        // current month
        int row = 1;
        int col = daysToFill;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDay = yearMonth.atDay(day);

            StackPane dayCell = new StackPane();
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setStyle("-fx-padding: 5px; -fx-alignment: center; -fx-font-size: 14px; -fx-text-fill: black; -fx-font-family: 'Trebuchet MS';");

            if (currentDay.equals(LocalDate.now())) {
                Circle highlightCircle = new Circle(15, Color.rgb(0, 128, 0, 0.2)); 
                dayCell.getChildren().add(highlightCircle);
            }

            dayCell.getChildren().add(dayLabel);
            miniCalendarGrid.add(dayCell, col, row);

            col++;
            if (col > 6) { 
                col = 0;
                row++;
            }
        }

        // next month
        int nextMonthDaysToFill = 7 - col; 
        LocalDate firstDayOfNextMonth = yearMonth.plusMonths(1).atDay(1);
        for (int i = 1; i <= nextMonthDaysToFill; i++) {
            Label dayLabel = new Label(String.valueOf(i));
            dayLabel.setStyle("-fx-padding: 5px; -fx-alignment: center; -fx-font-size: 14px; -fx-text-fill: lightgray; -fx-font-family: 'Trebuchet MS';");
            miniCalendarGrid.add(dayLabel, col, row);
            col++;
        }
    }

    // Greeting Methods
	private String getGreeting(LocalTime time) {
        if (time.isBefore(LocalTime.NOON)) {
            return "Hello, Good Morning!";
        } else if (time.isBefore(LocalTime.of(18, 0))) {
            return "Hello, Good Afternoon!";
        } else {
            return "Hello, Good Evening!";
        }
    }
    
	// Inspiration Methods
    private void updateInspirationText() {
        Random random = new Random();
        String newQuote = quotes.get(random.nextInt(quotes.size()));
        inspirationLabel.setText(newQuote);
    }
    
    // Notebook
    private void initializeMemo() {
        // 获取之前保存的内容
        Preferences prefs = Preferences.userNodeForPackage(DashboardController.class);
        String savedMemo = prefs.get("dashboardMemo", "");
        memoArea.setText(savedMemo);

        // 初始设置为不可编辑
        memoArea.setEditable(false);

        Platform.runLater(() -> {
            if (savedMemo.isEmpty()) {
                memoArea.setPromptText("Feel free to write any thoughts or notes here...");
            }
            memoArea.getParent().requestFocus();
        });

        // 文本框的点击事件 - 保持这个，这很重要
        memoArea.setOnMouseClicked(event -> {
            memoArea.setEditable(true);
            event.consume();
        });

        // Scene级别的事件处理
        Platform.runLater(() -> {
            Scene scene = memoArea.getScene();
            if (scene != null) {
                scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    // 如果点击的不是文本框
                    if (event.getTarget() != memoArea) {
                        memoArea.setEditable(false);
                        scene.getRoot().requestFocus();
                    }
                });
            }
        });

        // 保存内容
        memoArea.textProperty().addListener((observable, oldValue, newValue) -> {
            prefs.put("dashboardMemo", newValue);
        });
    }
    
 // Task loading
    @FXML
    public void loadTasksForDate(LocalDate date) {
        taskContainer.getChildren().clear(); // Clear existing tasks

        try (BufferedReader reader = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
            String line;
            boolean tasksFound = false; // Flag to check if there are tasks for the given date

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String taskName = parts[0];  // Task name
                    LocalDate taskDueDate = LocalDate.parse(parts[1]);  // Parse due date
                    boolean isFinished = Boolean.parseBoolean(parts[2]);  // Task finished status
                    String notes = parts[3].replace("\\n", "\n");  // Task notes, handling escaped newlines
                    
                    // Check if the task's due date matches the specified date
                    if (taskDueDate.equals(date)) {
                        tasksFound = true;
                        
                        // Create a checkbox for the task
                        CheckBox taskCheckBox = new CheckBox(taskName);
                        taskCheckBox.setSelected(isFinished);
                        taskCheckBox.setStyle(
                            "-fx-font-size: 16px; " + 
                            "-fx-background-color: white; " +
                            "-fx-border-color: white; " +
                            "-fx-mark-color: #4CAF50;" +
                            "-fx-stroke-width: 3;"
                        );

                        // Add a listener to update the task status when the checkbox is selected/deselected
                        taskCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            updateTaskStatusInFile(taskName, newVal);
                        });

                        // Add checkbox to task container
                        taskContainer.getChildren().add(taskCheckBox);

                        // Add a Separator between tasks (if necessary)
                        Separator separator = new Separator();
                        separator.setStyle("-fx-background-color: #cccccc; -fx-opacity: 0.5;");
                        taskContainer.getChildren().add(separator);
                    }
                }
            }

            // If no tasks were found for the given date, display a message
            if (!tasksFound) {
                Label noTasksLabel = new Label("No tasks for today. Enjoy your time!");
                taskContainer.getChildren().add(noTasksLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error loading tasks. Please try again.");
            taskContainer.getChildren().add(errorLabel);
        }
    }

    
    // Update if finished
    private void updateTaskStatusInFile(String taskName, boolean isFinished) {
     try {
            // Read all lines from the file
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        String currentTaskName = parts[0];
                        if (currentTaskName.equals(taskName)) {
                            // Update the finished status
                            parts[2] = String.valueOf(isFinished);
                        }
                        lines.add(String.join("|", parts));
                    }
                }
            }

            // Write the updated lines back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_FILE_PATH))) {
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
         e.printStackTrace();
        }
    }
    
    
}
