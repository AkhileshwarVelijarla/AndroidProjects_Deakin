package com.example.learningassistantapp.model;

public class LearningTask {
    private final String id;
    private final String title;
    private final String description;
    private final String topic;

    public LearningTask(String id, String title, String description, String topic) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTopic() {
        return topic;
    }
}
