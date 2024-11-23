package model;

import java.util.Date;

public class Meditation extends Event {
    private String timeSlot;

    public Meditation(String eventName, Date startTime, Date endTime, String eventTag, User user, Reminder reminder,
                      String timeSlot) {
        super(eventName, startTime, endTime, eventTag, user, reminder);
        this.timeSlot = timeSlot;
    }

    public String getTime() {
        return timeSlot;
    }

    public String calculateTotalTime() {
        // Placeholder logic to calculate total meditation time
        return "Total time calculated based on time slot.";
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
