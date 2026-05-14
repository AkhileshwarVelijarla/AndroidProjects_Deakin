package com.example.sit708_task8_1c_chatbot.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    
    private static final String BASE_URL = "https://openrouter.ai/api/v1/";
    private static Retrofit retrofit;
    
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    
    public static OpenRouterApiService getApiService() {
        return getRetrofit().create(OpenRouterApiService.class);
    }
}
