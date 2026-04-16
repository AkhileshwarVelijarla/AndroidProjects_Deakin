package com.example.istreampersonalvideoplaylistapp_51c.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.istreampersonalvideoplaylistapp_51c.model.UserEntity;
import com.example.istreampersonalvideoplaylistapp_51c.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<UserEntity> login(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            message.setValue("Username and password are required");
            return new MutableLiveData<>(null);
        }
        return repository.login(username.trim(), password.trim());
    }

    public LiveData<AuthRepository.SignupResult> signup(String fullName, String username, String password, String confirmPassword) {
        if (fullName.trim().isEmpty() || username.trim().isEmpty()
                || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            message.setValue("All fields are required");
            return new MutableLiveData<>(new AuthRepository.SignupResult(false, "All fields are required", -1));
        }

        if (!password.equals(confirmPassword)) {
            message.setValue("Passwords do not match");
            return new MutableLiveData<>(new AuthRepository.SignupResult(false, "Passwords do not match", -1));
        }

        return repository.signup(fullName.trim(), username.trim(), password);
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
