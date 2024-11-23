package model;

import java.util.Date;

public class Meeting extends Event{
	private String meetContent;
    private String meetLink;

    public Meeting(String eventName, Date startTime, Date endTime, String eventTag, User user, Reminder reminder, 
                   String meetContent, String meetLink) {
        super(eventName, startTime, endTime, eventTag, user, reminder);
        this.meetContent = meetContent;
        this.meetLink = meetLink;
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
    
    //CAN CAHNGE TO OVERRIDE
    public void addMeetContent(String meetContent) {
        this.meetContent = meetContent;
    }

    public String getMeetLink() {
        return meetLink;
    }
}
