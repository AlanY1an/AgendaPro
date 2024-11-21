package model;

import java.time.LocalDate;

public class Event {

    public static final String[] CATEGORIES = { "Work", "Study", "Exercise", "Entertainment" };

    private int id; // 事件ID
    private String title; // 标题
    private String category; // 分类：Work, Study, Exercise, Entertainment
    private String description; // 描述
    private LocalDate date; // 日期
    private int duration;

    // Constructor for creating a CalendarEvent
    public Event(int id, String title, String category, String description, LocalDate date, int duration) {
        this.id = id;
        this.title = title;
        this.category = isValidCategory(category) ? category : "Work"; // 默认分类为 Work
        this.description = description;
        this.date = date;
        this.duration = duration;
    }

    // 验证分类是否合法
    private boolean isValidCategory(String category) {
        for (String validCategory : CATEGORIES) {
            if (validCategory.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    // Getter 和 Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (isValidCategory(category)) {
            this.category = category;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public int getDuration() { 
        return duration;
    }

    public void setDuration(int duration) { 
        this.duration = duration;
    }

    // 检查事件是否发生在指定日期
    public boolean isOnDate(LocalDate localDate) {
        return date.equals(localDate);
    }

    // 打印事件信息
    @Override
    public String toString() {
        return "CalendarEvent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", duration=" + duration + " seconds" + 
                '}';
             
    }
}
