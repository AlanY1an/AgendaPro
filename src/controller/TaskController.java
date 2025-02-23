package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    
    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;
    
    private static final String FILE_PATH = "tasks.txt";
    private final ObservableList<Task> tasks;

    public TaskController() {
        tasks = FXCollections.observableArrayList();
        loadTasksFromFile(FILE_PATH);
    }

    
    public ObservableList<Task> getTasks(){
    	return tasks;
    }
    @FXML
    public void initialize() {
        // Bind ListView with tasks
    	TodoListView.setItems(tasks);

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
        saveTasksToFile(FILE_PATH);
        TodoListView.refresh();
    }
    
    public void handleDelete() {
    	Task selectedTask = TodoListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
        	tasks.remove(selectedTask); // Remove the task from the ObservableList
        	saveTasksToFile(FILE_PATH); // Save updated tasks to the file
            clearForm();
        } else {
            showAlert("Error", "No task selected to delete!");
        }
    }
    
    public void handleAdd() {
    	clearForm();
    	Task newTask = new Task();
    	tasks.add(newTask); // Add new task to the ObservableList

        // Select the new task to allow editing
        TodoListView.getSelectionModel().select(newTask);
    }

    private void populateTaskDetails(Task task) {
        taskName.setText(task.getTaskName());
        dueDate.setValue(task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        isfinished.setSelected(task.isFinished());
        isfinished.setStyle(
        	    "-fx-background-color: white; " +
        	    "-fx-border-color: white; " +
        	    "-fx-mark-color: #4CAF50;" +
        	    "-fx-stroke-width: 3;"
        	);
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
    
    private void saveTasksToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Task task : tasks) {
                writer.write(task.getTaskName() + "|" +
                        sdf.format(task.getDueDate()) + "|" +
                        task.isFinished() + "|" +
                        (task.getNote() != null ? task.getNote().replace("|", " ").replace("\n", "\\n") : ""));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadTasksFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            tasks.clear();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 4);
                if (parts.length >= 3) {
                    String taskName = parts[0];
                    Date dueDate = sdf.parse(parts[1]); 
                    boolean isFinished = Boolean.parseBoolean(parts[2]);
                    String note = parts.length > 3 ? parts[3].replace("\\n", "\n") : "";

                    Task task = new Task(taskName, dueDate);
                    task.setFinished(isFinished);
                    task.setNote(note);
                    tasks.add(task);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}