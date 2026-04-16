package com.example.sportsnewsfeedapp_51c.repository;

import com.example.sportsnewsfeedapp_51c.data.DummyNewsData;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewsRepository {

    private static NewsRepository instance;
    private final List<NewsItem> newsItems;

    private NewsRepository() {
        newsItems = DummyNewsData.getNewsItems();
    }

    public static NewsRepository getInstance() {
        if (instance == null) {
            instance = new NewsRepository();
        }
        return instance;
    }

    public List<NewsItem> getAllNews() {
        return new ArrayList<>(newsItems);
    }

    public List<NewsItem> getFeaturedNews() {
        List<NewsItem> featured = new ArrayList<>();
        for (NewsItem item : newsItems) {
            if (item.isFeatured()) {
                featured.add(item);
            }
        }
        return featured;
    }

    public NewsItem getNewsById(int id) {
        for (NewsItem item : newsItems) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public List<NewsItem> filterNews(String query, String category) {
        String safeQuery = query == null ? "" : query.toLowerCase(Locale.ROOT).trim();
        String safeCategory = category == null ? "All" : category;
        List<NewsItem> filtered = new ArrayList<>();

        for (NewsItem item : newsItems) {
            boolean matchesCategory = "All".equals(safeCategory) || item.getCategory().equals(safeCategory);
            boolean matchesQuery = safeQuery.isEmpty()
                    || item.getTitle().toLowerCase(Locale.ROOT).contains(safeQuery)
                    || item.getDescription().toLowerCase(Locale.ROOT).contains(safeQuery);

            if (matchesCategory && matchesQuery) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<NewsItem> getRelatedStories(NewsItem selectedItem) {
        List<NewsItem> related = new ArrayList<>();
        if (selectedItem == null) {
            return related;
        }

        for (NewsItem item : newsItems) {
            if (item.getId() != selectedItem.getId() && item.getCategory().equals(selectedItem.getCategory())) {
                related.add(item);
            }
        }

        if (related.isEmpty()) {
            for (NewsItem item : newsItems) {
                if (item.getId() != selectedItem.getId()) {
                    related.add(item);
                }
            }
        }
        return related;
    }
}
