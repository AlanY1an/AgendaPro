package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskRepository {
    private static TaskRepository instance;
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    private TaskRepository() {}

    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public void removeTask(Task task) {
    	tasks.remove(task);
    }
}
