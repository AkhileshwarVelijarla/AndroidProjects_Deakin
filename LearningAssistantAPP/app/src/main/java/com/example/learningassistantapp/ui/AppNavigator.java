package com.example.learningassistantapp.ui;

import com.example.learningassistantapp.model.QuizResult;

public interface AppNavigator {
    void openRegister();
    void openInterests();
    void openHome();
    void openAssessment();
    void openResults(QuizResult result);
}
