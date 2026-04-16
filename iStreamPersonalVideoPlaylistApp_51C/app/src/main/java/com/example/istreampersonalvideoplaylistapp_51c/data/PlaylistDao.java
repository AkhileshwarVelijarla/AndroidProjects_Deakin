package com.example.istreampersonalvideoplaylistapp_51c.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.istreampersonalvideoplaylistapp_51c.model.PlaylistEntity;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Insert
    long insertVideo(PlaylistEntity video);

    @Query("SELECT * FROM playlist WHERE userOwnerId = :userId ORDER BY timestamp DESC")
    LiveData<List<PlaylistEntity>> getVideosByUserId(int userId);

    @Delete
    void deleteVideo(PlaylistEntity video);
}
