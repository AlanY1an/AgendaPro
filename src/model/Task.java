package model;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private String taskName;
    private Date dueDate;
    private boolean finished;

    // Constructor
    public Task(String taskName, Date dueDate) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.finished = false; // Default to not finished
    }

    public Task(String string, Date dueDate, boolean b) {
		// TODO Auto-generated constructor stub
    	this.taskName=string;
        this.dueDate=dueDate;
    	this.finished =b;
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

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isOnDate(LocalDate localDate) {
        LocalDate taskDate = LocalDate.ofInstant(dueDate.toInstant(), java.time.ZoneId.systemDefault());
        return taskDate.equals(localDate);
    }

	public void setTaskName(String name) {
		this.taskName = taskName;
	}

	public void setDueDate(Date dueDateValue) {
		this.dueDate = dueDate;
	}

}
