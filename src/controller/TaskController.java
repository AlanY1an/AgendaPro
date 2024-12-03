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
import model.TaskRepository;

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
    
    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    public void initialize() {
        // Bind ListView with tasks
    	TodoListView.setItems(TaskRepository.getInstance().getTasks());

        // Set custom cell factory
        TodoListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                } else {
                    setText(task.getTaskName() + (task.isFinished() ? " (Finished)" : ""));
                }
            }
        });

        TodoListView.getSelectionModel().selectedItemProperty().addListener((obs, oldTask, selectedTask) -> {
            if (selectedTask != null) {
                populateTaskDetails(selectedTask);
            }
        });
        
        addButton.setOnAction(event -> handleAdd());
        deleteButton.setOnAction(event -> handleDelete());
    }

    @FXML
    private void handleSave() {
    	String name = taskName.getText();
        LocalDate localDueDate = dueDate.getValue();
        boolean finished = isfinished.isSelected();
        String note = note_content.getText();

        // Validation
        if (name == null || name.trim().isEmpty()) {
            showAlert("Error", "Task Name is required!");
            return;
        }

        if (localDueDate == null) {
            showAlert("Error", "Due Date is required!");
            return;
        }

        // Convert LocalDate to Date
        Date dueDateValue = Date.from(localDueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Editing an existing task
        Task selectedTask = TodoListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setTaskName(name);
            selectedTask.setDueDate(dueDateValue);
            selectedTask.setFinished(finished);
            selectedTask.setNote(note);
        } else {
            // Inform the user they need to use the "Add" button to create new tasks
            showAlert("Error", "No task is selected. Use the 'Add' button to create a new task.");
        }

        // Clear the form and refresh the ListView
        TodoListView.refresh();
    }
    
    public void handleDelete() {
    	Task selectedTask = TodoListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            TaskRepository.getInstance().removeTask(selectedTask); // Remove from repository
            TodoListView.getSelectionModel().clearSelection(); // Clear selection in UI
            clearForm();
        } else {
            showAlert("Error", "No task selected to delete!");
        }
    }
    
    public void handleAdd() {
    	clearForm();
    	Task newTask = new Task();
        TaskRepository.getInstance().addTask(newTask); // Add to repository

        // Select the new task to allow editing
        TodoListView.getSelectionModel().select(newTask);
    }

    private void populateTaskDetails(Task task) {
        taskName.setText(task.getTaskName());
        dueDate.setValue(task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        isfinished.setSelected(task.isFinished());
        note_content.setText(task.getNote());
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
}
