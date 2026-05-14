package com.example.sit708_task8_1c_chatbot.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public MutableLiveData<String> getUsername() {
        return username;
    }
    
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public void setUsername(String user) {
        username.setValue(user);
    }
    
    public void validateAndContinue(String inputUsername) {
        if (inputUsername == null || inputUsername.trim().isEmpty()) {
            errorMessage.setValue("Please enter a username");
            return;
        }
        
        isLoading.setValue(true);
        // Simulate validation
        new Thread(() -> {
            try {
                Thread.sleep(500);
                username.postValue(inputUsername.trim());
                isLoading.postValue(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public void clearError() {
        errorMessage.setValue(null);
    }
}
