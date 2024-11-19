package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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

    private LocalDate selectedDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);

    @FXML
    public void initialize() {
        selectedDate = LocalDate.now();
        updateDateText();
        
        this.switchToMonthlyView();

        prevButton.setOnAction(e -> navigateDate(-1));
        nextButton.setOnAction(e -> navigateDate(1));
        weeklyMenuItem.setOnAction(e -> switchToWeeklyView());
        monthlyMenuItem.setOnAction(e -> switchToMonthlyView());
    }

    private void navigateDate(int offset) {
        selectedDate = selectedDate.plusMonths(offset); // change month
        switchToMonthlyView(); 
    }

    private void updateDateText() {
        dateText.setText(selectedDate.format(DATE_FORMATTER));
    }

    private void switchToWeeklyView() {
        viewMenu.setText("Weekly View");
        updateDateText(); // change date as changing view
        // load WeeklyView
        centerPane.getChildren().clear();
        centerPane.getChildren().add(new WeeklyView());
    }


    private void switchToMonthlyView() {
        viewMenu.setText("Monthly View");
        updateDateText(); // Update date on lefttop

        MonthlyView monthlyView = new MonthlyView(selectedDate);
        monthlyView.prefWidthProperty().bind(centerPane.widthProperty());
        monthlyView.prefHeightProperty().bind(centerPane.heightProperty());

        centerPane.getChildren().clear();
        centerPane.getChildren().add(monthlyView);
    }




}
