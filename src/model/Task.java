package model;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private String taskName;
    private Date dueDate;
    private Reminder reminder;
    private boolean finished;

    // Constructor
    public Task(String taskName, Date dueDate, Reminder reminder) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.reminder = reminder;
        this.finished = false; // Default to not finished
    }

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    // Other getters and setters ...

    public boolean isOnDate(LocalDate localDate) {
        LocalDate taskDate = LocalDate.ofInstant(dueDate.toInstant(), java.time.ZoneId.systemDefault());
        return taskDate.equals(localDate);
    }
}
