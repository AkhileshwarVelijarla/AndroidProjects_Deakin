package com.example.quizapp_31c.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemePreference {
    private static final String PREFERENCES_NAME = "quiz_preferences";
    private static final String KEY_DARK_MODE = "dark_mode";

    private final SharedPreferences sharedPreferences;

    public ThemePreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }

    public void setDarkModeEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }
}
