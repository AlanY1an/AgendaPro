package view;

import controller.AddEventDialogController;
import controller.EventController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Event;
import javafx.collections.ListChangeListener;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CalendarView {

    @FXML
    private Text dateText;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private MenuButton viewMenu;

    @FXML
    private MenuItem weeklyMenuItem;

    @FXML
    private MenuItem monthlyMenuItem;

    @FXML
    private AnchorPane centerPane;

    @FXML
    private Button addEventButton;
    
    @FXML
    private Button todayButton;
    
    private boolean isMonthlyView = true;


    private LocalDate selectedDate;
    private EventController eventController;
    
    private MonthlyView monthlyView;
    private WeeklyView weeklyView;


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);

    public CalendarView(EventController eventController) {
        this.eventController = eventController; 
    }

    @FXML
    public void initialize() {
        selectedDate = LocalDate.now();
        updateDateText();
        
        monthlyView = new MonthlyView(selectedDate, eventController);
        weeklyView = new WeeklyView(selectedDate, eventController);
        
        switchToMonthlyView();

        prevButton.setOnAction(e -> navigateDate(-1));
        nextButton.setOnAction(e -> navigateDate(1));
        weeklyMenuItem.setOnAction(e -> switchToWeeklyView());
        monthlyMenuItem.setOnAction(e -> switchToMonthlyView());
        addEventButton.setOnAction(e -> handleAddEvent());
        todayButton.setOnAction(e -> {
            selectedDate = LocalDate.now();
            if(isMonthlyView) {
            	switchToMonthlyView(); 
            }
            else {
            	switchToWeeklyView();
            }
        });
    }

    private void navigateDate(int offset) {
        if (isMonthlyView) {
            selectedDate = selectedDate.plusMonths(offset);
            switchToMonthlyView();
        } else {
            selectedDate = selectedDate.plusWeeks(offset);
            switchToWeeklyView();
        }
    }

    private void updateDateText() {
        dateText.setText(selectedDate.format(DATE_FORMATTER));
    }

    private void switchToWeeklyView() {
        isMonthlyView = false;
        viewMenu.setText("Weekly View");
        updateDateText();

        weeklyView.setDate(selectedDate);

        weeklyView.prefWidthProperty().bind(centerPane.widthProperty());
        weeklyView.prefHeightProperty().bind(centerPane.heightProperty());
        weeklyView.maxWidthProperty().bind(centerPane.widthProperty());
        weeklyView.maxHeightProperty().bind(centerPane.heightProperty());

        AnchorPane.setTopAnchor(weeklyView, 0.0);
        AnchorPane.setBottomAnchor(weeklyView, 0.0);
        AnchorPane.setLeftAnchor(weeklyView, 0.0);
        AnchorPane.setRightAnchor(weeklyView, 0.0);

        centerPane.getChildren().clear();
        centerPane.getChildren().add(weeklyView);
    }

    private void switchToMonthlyView() {
        isMonthlyView = true;
        viewMenu.setText("Monthly View");
        updateDateText();

        MonthlyView monthlyView = new MonthlyView(selectedDate, eventController);

        monthlyView.setDate(selectedDate); // 更新月视图的日期
        
        // 绑定宽高到 centerPane
        weeklyView.prefWidthProperty().bind(centerPane.widthProperty());
        weeklyView.prefHeightProperty().bind(centerPane.heightProperty());
        weeklyView.maxWidthProperty().bind(centerPane.widthProperty());
        weeklyView.maxHeightProperty().bind(centerPane.heightProperty());

        // 确保对齐
        AnchorPane.setTopAnchor(monthlyView, 0.0);
        AnchorPane.setBottomAnchor(monthlyView, 0.0);
        AnchorPane.setLeftAnchor(monthlyView, 0.0);
        AnchorPane.setRightAnchor(monthlyView, 0.0);

        centerPane.getChildren().clear();
        centerPane.getChildren().add(monthlyView);
    }


    @FXML
    private void handleAddEvent() {
        try {
        	System.out.println("Add Eventdialog opened");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEventDialog.fxml"));
            Parent root = loader.load();

            AddEventDialogController controller = loader.getController();
            controller.setEventController(eventController);

            Stage stage = new Stage();
            stage.setTitle("Add Event");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   

}
