package view;

import controller.EventController;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import model.Event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class MonthlyView extends GridPane {

    private static final int NUM_ROWS = 7; // 1 week + 6 date
    private static final int NUM_COLS = 7; // 7 days

    private LocalDate selectedDate;
    private LocalDate today;
    private EventController eventController;

    public MonthlyView(LocalDate selectedDate, EventController eventController) {
        this.selectedDate = selectedDate;
        this.today = LocalDate.now();
        this.eventController = eventController;

        setStyle("-fx-background-color: white;");
        configureGrid();
        addWeekHeaders();
        populateMonth();
        
        eventController.getAllEvents().addListener((ListChangeListener<Event>) change -> {
            while (change.next()) {
                populateMonth(); // 刷新月视图
            }
        });
    }

    public void setDate(LocalDate newDate) {
        this.selectedDate = newDate;
        populateMonth(); // 重新填充月视图
    }

    
    /**
     * define the rows and columns
     */
    private void configureGrid() {
        for (int i = 0; i < NUM_COLS; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS); // 列水平增长
            col.setMinWidth(0); // 最小宽度
            col.prefWidthProperty().bind(this.widthProperty().divide(NUM_COLS)); // 动态绑定宽度
            this.getColumnConstraints().add(col);
        }

        for (int i = 0; i < NUM_ROWS; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS); // 行垂直增长
            row.setMinHeight(0); // 最小高度
            row.prefHeightProperty().bind(this.heightProperty().divide(NUM_ROWS)); // 动态绑定高度
            this.getRowConstraints().add(row);
        }
    }


    /**
     * add the day of the week
     */
    private void addWeekHeaders() {
        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int col = 0; col < NUM_COLS; col++) {
            StackPane cell = new StackPane();
            cell.setStyle("-fx-border-color: lightgray; -fx-background-color: #F9F9F9;");
            Text dayText = new Text(daysOfWeek[col]);
            dayText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
            cell.getChildren().add(dayText);
            cell.setAlignment(Pos.CENTER);
            this.add(cell, col, 0);
        }
    }

    /**
     * Fill the date of the Month
     */
    private void populateMonth() {
        // 清除所有内容，包括第 0 行的星期标题
        this.getChildren().clear();

        // 重新添加星期标题
        addWeekHeaders();
        
        YearMonth yearMonth = YearMonth.from(selectedDate);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();

        int firstColumn = (firstDayOfWeek.getValue() + 6) % 7; // 0 is Monday
        int daysInMonth = yearMonth.lengthOfMonth();

        // last
        LocalDate previousMonth = yearMonth.minusMonths(1).atEndOfMonth();
        for (int i = firstColumn - 1; i >= 0; i--) {
            StackPane cell = createDayCell(previousMonth, "lightgray");
            this.add(cell, i, 1);
            previousMonth = previousMonth.minusDays(1);
        }

        // current
        LocalDate currentDate = firstDayOfMonth;
        for (int day = 1; day <= daysInMonth; day++) {
            int row = (firstColumn + day - 1) / NUM_COLS + 1;
            int col = (firstColumn + day - 1) % NUM_COLS;
            StackPane cell = createDayCell(currentDate, "black");
            this.add(cell, col, row);
            currentDate = currentDate.plusDays(1);
        }

        // next
        LocalDate nextMonth = yearMonth.plusMonths(1).atDay(1);
        int totalDaysDisplayed = firstColumn + daysInMonth;
        for (int i = totalDaysDisplayed; i < (NUM_ROWS - 1) * NUM_COLS; i++) {
            int row = i / NUM_COLS + 1;
            int col = i % NUM_COLS;
            StackPane cell = createDayCell(nextMonth, "lightgray");
            this.add(cell, col, row);
            nextMonth = nextMonth.plusDays(1);
        }
    }

    /**
     * Create a data cell
     */
    private StackPane createDayCell(LocalDate date, String textColor) {
        StackPane cell = new StackPane();
        cell.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");

        VBox content = new VBox(5);
        content.setAlignment(Pos.TOP_LEFT);
        content.setPadding(new Insets(5));

        Text dayText = new Text(String.valueOf(date.getDayOfMonth()));
        dayText.setStyle("-fx-font-size: 12; -fx-fill: " + textColor + ";");
        content.getChildren().add(dayText);

        // get event titles
        List<Event> eventList = eventController.getEventsOnDate(date);
        for (Event event : eventList) {
            Text eventText = new Text(event.getTitle());
            eventText.setStyle("-fx-font-size: 10; -fx-fill: gray;");
            content.getChildren().add(eventText);
        }

        // hightlight today
        if (date.equals(today)) {
        	cell.setStyle("-fx-border-color: #388E3C; -fx-border-width: 3; -fx-padding: 5; -fx-background-color: #E8F5E9;");
        }

        cell.getChildren().add(content);
        return cell;
    }

}
