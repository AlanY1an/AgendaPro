package model;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private String taskName;
    private Date dueDate;
    private boolean finished;
    private String notes;

    // Constructor
    public Task() {
        this.taskName = "New Task"; // Default
        this.finished = false; // Default to not finished
    }

    public Task(String taskName, Date dueDate) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.finished = false; // Default to not finished
    }
    
    public Task(String taskName, Date dueDate, Boolean finished) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.finished = finished; 
    }
    
    public String getTaskName() {
		return taskName;
	}

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isFinished() {
        return finished;
    }
    
    public String getNote() {
		return notes;
	}

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isOnDate(LocalDate localDate) {
        LocalDate taskDate = LocalDate.ofInstant(dueDate.toInstant(), java.time.ZoneId.systemDefault());
        return taskDate.equals(localDate);
    }

	public void setTaskName(String name) {
		this.taskName = name;
	}

	public void setDueDate(Date dueDateValue) {
		this.dueDate = dueDateValue;
	}
	
	public void setNote(String note) {
		this.notes = note;
	}

}