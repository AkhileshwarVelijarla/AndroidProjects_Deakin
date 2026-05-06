package com.example.learningassistantapp.model;

public class LLMRequest {
    private final String purpose;
    private final String prompt;
    private final boolean forceFailure;

    public LLMRequest(String purpose, String prompt, boolean forceFailure) {
        this.purpose = purpose;
        this.prompt = prompt;
        this.forceFailure = forceFailure;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getPrompt() {
        return prompt;
    }

    public boolean isForceFailure() {
        return forceFailure;
    }
}
