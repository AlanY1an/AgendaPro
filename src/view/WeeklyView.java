package view;

import controller.EventController;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class WeeklyView extends GridPane {

    private static final int NUM_DAYS = 7;
    private LocalDate startOfWeek; 
    private LocalDate today;
    private EventController eventController;

    public WeeklyView(LocalDate selectedDate, EventController eventController) {
        this.startOfWeek = selectedDate.with(DayOfWeek.MONDAY); 
        this.today = LocalDate.now();
        this.eventController = eventController;
        this.setStyle("-fx-background-color: white;");

        setupGrid();
        populateWeek();
    }

    private void setupGrid() {
        for (int i = 0; i < NUM_DAYS; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / NUM_DAYS);
            this.getColumnConstraints().add(col);
        }


        RowConstraints rowDate = new RowConstraints();
        rowDate.setPercentHeight(20); // 日期行占20%高度
        this.getRowConstraints().add(rowDate);

        RowConstraints rowEvents = new RowConstraints();
        rowEvents.setPercentHeight(80); // 事件行占80%高度
        this.getRowConstraints().add(rowEvents);
    }

    private void populateWeek() {
        // 遍历一周的日期
        LocalDate currentDate = startOfWeek;

        for (int day = 0; day < NUM_DAYS; day++) {
            // 创建日期单元格
            StackPane dateCell = createDateCell(currentDate);
            this.add(dateCell, day, 0); // 添加到第一行

            // 创建事件单元格
            StackPane eventCell = createEventCell(currentDate);
            this.add(eventCell, day, 1); // 添加到第二行

            currentDate = currentDate.plusDays(1); // 移动到下一天
        }
    }

    private StackPane createDateCell(LocalDate date) {
        StackPane cell = new StackPane();
        cell.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");

        Text dayText = new Text(date.getDayOfWeek().toString().substring(0, 3));
        dayText.setStyle("-fx-font-size: 12; -fx-fill: gray;");

        Text dateText = new Text(String.valueOf(date.getDayOfMonth()));
        dateText.setStyle("-fx-font-size: 18; -fx-fill: black;");

        if (date.equals(today)) {
        	cell.setStyle("-fx-border-color: #388E3C; -fx-border-width: 3; -fx-padding: 5; -fx-background-color: #E8F5E9;");
        }

        VBox dateBox = new VBox(dayText, dateText);
        dateBox.setAlignment(Pos.CENTER);
        cell.getChildren().add(dateBox);

        return cell;
    }

    private StackPane createEventCell(LocalDate date) {
        StackPane cell = new StackPane();
        cell.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");

        VBox content = new VBox(5);
        content.setAlignment(Pos.TOP_CENTER);

        List<Event> events = eventController.getEventsOnDate(date);
        if (events.isEmpty()) {
            Text noEventText = new Text("No Events");
            noEventText.setStyle("-fx-font-size: 10; -fx-fill: gray;");
            content.getChildren().add(noEventText);
        } else {
            for (Event event : events) {
                VBox eventItem = createEventItem(event);
                content.getChildren().add(eventItem);
            }
        }

        cell.getChildren().add(content);
        return cell;
    }

    private VBox createEventItem(Event event) {
        VBox eventBox = new VBox();
        eventBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 5;");
        eventBox.setSpacing(2);

        Text eventTitle = new Text(event.getTitle());
        eventTitle.setStyle("-fx-font-size: 12; -fx-fill: black;");

        // 根据事件类别设置颜色
        Text eventCategory = new Text(event.getCategory());
        eventCategory.setStyle("-fx-font-size: 10; -fx-fill: " + getCategoryColor(event.getCategory()) + ";");

        eventBox.getChildren().addAll(eventTitle, eventCategory);
        return eventBox;
    }

    private String getCategoryColor(String category) {
        switch (category) {
            case "Work":
                return "#4C95CE";
            case "Study":
                return "#81C457";
            case "Exercise":
                return "#F7C531";
            case "Entertainment":
                return "#E85569";
            default:
                return "#B2B2B2"; 
        }
    }
}
