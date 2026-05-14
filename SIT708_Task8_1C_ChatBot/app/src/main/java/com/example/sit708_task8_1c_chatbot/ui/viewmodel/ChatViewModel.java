package com.example.sit708_task8_1c_chatbot.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sit708_task8_1c_chatbot.BuildConfig;
import com.example.sit708_task8_1c_chatbot.api.ApiClient;
import com.example.sit708_task8_1c_chatbot.api.OpenRouterApiService;
import com.example.sit708_task8_1c_chatbot.api.model.ChatRequest;
import com.example.sit708_task8_1c_chatbot.api.model.ChatResponse;
import com.example.sit708_task8_1c_chatbot.data.entity.Message;
import com.example.sit708_task8_1c_chatbot.data.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends AndroidViewModel {
    
    private MessageRepository messageRepository;
    private LiveData<List<Message>> messages;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Call<ChatResponse> activeCall;
    private String openRouterApiKey = BuildConfig.OPENROUTER_API_KEY;
    
    public ChatViewModel(Application application) {
        super(application);
        messageRepository = new MessageRepository(application);
        messages = messageRepository.getAllMessages();
    }
    
    public LiveData<List<Message>> getMessages() {
        return messages;
    }
    
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public void setOpenRouterApiKey(String apiKey) {
        this.openRouterApiKey = apiKey;
    }
    
    public void sendMessage(String messageText) {
        if (messageText == null || messageText.trim().isEmpty()) {
            return;
        }
        
        String trimmedMessage = messageText.trim();
        errorMessage.setValue(null);
        Message userMessage = new Message(trimmedMessage, true, System.currentTimeMillis());
        messageRepository.insertMessage(userMessage);
        
        if (isApiKeyMissing()) {
            Message botMessage = new Message(
                    "OpenRouter API key is missing. Replace OPENROUTER_API_KEY in local.properties with your real key, then rebuild the app.",
                    false,
                    System.currentTimeMillis()
            );
            messageRepository.insertMessage(botMessage);
            return;
        }

        isLoading.setValue(true);
        sendToOpenRouter(trimmedMessage);
    }
    
    private void sendToOpenRouter(String userMessage) {
        try {
            List<ChatRequest.Message> messageList = new ArrayList<>();
            messageList.add(new ChatRequest.Message("system", "You are a helpful study assistant for a university Android chatbot assignment. Keep answers concise and friendly."));
            messageList.add(new ChatRequest.Message("user", userMessage));
            
            ChatRequest request = new ChatRequest("openai/gpt-4o-mini", messageList);
            
            OpenRouterApiService apiService = ApiClient.getApiService();
            String authHeader = "Bearer " + openRouterApiKey.trim();
            String referer = "https://sit708-task8-1c-chatbot.local";
            String title = "SIT708 Task 8.1C ChatBot";
            
            activeCall = apiService.sendMessage(authHeader, referer, title, request);
            
            activeCall.enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                    isLoading.postValue(false);
                    
                    if (response.isSuccessful() && response.body() != null) {
                        ChatResponse chatResponse = response.body();
                        
                        if (chatResponse.getChoices() != null
                                && !chatResponse.getChoices().isEmpty()
                                && chatResponse.getChoices().get(0).getMessage() != null
                                && chatResponse.getChoices().get(0).getMessage().getContent() != null) {
                            String botReply = chatResponse.getChoices().get(0).getMessage().getContent();
                            Message botMessage = new Message(botReply, false, System.currentTimeMillis());
                            messageRepository.insertMessage(botMessage);
                            errorMessage.postValue(null);
                        } else {
                            postBotError("The bot returned an empty response.");
                        }
                    } else if (response.body() != null && response.body().getError() != null) {
                        postBotError("OpenRouter error: " + response.body().getError().getMessage());
                    } else {
                        postBotError("OpenRouter error: " + response.code() + " " + response.message());
                    }
                }
                
                @Override
                public void onFailure(Call<ChatResponse> call, Throwable t) {
                    if (call.isCanceled()) {
                        return;
                    }
                    isLoading.postValue(false);
                    postBotError("Network error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            isLoading.postValue(false);
            postBotError("Error: " + e.getMessage());
        }
    }

    private boolean isApiKeyMissing() {
        return openRouterApiKey == null
                || openRouterApiKey.trim().isEmpty()
                || "PASTE_YOUR_OPENROUTER_API_KEY_HERE".equals(openRouterApiKey.trim());
    }

    private void postBotError(String message) {
        errorMessage.postValue(message);
        messageRepository.insertMessage(new Message(message, false, System.currentTimeMillis()));
    }
    
    public void clearMessages() {
        messageRepository.deleteAllMessages();
    }

    @Override
    protected void onCleared() {
        if (activeCall != null) {
            activeCall.cancel();
        }
        super.onCleared();
    }
}
