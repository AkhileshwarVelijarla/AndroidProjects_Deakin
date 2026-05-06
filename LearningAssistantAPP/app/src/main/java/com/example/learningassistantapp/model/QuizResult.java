package com.example.learningassistantapp.model;

import java.io.Serializable;
import java.util.Map;

public class QuizResult implements Serializable {
    private final String taskId;
    private final String taskTitle;
    private final int score;
    private final int totalQuestions;
    private final Map<String, Integer> selectedAnswers;

    public QuizResult(String taskId, String taskTitle, int score, int totalQuestions, Map<String, Integer> selectedAnswers) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.selectedAnswers = selectedAnswers;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public Map<String, Integer> getSelectedAnswers() {
        return selectedAnswers;
    }
}
