package com.example.sit708_task8_1c_chatbot.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sit708_task8_1c_chatbot.databinding.ActivityChatBinding;
import com.example.sit708_task8_1c_chatbot.ui.adapter.MessagesAdapter;
import com.example.sit708_task8_1c_chatbot.ui.viewmodel.ChatViewModel;

public class ChatActivity extends AppCompatActivity {
    
    private ActivityChatBinding binding;
    private ChatViewModel viewModel;
    private MessagesAdapter adapter;
    private SharedPreferences sharedPreferences;
    private String username;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        sharedPreferences = getSharedPreferences("chatbot_prefs", Context.MODE_PRIVATE);
        
        // Get username from intent or SharedPreferences
        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = sharedPreferences.getString("username", "User");
        }
        
        setupUI();
        setupRecyclerView();
        setupObservers();
        
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logout();
            }
        });
    }
    
    private void setupUI() {
        // Update toolbar with username
        binding.toolbarUsername.setText("Logged in as: " + username);
        
        // Send button click listener
        binding.sendButton.setOnClickListener(v -> {
            String message = binding.messageInputEditText.getText().toString();
            if (!message.trim().isEmpty()) {
                viewModel.sendMessage(message);
                binding.messageInputEditText.setText("");
                binding.messageInputEditText.clearFocus();
            }
        });
    }
    
    private void setupRecyclerView() {
        adapter = new MessagesAdapter();
        binding.messagesRecyclerView.setAdapter(adapter);
        binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void setupObservers() {
        viewModel.getMessages().observe(this, messages -> {
            if (messages != null) {
                adapter.setMessages(messages);
                
                // Auto scroll to latest message
                if (!messages.isEmpty()) {
                    binding.messagesRecyclerView.scrollToPosition(messages.size() - 1);
                    binding.emptyChatText.setVisibility(View.GONE);
                    binding.messagesRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyChatText.setVisibility(View.VISIBLE);
                    binding.messagesRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.chatProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.sendButton.setEnabled(!isLoading);
            binding.messageInputEditText.setEnabled(!isLoading);
        });
        
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // Show error in a snackbar or toast
                android.widget.Toast.makeText(this, errorMessage, android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.apply();
        
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
