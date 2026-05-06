package com.example.learningassistantapp.repository;

import com.example.learningassistantapp.model.LLMRequest;
import com.example.learningassistantapp.model.LLMResponse;
import com.example.learningassistantapp.network.LLMApiService;

public class LLMRepository {
    public interface Callback {
        void onSuccess(LLMResponse response);
        void onFailure(String errorMessage);
    }

    private final LLMApiService apiService;

    public LLMRepository() {
        apiService = new LLMApiService();
    }

    public void requestCompletion(LLMRequest request, Callback callback) {
        apiService.generateResponse(request, new LLMApiService.Callback() {
            @Override
            public void onSuccess(LLMResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }
}
