package com.example.istreampersonalvideoplaylistapp_51c.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "istream_session";
    private static final String KEY_USER_ID = "logged_in_user_id";
    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserSession(int userId) {
        sharedPreferences.edit().putInt(KEY_USER_ID, userId).apply();
    }

    public int getLoggedInUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public boolean isLoggedIn() {
        return getLoggedInUserId() != -1;
    }

    public void clearSession() {
        sharedPreferences.edit().clear().apply();
    }
}
