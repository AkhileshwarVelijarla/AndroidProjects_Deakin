package com.example.istreampersonalvideoplaylistapp_51c.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.istreampersonalvideoplaylistapp_51c.model.PlaylistEntity;
import com.example.istreampersonalvideoplaylistapp_51c.model.UserEntity;

@Database(entities = {UserEntity.class, PlaylistEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public abstract UserDao userDao();

    public abstract PlaylistDao playlistDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "istream_database"
                    ).build();
                }
            }
        }
        return instance;
    }
}
