package controller;

import model.Event;

public class EventController {

    public void addEvent(Event event) {
        // 添加事件到数据库或模型
        System.out.println("Event added: " + event);
    }

    public void deleteEvent(Event event) {
        // 删除事件逻辑
        System.out.println("Event deleted: " + event);
    }
}
