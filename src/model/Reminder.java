package model;

import java.util.Date;

public class Reminder {
	private String message;
	private Date startTime;
    private Date endTime;

    public Reminder(String message, Date startTime, Date endTime) {
        this.message = message;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter and Setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and Setter for time
    public void addReminder(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void deleteReminder() {
        this.startTime = null;
        this.endTime = null;
    }

    public void updateReminder(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    @Override
    public String toString() {
        return "Reminder{" +
                "message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
