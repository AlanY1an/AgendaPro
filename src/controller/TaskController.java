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
    }

    @FXML
    private void handleSave() {
        String name = taskName.getText();
        LocalDate localDueDate = dueDate.getValue();
        boolean finished = isfinished.isSelected();
        String note = note_content.getText();

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
            selectedTask.setNote(note);
        } else {
            // Add a new task
            Task newTask = new Task(name, dueDateValue);
            newTask.setFinished(finished);
            if(note_content != null) newTask.setNote(note);
            TaskRepository.getInstance().getTasks().add(newTask);
            
        }

        clearForm();
        TodoListView.refresh();
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
