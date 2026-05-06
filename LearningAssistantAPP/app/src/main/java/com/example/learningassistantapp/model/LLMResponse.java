package com.example.learningassistantapp.model;

public class LLMResponse {
    private final String prompt;
    private final String responseText;
    private final boolean mock;

    public LLMResponse(String prompt, String responseText, boolean mock) {
        this.prompt = prompt;
        this.responseText = responseText;
        this.mock = mock;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getResponseText() {
        return responseText;
    }

    public boolean isMock() {
        return mock;
    }
}
