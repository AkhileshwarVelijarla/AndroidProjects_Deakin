package com.example.sit708_task8_1c_chatbot.api;

import com.example.sit708_task8_1c_chatbot.api.model.ChatRequest;
import com.example.sit708_task8_1c_chatbot.api.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OpenRouterApiService {
    
    @POST("chat/completions")
    Call<ChatResponse> sendMessage(
            @Header("Authorization") String authorization,
            @Header("HTTP-Referer") String referer,
            @Header("X-Title") String title,
            @Body ChatRequest request
    );
}
