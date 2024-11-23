package model;

import java.util.Date;

public class Entertainment extends Event{
	private String activityName;
    private String location;

    public Entertainment(String eventName, Date startTime, Date endTime, String eventTag, User user, Reminder reminder,
                         String activityName, String location) {
        super(eventName, startTime, endTime, eventTag, user, reminder);
        this.activityName = activityName;
        this.location = location;
    }

    public void setName(String activityName) {
        this.activityName = activityName;
    }

    public String getName() {
        return activityName;
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
