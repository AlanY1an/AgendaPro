package view;

import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

public class MonthlyView extends GridPane {

    private static final int NUM_ROWS = 7; 
    private static final int NUM_COLS = 7;

    private LocalDate selectedDate;
    private LocalDate today;

    public MonthlyView(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        this.today = LocalDate.now();
        this.setStyle("-fx-background-color: white;");

        for (int i = 0; i < NUM_COLS; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / NUM_COLS);
            this.getColumnConstraints().add(col);
        }
        for (int i = 0; i < NUM_ROWS; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / NUM_ROWS);
            this.getRowConstraints().add(row);
        }
        addWeekHeaders();
        populateMonth();
    }

    
    private void addWeekHeaders() {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int col = 0; col < NUM_COLS; col++) {
            StackPane cell = new StackPane();
            cell.setStyle("-fx-border-color: lightgray; -fx-background-col or: #F9F9F9;");
            Text dayText = new Text(daysOfWeek[col]);
            dayText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
            cell.getChildren().add(dayText);
            cell.setAlignment(Pos.CENTER);
            this.add(cell, col, 0); // in 0th row
        }
    }

    private void populateMonth() {
        this.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0); // remove
        YearMonth yearMonth = YearMonth.from(selectedDate); 
        //System.out.print(yearMonth);
 
        LocalDate firstDayOfMonth = yearMonth.atDay(1); 
        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek(); // 获取第一天是星期几

        int firstColumn = (firstDayOfWeek.getValue() + 6) % 7; // 0 is Monday so (1+6) %7 works
        int daysInMonth = yearMonth.lengthOfMonth(); // get number of days 31/30

        // month of date before 
        LocalDate previousMonth = yearMonth.minusMonths(1).atEndOfMonth();
        for (int i = firstColumn - 1; i >= 0; i--) {
            StackPane cell = createDayCell(previousMonth, "lightgray");
            this.add(cell, i, 1);
            previousMonth = previousMonth.minusDays(1);
        }

        // date of month
        LocalDate currentDate = firstDayOfMonth;
        for (int day = 1; day <= daysInMonth; day++) {
            int row = (firstColumn + day - 1) / NUM_COLS + 1; 
            int col = (firstColumn + day - 1) % NUM_COLS; // 计算index
            StackPane cell = createDayCell(currentDate, "black");
            this.add(cell, col, row);
            currentDate = currentDate.plusDays(1);
        }

        // month of date after
        LocalDate nextMonth = yearMonth.plusMonths(1).atDay(1);
        int totalDaysDisplayed = firstColumn + daysInMonth;
        for (int i = totalDaysDisplayed; i < (NUM_ROWS - 1) * NUM_COLS; i++) {
            int row = i / NUM_COLS + 1; // 0 row is header so + 1
            int col = i % NUM_COLS;
            StackPane cell = createDayCell(nextMonth, "lightgray");
            this.add(cell, col, row);
            nextMonth = nextMonth.plusDays(1);
        }
    }

    // this generate the numebr of the day
    private StackPane createDayCell(LocalDate date, String textColor) {
        StackPane cell = new StackPane();
        cell.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");

        Text dayText = new Text(String.valueOf(date.getDayOfMonth()));
        dayText.setStyle("-fx-font-size: 14; -fx-fill: " + textColor + ";");

        if (date.equals(today)) {
            cell.setStyle("-fx-border-color: blue; -fx-background-color: lightblue; -fx-padding: 5;");
        }

        cell.getChildren().add(dayText);
        cell.setAlignment(Pos.CENTER);
        return cell;
    }
}
