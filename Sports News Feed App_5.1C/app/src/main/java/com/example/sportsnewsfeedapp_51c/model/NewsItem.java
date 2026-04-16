package com.example.sportsnewsfeedapp_51c.model;

public class NewsItem {

    private final int id;
    private final String title;
    private final String description;
    private final String category;
    private final int imageResource;
    private final boolean featured;

    public NewsItem(int id, String title, String description, String category, int imageResource, boolean featured) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageResource = imageResource;
        this.featured = featured;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getImageResource() {
        return imageResource;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getShortSummary() {
        if (description.length() <= 90) {
            return description;
        }
        return description.substring(0, 87) + "...";
    }
}
