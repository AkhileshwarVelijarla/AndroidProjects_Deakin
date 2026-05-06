package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AdvertDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lost_found.db";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_NAME = "adverts";
    private static final String COL_ID = "id";
    private static final String COL_POST_TYPE = "post_type";
    private static final String COL_NAME = "name";
    private static final String COL_PHONE = "phone";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_DATE_TEXT = "date_text";
    private static final String COL_LOCATION = "location";
    private static final String COL_CATEGORY = "category";
    private static final String COL_IMAGE_URI = "image_uri";
    private static final String COL_CREATED_TIMESTAMP = "created_timestamp";

    public AdvertDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_POST_TYPE + " TEXT NOT NULL, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_PHONE + " TEXT NOT NULL, "
                + COL_DESCRIPTION + " TEXT NOT NULL, "
                + COL_DATE_TEXT + " TEXT NOT NULL, "
                + COL_LOCATION + " TEXT NOT NULL, "
                + COL_CATEGORY + " TEXT NOT NULL, "
                + COL_IMAGE_URI + " TEXT NOT NULL, "
                + COL_CREATED_TIMESTAMP + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertItem(Advert advert) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_POST_TYPE, advert.getPostType());
        values.put(COL_NAME, advert.getName());
        values.put(COL_PHONE, advert.getPhone());
        values.put(COL_DESCRIPTION, advert.getDescription());
        values.put(COL_DATE_TEXT, advert.getDateText());
        values.put(COL_LOCATION, advert.getLocation());
        values.put(COL_CATEGORY, advert.getCategory());
        values.put(COL_IMAGE_URI, advert.getImageUri());
        values.put(COL_CREATED_TIMESTAMP, advert.getCreatedTimestamp());
        return db.insert(TABLE_NAME, null, values);
    }

    public List<Advert> getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
                COL_CREATED_TIMESTAMP + " DESC");
        return readItems(cursor);
    }

    public List<Advert> getItemsByCategory(String category) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_CATEGORY + " = ?",
                new String[]{category}, null, null, COL_CREATED_TIMESTAMP + " DESC");
        return readItems(cursor);
    }

    public Advert getItemById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return readItem(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public int deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    private List<Advert> readItems(Cursor cursor) {
        List<Advert> items = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                items.add(readItem(cursor));
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    private Advert readItem(Cursor cursor) {
        return new Advert(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_POST_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE_TEXT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_URI)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_TIMESTAMP))
        );
    }
}
