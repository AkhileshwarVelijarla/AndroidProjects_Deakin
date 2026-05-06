package com.example.learningassistantapp.model;

public class LLMUiState {
    private final boolean loading;
    private final String prompt;
    private final String response;
    private final String error;

    public LLMUiState(boolean loading, String prompt, String response, String error) {
        this.loading = loading;
        this.prompt = prompt;
        this.response = response;
        this.error = error;
    }

    public static LLMUiState idle() {
        return new LLMUiState(false, "", "", "");
    }

    public static LLMUiState loading(String prompt) {
        return new LLMUiState(true, prompt, "", "");
    }

    public static LLMUiState success(String prompt, String response) {
        return new LLMUiState(false, prompt, response, "");
    }

    public static LLMUiState error(String prompt, String error) {
        return new LLMUiState(false, prompt, "", error);
    }

    public boolean isLoading() {
        return loading;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }
}
