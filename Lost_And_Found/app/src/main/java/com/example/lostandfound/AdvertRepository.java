package com.example.lostandfound;

import android.content.Context;

import java.util.List;

public class AdvertRepository {
    private final AdvertDatabaseHelper databaseHelper;

    public AdvertRepository(Context context) {
        databaseHelper = new AdvertDatabaseHelper(context.getApplicationContext());
    }

    public long insertItem(Advert advert) {
        return databaseHelper.insertItem(advert);
    }

    public List<Advert> getAllItems() {
        return databaseHelper.getAllItems();
    }

    public List<Advert> getItemsByCategory(String category) {
        if (category == null || category.equals("All")) {
            return getAllItems();
        }
        return databaseHelper.getItemsByCategory(category);
    }

    public Advert getItemById(int id) {
        return databaseHelper.getItemById(id);
    }

    public boolean deleteItem(int id) {
        return databaseHelper.deleteItem(id) > 0;
    }
}
