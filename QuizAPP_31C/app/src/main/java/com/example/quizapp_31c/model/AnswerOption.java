package com.example.quizapp_31c.model;

public class AnswerOption {
    private final String label;
    private final boolean enabled;
    private final OptionState state;

    public AnswerOption(String label, boolean enabled, OptionState state) {
        this.label = label;
        this.enabled = enabled;
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public OptionState getState() {
        return state;
    }
}
