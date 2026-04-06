package com.example.a41_personaleventplannerapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM events ORDER BY event_date_time ASC")
    LiveData<List<EventEntity>> getAllEvents();

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    EventEntity getEventById(int eventId);

    @Insert
    void insert(EventEntity event);

    @Update
    void update(EventEntity event);

    @Delete
    void delete(EventEntity event);
}
