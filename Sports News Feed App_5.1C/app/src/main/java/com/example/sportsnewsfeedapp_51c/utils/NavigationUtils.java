package com.example.sportsnewsfeedapp_51c.utils;

import android.os.Bundle;

public class NavigationUtils {

    public static final String ARG_NEWS_ID = "newsId";

    private NavigationUtils() {
    }

    public static Bundle createNewsBundle(int newsId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NEWS_ID, newsId);
        return bundle;
    }
}
