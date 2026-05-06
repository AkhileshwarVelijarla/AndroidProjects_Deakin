package com.example.learningassistantapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.learningassistantapp.repository.LearningRepository;

public class LoginViewModel extends AndroidViewModel {
    private final LearningRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LearningRepository(application);
    }

    public boolean login(String username, String password) {
        return repository.login(username, password);
    }
}
