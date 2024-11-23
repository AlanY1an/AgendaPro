package model;

import java.util.Date;

public class Reading extends Event{
	private String content;
    private int pages;

    public Reading(String eventName, Date startTime, Date endTime, String eventTag, User user, Reminder reminder, 
                   String content, int pages) {
        super(eventName, startTime, endTime, eventTag, user, reminder);
        this.content = content;
        this.pages = pages;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPages() {
        return pages;
    }
}
