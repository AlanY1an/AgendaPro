package model;

import java.util.Date;

public class Course extends Event{
	private String courseName;
    private String location;

    public Course(String eventName, Date startTime, Date endTime, String eventTag, User user, Reminder reminder,
                  String courseName, String location) {
        super(eventName, startTime, endTime, eventTag, user, reminder);
        this.courseName = courseName;
        this.location = location;
    }

    public void setName(String courseName) {
        this.courseName = courseName;
    }

    public String getName() {
        return courseName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void updateEvent(String eventName, Date startTime, Date endTime, String eventTag) {
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventTag = eventTag;
    }

    @Override
    public Event getEvent() {
        return this;
    }
}
