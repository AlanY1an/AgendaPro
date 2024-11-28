package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import model.Achievements;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.beans.binding.Bindings;

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

    // Instance of Achievements to get the data
    private Achievements achievements;

    // No-args constructor required by FXMLLoader
    public AchievementController() {
    }

    // Setter to inject Achievements instance
    public void setAchievements(Achievements achievements) {
        this.achievements = achievements;
        // Update the view with the injected data
        updateAchievementData();
    }

    // Initialize method, called after FXML is loaded
    @FXML
    public void initialize() {
        // Only update if achievements is already set
        if (achievements != null) {
            updateAchievementData();
        }
    }

    private void updateAchievementData() {
        if (achievements == null) {
            return; // Avoid null pointer exceptions
        }

        // Update the events and task labels with the current data from achievements
        StudyEvents.setText(String.valueOf(achievements.countFinishedEventsInCategory("Study")));
        WorkEvents.setText(String.valueOf(achievements.countFinishedEventsInCategory("Work")));
        EntertainmentEvents.setText(String.valueOf(achievements.countFinishedEventsInCategory("Entertainment")));
        ExerciseEvents.setText(String.valueOf(achievements.countFinishedEventsInCategory("Exercise")));

        // Update the progress bars for tasks completed in the last 7 and 30 days
        double tasksCompleted7Days = achievements.countFinishedTasksInLast7Days();
        double tasksCompleted30Days = achievements.countFinishedTasksInLast30Days();
        double maxTasks = 100.0; // Assuming the maximum possible number of tasks is 100

        tasks7DaysProgress.setProgress(tasksCompleted7Days / maxTasks);
        tasks30DaysProgress.setProgress(tasksCompleted30Days / maxTasks);

        // Update the labels showing the actual number of tasks completed
        TasksIn7Days.setText(String.valueOf(tasksCompleted7Days));
        TasksIn30Days.setText(String.valueOf(tasksCompleted30Days));

        // Update labels for completed tasks in last 7 and 30 days
        completedTasks7DaysLabel.setText("Completed in the Last 7 Days: " + tasksCompleted7Days);
        completedTasks30DaysLabel.setText("Completed in the Last 30 Days: " + tasksCompleted30Days);
    }
}
