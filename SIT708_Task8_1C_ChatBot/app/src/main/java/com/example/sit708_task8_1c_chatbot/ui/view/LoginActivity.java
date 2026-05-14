package com.example.sit708_task8_1c_chatbot.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sit708_task8_1c_chatbot.databinding.ActivityLoginBinding;
import com.example.sit708_task8_1c_chatbot.ui.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private static final String PREF_USERNAME = "username";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        sharedPreferences = getSharedPreferences("chatbot_prefs", Context.MODE_PRIVATE);
        
        // Check if user is already logged in
        String savedUsername = sharedPreferences.getString(PREF_USERNAME, null);
        if (savedUsername != null && !savedUsername.isEmpty()) {
            navigateToChatScreen(savedUsername);
            return;
        }
        
        // Set up UI
        setupUI();
        setupObservers();
    }
    
    private void setupUI() {
        binding.continueButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString();
            viewModel.validateAndContinue(username);
        });
    }
    
    private void setupObservers() {
        viewModel.getUsername().observe(this, username -> {
            if (username != null) {
                saveUsername(username);
                navigateToChatScreen(username);
            }
        });
        
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.continueButton.setEnabled(!isLoading);
            binding.loginProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                binding.errorTextView.setText(errorMessage);
                binding.errorTextView.setVisibility(View.VISIBLE);
            } else {
                binding.errorTextView.setVisibility(View.GONE);
            }
        });
    }
    
    private void saveUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USERNAME, username);
        editor.apply();
    }
    
    private void navigateToChatScreen(String username) {
        Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
