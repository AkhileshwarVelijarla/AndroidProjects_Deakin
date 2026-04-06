package com.example.a41_personaleventplannerapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {EventEntity.class}, version = 1, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {

    private static volatile EventDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract EventDao eventDao();

    public static EventDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (EventDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    EventDatabase.class,
                                    "event_database"
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
