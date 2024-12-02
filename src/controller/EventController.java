package controller;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;



public class EventController {

	
	// dynamicly get change of the list and refresh the view
    private final ObservableList<Event> eventList;

    public EventController() {
        eventList = FXCollections.observableArrayList();
    }

    public void addEvent(Event event) {
        eventList.add(event);
    }

    public void removeEvent(Event event) {
        eventList.removeIf(e -> e.getId() == event.getId());
    }

    
    public ObservableList<Event> getEventsOnDate(LocalDate date) {
        ObservableList<Event> eventsOnDate = FXCollections.observableArrayList();
        for (Event event : eventList) {
            if (event.getDate().equals(date)) {
                eventsOnDate.add(event);
            }
            if (event.getDate().isBefore(LocalDate.now())) {
                event.setFinished(true);
            }
        }
        return eventsOnDate;
    }

    public ObservableList<Event> getAllEvents() {
        return eventList;
    }
    
    

}
