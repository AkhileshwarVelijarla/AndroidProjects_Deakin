package com.example.a41_personaleventplannerapp.data;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EventRepository {

    public interface EventCallback {
        void onEventLoaded(EventEntity event);
    }

    private final EventDao eventDao;
    private final LiveData<List<EventEntity>> allEvents;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public EventRepository(Application application) {
        EventDatabase database = EventDatabase.getInstance(application);
        eventDao = database.eventDao();
        allEvents = eventDao.getAllEvents();
    }

    public LiveData<List<EventEntity>> getAllEvents() {
        return allEvents;
    }

    public void loadEvent(int eventId, EventCallback callback) {
        EventDatabase.databaseWriteExecutor.execute(() -> {
            EventEntity event = eventDao.getEventById(eventId);
            mainHandler.post(() -> callback.onEventLoaded(event));
        });
    }

    public void insert(EventEntity event) {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDao.insert(event));
    }

    public void update(EventEntity event) {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDao.update(event));
    }

    public void delete(EventEntity event) {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDao.delete(event));
    }
}
