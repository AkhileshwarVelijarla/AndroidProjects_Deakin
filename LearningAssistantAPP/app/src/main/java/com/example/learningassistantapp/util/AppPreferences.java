package com.example.learningassistantapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.learningassistantapp.model.User;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class AppPreferences {
    private static final String PREFS_NAME = "learning_assistant_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_INTERESTS = "interests";
    private static final String KEY_LAST_SCORE = "last_score";
    private static final String KEY_LAST_TOTAL = "last_total";
    private static final String KEY_LAST_TASK = "last_task";

    private final SharedPreferences preferences;

    public AppPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(User user) {
        preferences.edit()
                .putString(KEY_USERNAME, user.getUsername())
                .putString(KEY_EMAIL, user.getEmail())
                .putString(KEY_PHONE, user.getPhoneNumber())
                .apply();
    }

    public User getUser() {
        String username = preferences.getString(KEY_USERNAME, "Student");
        String email = preferences.getString(KEY_EMAIL, "student@example.com");
        String phone = preferences.getString(KEY_PHONE, "0400000000");
        return new User(username, email, phone);
    }

    public void saveInterests(Set<String> interests) {
        preferences.edit().putStringSet(KEY_INTERESTS, new LinkedHashSet<>(interests)).apply();
    }

    public Set<String> getInterests() {
        Set<String> saved = preferences.getStringSet(KEY_INTERESTS, null);
        if (saved != null && !saved.isEmpty()) {
            return new LinkedHashSet<>(saved);
        }
        return new LinkedHashSet<>(Arrays.asList("Algorithms", "Android Development", "Machine Learning"));
    }

    public void saveLatestQuizResult(String taskTitle, int score, int total) {
        preferences.edit()
                .putString(KEY_LAST_TASK, taskTitle)
                .putInt(KEY_LAST_SCORE, score)
                .putInt(KEY_LAST_TOTAL, total)
                .apply();
    }

    public int getLastScore() {
        return preferences.getInt(KEY_LAST_SCORE, 2);
    }

    public int getLastTotal() {
        return preferences.getInt(KEY_LAST_TOTAL, 3);
    }

    public String getLastTaskTitle() {
        return preferences.getString(KEY_LAST_TASK, "Generated Task 1");
    }
}
