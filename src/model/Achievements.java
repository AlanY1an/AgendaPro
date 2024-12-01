package model;

import controller.EventController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class Achievements {
    private User user;
    private EventController eventController;
    private ArrayList<Task> tasklist;

    // Constructor
    public Achievements(User user, EventController eventController, ArrayList<Task> tasklist) {
        this.user = user;
        this.eventController = eventController;
        this.tasklist = tasklist;
    }

    public Achievements(User user) {
        this.user = user;
        this.eventController = new EventController();
        this.tasklist = new ArrayList<>();
    }

    // 1. Count all finished events on current date
    public long countFinishedEventsOnCurrentDate() {
        LocalDate today = LocalDate.now();
        return eventController.getEventsOnDate(today).stream()
                .filter(Event::isFinished)
                .count();
    }

    // 2. Count all finished events in the last 7 days
    public long countFinishedEventsInLast7Days() {
        LocalDate today = LocalDate.now();
        return eventController.getAllEvents().stream()
                .filter(event -> event.isFinished() && isInLastNDays(event.getDate(), 7))
                .count();
    }

    // 3. Count all finished events in the last 30 days
    public long countFinishedEventsInLast30Days() {
        LocalDate today = LocalDate.now();
        return eventController.getAllEvents().stream()
                .filter(event -> event.isFinished() && isInLastNDays(event.getDate(), 30))
                .count();
    }

    // 4. Count all finished tasks on current date
    public long countFinishedTasksOnCurrentDate() {
        LocalDate today = LocalDate.now();
        return tasklist.stream()
                .filter(task -> task.isFinished() && isOnDate(task.getDueDate(), today))
                .count();
    }

    // 5. Count all finished tasks in the last 7 days
    public long countFinishedTasksInLast7Days() {
        return tasklist.stream()
                .filter(task -> task.isFinished() && isInLastNDays(toLocalDate(task.getDueDate()), 7))
                .count();
    }

    // 6. Count all finished tasks in the last 30 days
    public long countFinishedTasksInLast30Days() {
        return tasklist.stream()
                .filter(task -> task.isFinished() && isInLastNDays(toLocalDate(task.getDueDate()), 30))
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

    // Helper method to check if task due date is on the given LocalDate
    private boolean isOnDate(Date dueDate, LocalDate localDate) {
        LocalDate taskDate = toLocalDate(dueDate);
        return taskDate.equals(localDate);
    }

    // Add a new event
    public void addEvent(Event event) {
        eventController.addEvent(event);
    }

    // Count all finished events in a specific category
    public long countFinishedEventsInCategory(String category) {
        return eventController.getAllEvents().stream()
                .filter(event -> event.isFinished() && event.getCategory().equalsIgnoreCase(category))
                .count();
    }
}