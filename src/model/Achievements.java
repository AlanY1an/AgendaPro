package model;

import controller.EventController;
import controller.TaskController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Achievements {
    private EventController eventController;
    private TaskController taskController;

    // Constructor
    public Achievements(EventController eventController, TaskController tasklist) {
        this.eventController = eventController;
        this.taskController = tasklist;
    }

    // 1. Count all finished events on current date
    public int countFinishedEventsOnCurrentDate() {
        LocalDate today = LocalDate.now();
        return (int) eventController.getEventsOnDate(today).stream()
                .filter(Event::isFinished)
                .count();
    }
    
    public int countAllEventsOnCurrentDate() {
        LocalDate today = LocalDate.now();
        return (int) eventController.getEventsOnDate(today).stream().count();
    }

    
    private boolean isOnDate(Date dueDate, LocalDate localDate) {
        LocalDate taskDate = toLocalDate(dueDate);
        return taskDate.equals(localDate);
    }
    // 2. Count all finished events in the last 7 days
    public int countFinishedEventsInLast7Days() {
        return (int) eventController.getAllEvents().stream()
                .filter(event -> event.isFinished() && isInLastNDays(event.getDate(), 7))
                .count();
    }

    // 3. Count all finished events in the last 30 days
    public int countFinishedEventsInLast30Days() {
        return (int) eventController.getAllEvents().stream()
                .filter(event -> event.isFinished() && isInLastNDays(event.getDate(), 30))
                .count();
    }

    // 4. Count all finished tasks on current date
    public int countFinishedTasksOnCurrentDate() {
    	LocalDate today = LocalDate.now();
        return (int) taskController.getTasks().stream()
                .filter(task -> task.isFinished() && isOnDate(task.getDueDate(), today))
                .count();
    }

    // 5. Count all finished tasks in the last 7 days
    public int countFinishedTasksInLast7Days() {
    	 LocalDate today = LocalDate.now();
    	    LocalDate sevenDaysAgo = today.minusDays(7);

    	    return (int) taskController.getTasks().stream()
    	            .filter(task -> task.isFinished() &&
    	                    (toLocalDate(task.getDueDate()).isAfter(sevenDaysAgo) ||
    	                     toLocalDate(task.getDueDate()).isEqual(sevenDaysAgo)))
    	            .count();
    }

    // 6. Count all finished tasks in the last 30 days
    public int countFinishedTasksInLast30Days() {
    	LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);

        return (int) taskController.getTasks().stream()
                .filter(task -> task.isFinished() &&
                        (toLocalDate(task.getDueDate()).isAfter(thirtyDaysAgo) ||
                         toLocalDate(task.getDueDate()).isEqual(thirtyDaysAgo)))
                .count();
    }

    // Helper method to check if a date is within the last N days
    private boolean isInLastNDays(LocalDate date, int n) {
        return ChronoUnit.DAYS.between(date, LocalDate.now()) <= n;
    }

    // Convert Date to LocalDate for comparison
    private LocalDate toLocalDate(Date date) {
        return LocalDate.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
    }

    // Add a new event
    public void addEvent(Event event) {
        eventController.addEvent(event);
    }
    
    public long countFinishedEventsInCategory(String category) {
        LocalDate today = LocalDate.now(); // Get today's date
        LocalDate thirtyDaysAgo = today.minusDays(30); // Calculate the date 30 days ago

        return eventController.getAllEvents().stream()
                .filter(event -> event.isFinished() // Check if the event is finished
                        && event.getCategory().equalsIgnoreCase(category) // Check category
                        && (event.getDate().isAfter(thirtyDaysAgo) || event.getDate().isEqual(thirtyDaysAgo))) // Check if within 30 days
                .count();
    }



	public EventController getEventController() {
		return this.eventController;
	}

	public int getTotalMeditationMinutesInLast7Days() {
		LocalDate today = LocalDate.now();
	    LocalDate sevenDaysAgo = today.minusDays(7);

	    return eventController.getAllEvents().stream()
	        .filter(event -> "Meditation".equalsIgnoreCase(event.getCategory()) 
	                && (event.getDate().isAfter(sevenDaysAgo) || event.getDate().isEqual(sevenDaysAgo)) 
	                && event.isFinished()) 
	        .mapToInt(Event::getMeditationMinutes) 
	        .sum(); 
	}

	
	public int getTotalMeditationMinutesInLast30Days() {
		LocalDate today = LocalDate.now();
	    LocalDate thirtyDaysAgo = today.minusDays(30);
	    return eventController.getAllEvents().stream()
	        .filter(event -> "Meditation".equalsIgnoreCase(event.getCategory()) 
	                && (event.getDate().isAfter(thirtyDaysAgo) || event.getDate().isEqual(thirtyDaysAgo)) 
	                && event.isFinished()) 
	        .mapToInt(Event::getMeditationMinutes) 
	        .sum(); 
	}
	
	
	public int getTotalMeditationMinutesToday() {
	    LocalDate today = LocalDate.now();
	    return eventController.getAllEvents().stream()
	        .filter(event -> "Meditation".equalsIgnoreCase(event.getCategory()) 
	                && event.getDate().isEqual(today) 
	                && event.isFinished()) 
	        .mapToInt(Event::getMeditationMinutes) 
	        .sum(); 
	}

	public int countFocusTimeOnCurrentDate() {
		 LocalDate today = LocalDate.now(); 
		    return eventController.getAllEvents().stream()
		        .filter(event -> event.getDate().isEqual(today)) 
		        .filter(event -> {
		            String category = event.getCategory();
		            return !"Meditation".equalsIgnoreCase(category) && !"Entertainment".equalsIgnoreCase(category);
		        }) 
		        .mapToInt(Event::getDuration) 
		        .sum(); 
	}
}