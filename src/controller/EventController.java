package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;

import java.time.LocalDate;

public class EventController {

	
	// dynamicly get change of the list and refresh the view
    private final ObservableList<Event> eventList;

    public EventController() {
        eventList = FXCollections.observableArrayList();
        loadEventsFromFile("events.txt");
    }

    public void addEvent(Event event) {
        eventList.add(event);
        saveEventsToFile("events.txt");
    }

    public void removeEvent(Event event) {
        eventList.removeIf(e -> e.getId() == event.getId());
        saveEventsToFile("events.txt"); // 保存到文件
    }

    
    public ObservableList<Event> getEventsOnDate(LocalDate date) {
        ObservableList<Event> eventsOnDate = FXCollections.observableArrayList();
        for (Event event : eventList) {
            if (event.getDate().equals(date)) {
                eventsOnDate.add(event);
            }
        }
        return eventsOnDate;
    }

    public ObservableList<Event> getAllEvents() {
        return eventList;
    }
    
    // Event method for pomodoto and meditation:
    public ObservableList<Event> getMeditationEventsOnDate(LocalDate date) {
        ObservableList<Event> meditationEvents = FXCollections.observableArrayList();
        for (Event event : eventList) {
            if ("Meditation".equals(event.getCategory()) && event.getDate().equals(date)) {
                meditationEvents.add(event);
            }
            if (event.getDate().isBefore(LocalDate.now())) {
            	event.setFinished(true);
            }
        }
        return meditationEvents;
    }
    
    public int getTotalMeditationMinutes() {
        return eventList.stream()
            .filter(event -> "Meditation".equals(event.getCategory()))
            .mapToInt(Event::getMeditationMinutes)
            .sum();
    }

    public int getTotalMeditationMinutesOnDate(LocalDate date) {
        return eventList.stream()
            .filter(event -> "Meditation".equals(event.getCategory()) && event.getDate().equals(date))
            .mapToInt(Event::getMeditationMinutes)
            .sum();
    }
    
    public void saveEventsToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Event event : eventList) {
                // 判断事件类型，根据字段构建保存行
                if (event.getMeditationMinutes() > 0 || event.getDuration() > 0) {
                    writer.write(event.getId() + "|" +
                                 event.getTitle() + "|" +
                                 event.getCategory() + "|" +
                                 event.getDescription() + "|" +
                                 event.getDate() + "|" +
                                 event.getDuration() + "|" +
                                 event.getMeditationMinutes() + "|" +
                                 event.isFinished());
                } else {
                    writer.write(event.getId() + "|" +
                                 event.getTitle() + "|" +
                                 event.getCategory() + "|" +
                                 event.getDescription() + "|" +
                                 event.getDate() + "|" +
                                 event.isFinished());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadEventsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            eventList.clear(); // 清空列表
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 8) {
                    // 包含 duration 和 meditationMinutes 的事件
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String category = parts[2];
                    String description = parts[3];
                    LocalDate date = LocalDate.parse(parts[4]);
                    int duration = Integer.parseInt(parts[5]);
                    int meditationMinutes = Integer.parseInt(parts[6]);
                    boolean finished = Boolean.parseBoolean(parts[7]);
                    eventList.add(new Event(id, title, category, description, date, duration, meditationMinutes));
                } else if (parts.length == 6) {
                    // 简单事件
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String category = parts[2];
                    String description = parts[3];
                    LocalDate date = LocalDate.parse(parts[4]);
                    boolean finished = Boolean.parseBoolean(parts[5]);
                    eventList.add(new Event(id, title, category, description, date));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}