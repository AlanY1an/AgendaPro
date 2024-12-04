package view;

import controller.EventController;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
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

        VBox content = new VBox(2);
        content.setAlignment(Pos.TOP_LEFT);
        content.setPadding(new Insets(3));
        content.setMaxHeight(60); // 限制格子内容最大高度

        // 添加日期文本
        Text dayText = new Text(String.valueOf(date.getDayOfMonth()));
        dayText.setStyle("-fx-font-size: 12; -fx-fill: " + textColor + ";");
        content.getChildren().add(dayText);

        // 获取当天的事件列表
        List<Event> eventList = eventController.getEventsOnDate(date);
        int maxVisibleEvents = 2; // 最多显示的事件数量

        // 显示前两个事件
        for (int i = 0; i < Math.min(eventList.size(), maxVisibleEvents); i++) {
            Event event = eventList.get(i);

            // 限制标题长度为20个字符，多余部分显示省略号
            String truncatedTitle = event.getTitle().length() > 18
                ? event.getTitle().substring(0, 18) + "..." 
                : event.getTitle();

            Text eventText = new Text(truncatedTitle);
            eventText.setStyle("-fx-font-size: 10; -fx-fill: gray;");
            content.getChildren().add(eventText);
        }

        // 如果有更多事件，显示 "+N more"
        if (eventList.size() > maxVisibleEvents) {
            Text moreText = new Text("+" + (eventList.size() - maxVisibleEvents) + " more");
            moreText.setStyle("-fx-font-size: 10; -fx-fill: red;");
            content.getChildren().add(moreText);
        }

        // 点击事件，显示详情弹窗
        cell.setOnMouseClicked(event -> showEventDetails(date));

        // 高亮今天
        if (date.equals(today)) {
            cell.setStyle("-fx-border-color: #388E3C; -fx-border-width: 3; -fx-padding: 5; -fx-background-color: #E8F5E9;");
        }

        cell.getChildren().add(content);
        return cell;
    }


    // 显示事件详情弹窗
    private void showEventDetails(LocalDate date) {
        List<Event> eventList = eventController.getEventsOnDate(date);
        StringBuilder details = new StringBuilder("Events on " + date + ":\n\n");
        
        for (Event event : eventList) {
            details.append("Title: ").append(event.getTitle()).append("\n")
                   .append("Category: ").append(event.getCategory()).append("\n")
                   .append("Description: ").append(event.getDescription() != null ? event.getDescription() : "No Description").append("\n");

            // 如果是冥想事件或有时长信息，额外添加
            if (event.getMeditationMinutes() > 0) {
                details.append("Meditation Minutes: ").append(event.getMeditationMinutes()).append("\n");
            }
            if (event.getDuration() > 0) {
                details.append("Duration: ").append(event.getDuration()).append(" minutes\n");
            }

            // 显示是否已完成
            details.append("Finished: ").append(event.isFinished() ? "Yes" : "No").append("\n\n");
        }

        // 使用 Alert 显示详细信息
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Details");
        alert.setHeaderText("Details for " + date);
        alert.setContentText(details.toString());
        alert.showAndWait();
    }



}
