package com.example.istreampersonalvideoplaylistapp_51c.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.istreampersonalvideoplaylistapp_51c.data.AppDatabase;
import com.example.istreampersonalvideoplaylistapp_51c.data.PlaylistDao;
import com.example.istreampersonalvideoplaylistapp_51c.model.PlaylistEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistRepository {
    private final PlaylistDao playlistDao;
    private final ExecutorService executorService;

    public PlaylistRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        playlistDao = database.playlistDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<PlaylistEntity>> getVideosByUserId(int userId) {
        return playlistDao.getVideosByUserId(userId);
    }

    public LiveData<Boolean> insertVideo(int userId, String videoUrl) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        executorService.execute(() -> {
            PlaylistEntity video = new PlaylistEntity(userId, videoUrl, System.currentTimeMillis());
            playlistDao.insertVideo(video);
            result.postValue(true);
        });
        return result;
    }
}
