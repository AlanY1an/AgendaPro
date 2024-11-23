package model;

import java.util.Date;

public class Workout extends Event {
    private String activityType;
    private String location;

    public Workout(String eventName, Date startTime, Date endTime, String eventTag, User user, Reminder reminder,
                   String activityType, String location) {
        super();
        this.activityType = activityType;
        this.location = location;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityType() {
        return activityType;
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
