package controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Event;
import model.Task;

public class TaskController {
	
	@FXML
    private ListView<Task> TodoListView;

    @FXML
    private TextField taskName;

    @FXML
    private DatePicker dueDate;

    @FXML
    private CheckBox isfinished;

    @FXML
    private TextArea note_content;

    // Observable list to hold tasks
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind ListView with tasks
        TodoListView.setItems(taskList);

        TodoListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null || task.getTaskName() == null) {
                    setText(null);
                } else {
                    setText(task.getTaskName() + (task.isFinished() ? " (Finished)" : ""));
                }
            }
        });

        // Check if taskList is not empty
        if (!taskList.isEmpty()) {
            System.out.println("Tasks Loaded"); // Display a message when tasks exist
        } else {
        	System.out.println("No tasks available"); // Default message when no tasks
        }

        // Handle selection changes
        TodoListView.getSelectionModel().selectedItemProperty().addListener((obs, oldTask, selectedTask) -> {
            if (selectedTask != null) {
                populateTaskDetails(selectedTask);
            }
        });
    }

    @FXML
    private void saveNote() {
        String name = taskName.getText();
        LocalDate localDueDate = dueDate.getValue();
        boolean finished = isfinished.isSelected();

        if (name == null || name.trim().isEmpty() || localDueDate == null) {
            showAlert("Error", "Task Name and Due Date are required!");
            return;
        }

        // Convert LocalDate to Date
        Date dueDateValue = Date.from(localDueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Check if editing an existing task
        Task selectedTask = TodoListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setTaskName(name);
            selectedTask.setDueDate(dueDateValue);
            selectedTask.setFinished(finished);
        } else {
            // Add a new task
            Task newTask = new Task(name, dueDateValue);
            newTask.setFinished(finished);
            taskList.add(newTask);
        }

        clearForm();
        TodoListView.refresh();
    }

    private void populateTaskDetails(Task task) {
        taskName.setText(task.getTaskName());
        dueDate.setValue(task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        isfinished.setSelected(task.isFinished());
        note_content.setText("Edit task details here.");
    }

    private void clearForm() {
        taskName.clear();
        dueDate.setValue(null);
        isfinished.setSelected(false);
        note_content.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public ObservableList<Task> getAllTasks() {
        return FXCollections.unmodifiableObservableList(taskList);
    }
}
