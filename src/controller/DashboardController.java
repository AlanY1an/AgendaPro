package controller;

import java.io.BufferedReader;
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
import java.util.List;
import java.util.Locale;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import view.Clock;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONObject;


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
    
    @FXML
    public void initialize() {
        System.out.println("Dashboard Controller initialized");
        // Greeting
        LocalTime now = LocalTime.now();
        String greeting = getGreeting(now);
        greetingLabel.setText(greeting);
        
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
}
