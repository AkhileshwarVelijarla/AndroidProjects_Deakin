package com.example.sportsnewsfeedapp_51c.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sportsnewsfeedapp_51c.model.NewsItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookmarkRepository {

    private static final String PREF_NAME = "sports_bookmarks";
    private static final String KEY_BOOKMARK_IDS = "bookmark_ids";

    private final SharedPreferences preferences;
    private final NewsRepository newsRepository;

    public BookmarkRepository(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        newsRepository = NewsRepository.getInstance();
    }

    public List<NewsItem> getBookmarkedStories() {
        Set<String> ids = preferences.getStringSet(KEY_BOOKMARK_IDS, new HashSet<>());
        List<NewsItem> bookmarks = new ArrayList<>();

        for (String idText : ids) {
            try {
                NewsItem item = newsRepository.getNewsById(Integer.parseInt(idText));
                if (item != null) {
                    bookmarks.add(item);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return bookmarks;
    }

    public boolean isBookmarked(int newsId) {
        Set<String> ids = preferences.getStringSet(KEY_BOOKMARK_IDS, new HashSet<>());
        return ids.contains(String.valueOf(newsId));
    }

    public void addBookmark(int newsId) {
        Set<String> ids = new HashSet<>(preferences.getStringSet(KEY_BOOKMARK_IDS, new HashSet<>()));
        ids.add(String.valueOf(newsId));
        preferences.edit().putStringSet(KEY_BOOKMARK_IDS, ids).apply();
    }

    public void removeBookmark(int newsId) {
        Set<String> ids = new HashSet<>(preferences.getStringSet(KEY_BOOKMARK_IDS, new HashSet<>()));
        ids.remove(String.valueOf(newsId));
        preferences.edit().putStringSet(KEY_BOOKMARK_IDS, ids).apply();
    }
}
