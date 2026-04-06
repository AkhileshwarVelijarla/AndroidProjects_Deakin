package com.example.a41_personaleventplannerapp.util;

public class OneTimeEvent<T> {

    private final T content;
    private boolean handled;

    public OneTimeEvent(T content) {
        this.content = content;
    }

    public T getContentIfNotHandled() {
        if (handled) {
            return null;
        }
        handled = true;
        return content;
    }
}
