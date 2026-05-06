package com.example.learningassistantapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.learningassistantapp.repository.LearningRepository;

public class RegisterViewModel extends AndroidViewModel {
    private final LearningRepository repository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        repository = new LearningRepository(application);
    }

    public String validateAndRegister(String username, String email, String confirmEmail, String password, String confirmPassword, String phone) {
        if (isBlank(username) || isBlank(email) || isBlank(confirmEmail) || isBlank(password) || isBlank(confirmPassword) || isBlank(phone)) {
            return "Please complete all setup fields.";
        }
        if (!email.trim().equalsIgnoreCase(confirmEmail.trim())) {
            return "Email fields do not match.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        repository.register(username, email, phone, password);
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
