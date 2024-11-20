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
    
    private boolean isMonthlyView = true; // control cur view


    private LocalDate selectedDate;
    private EventController eventController;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);

    public CalendarView() {
        this.eventController = new EventController(); 
    }

    @FXML
    public void initialize() {
        selectedDate = LocalDate.now();
        updateDateText();

        switchToMonthlyView();

        prevButton.setOnAction(e -> navigateDate(-1));
        nextButton.setOnAction(e -> navigateDate(1));
        weeklyMenuItem.setOnAction(e -> switchToWeeklyView());
        monthlyMenuItem.setOnAction(e -> switchToMonthlyView());
        addEventButton.setOnAction(e -> handleAddEvent());
        todayButton.setOnAction(e -> {
            selectedDate = LocalDate.now();
            switchToMonthlyView(); 
        });
    }

    private void navigateDate(int offset) {
        selectedDate = selectedDate.plusMonths(offset);
        switchToMonthlyView();
    }

    private void updateDateText() {
        dateText.setText(selectedDate.format(DATE_FORMATTER));
    }

    private void switchToWeeklyView() {
    	isMonthlyView = false;
        viewMenu.setText("Weekly View");
        updateDateText();

        WeeklyView weeklyView = new WeeklyView(selectedDate, eventController);
        weeklyView.prefWidthProperty().bind(centerPane.widthProperty());
        weeklyView.prefHeightProperty().bind(centerPane.heightProperty());

        centerPane.getChildren().clear();
        centerPane.getChildren().add(weeklyView);
    }

    private void switchToMonthlyView() {
    	isMonthlyView = true;
        viewMenu.setText("Monthly View");
        updateDateText();

        MonthlyView monthlyView = new MonthlyView(selectedDate, eventController);
        monthlyView.prefWidthProperty().bind(centerPane.widthProperty());
        monthlyView.prefHeightProperty().bind(centerPane.heightProperty());

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
            
            // Refresh the view
            controller.setCallback(() -> {
                System.out.println("Event added. Refreshing View...");
                refreshCurrentView();
            });

            Stage stage = new Stage();
            stage.setTitle("Add Event");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void refreshCurrentView() {
        if (isMonthlyView) {
            switchToMonthlyView();
        } else {
            switchToWeeklyView();
        }
    }

}
