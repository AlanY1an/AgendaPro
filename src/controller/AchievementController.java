package controller;

import java.time.LocalDate;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import model.Achievements;
import model.Event;
import model.Task;
import model.User;

public class AchievementController {

    // FXML components
    @FXML private Label StudyEvents;
    @FXML private Label WorkEvents;
    @FXML private Label EntertainmentEvents;
    @FXML private Label ExerciseEvents;

    @FXML private Label completedTasks7DaysLabel;
    @FXML private Label completedTasks30DaysLabel;

    @FXML private ProgressBar tasks7DaysProgress;
    @FXML private ProgressBar tasks30DaysProgress;

    @FXML private Label TasksIn7Days;
    @FXML private Label TasksIn30Days;
    
    @FXML private Label MeditationIn7Days;
    @FXML private Label MeditationIn30Days;
    
    @FXML private ProgressBar meditationIn7Days;
    @FXML private ProgressBar meditationIn30Days;


    // Instance of Achievements to get the data
    private Achievements achievements;

    // Constructor
    public AchievementController(Achievements as) {
    	 this.achievements=as;
    	 
    }

    // Initialize method, called after FXML is loaded
    @FXML
    public void initialize() {
        if (achievements == null) {
            System.out.println("Achievements not set. Initializing default values...");
        }
        achievements.getEventController().getAllEvents().addListener((ListChangeListener.Change<?> change) -> {
            while (change.next()) {
            	System.out.println("Change detected: Added? " + change.wasAdded() + ", Removed? " + change.wasRemoved());
                if (change.wasAdded() || change.wasRemoved()) {
                    System.out.println("Events list changed. Updating data...");
                    updateAchievementData();
                }
            }
        });
        updateEventCounts();
        // Validate FXML components
        validateFXMLComponents();
        updateAchievementData();
    }


    // Validate FXML components to avoid NullPointerException
    private void validateFXMLComponents() {
        if (StudyEvents == null || WorkEvents == null || EntertainmentEvents == null || ExerciseEvents == null ||
            TasksIn7Days == null || TasksIn30Days == null) {
            throw new IllegalStateException("FXML components are not properly injected.");
        }
        System.out.println("All FXML components successfully initialized.");
    }

    // Update the view with current achievement data
    private void updateAchievementData() {
        // Update event counts
        updateEventCounts();
        // Update task progress
        updateTaskProgress();
    }

    // Update event counts in labels
    private void updateEventCounts() {
        StudyEvents.setText(String.valueOf(achievements.countFinishedEventsInCategory("Study")));
        WorkEvents.setText(String.valueOf(achievements.countFinishedEventsInCategory("Work")));
        EntertainmentEvents.setText(String.valueOf(achievements.countFinishedEventsInCategory("Entertainment")));
        ExerciseEvents.setText(String.valueOf(achievements.countFinishedEventsInCategory("Exercise")));
        System.out.print(String.valueOf(achievements.countFinishedEventsInCategory("Study")));
    }

    // Update task progress bars and labels
    private void updateTaskProgress() {
        int tasksCompleted7Days = achievements.countFinishedTasksInLast7Days();
        int tasksCompleted30Days = achievements.countFinishedTasksInLast30Days();
        int maxTasks = 10; // Assuming 100 is the maximum for normalization

        // Safeguard progress values between 0 and 1
        tasks7DaysProgress.setProgress((double) tasksCompleted7Days / maxTasks);
        tasks30DaysProgress.setProgress((double) tasksCompleted30Days / maxTasks);


        // Update labels for tasks
        TasksIn7Days.setText(String.valueOf(tasksCompleted7Days));
        TasksIn30Days.setText(String.valueOf(tasksCompleted30Days));
        
        
        int meditateIn7Days = achievements.getTotalMeditationMinutesInLast7Days();
        int meditateIn30Days = achievements.getTotalMeditationMinutesInLast30Days();
        int maxLength = 10;

        MeditationIn7Days.setText(String.valueOf(meditateIn7Days));
        MeditationIn30Days.setText(String.valueOf(meditateIn30Days));
        
        meditationIn7Days.setProgress((double) meditateIn7Days / maxLength);
        meditationIn30Days.setProgress((double) meditateIn30Days / maxLength);
    }

    // Normalize progress values between 0 and 1
//    private double normalizeProgress(double value) {
//        return Math.max(0, Math.min(1, value));
//    }
}
