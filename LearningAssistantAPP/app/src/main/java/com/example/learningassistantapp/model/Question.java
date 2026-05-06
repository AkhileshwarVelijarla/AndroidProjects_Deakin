package com.example.learningassistantapp.model;

import java.util.List;

public class Question {
    private final String id;
    private final String topic;
    private final String questionText;
    private final List<String> options;
    private final int correctAnswerIndex;

    public Question(String id, String topic, String questionText, List<String> options, int correctAnswerIndex) {
        this.id = id;
        this.topic = topic;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
