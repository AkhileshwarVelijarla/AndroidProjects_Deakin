package com.example.istreampersonalvideoplaylistapp_51c.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.istreampersonalvideoplaylistapp_51c.model.PlaylistEntity;
import com.example.istreampersonalvideoplaylistapp_51c.repository.PlaylistRepository;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {
    private final PlaylistRepository repository;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaylistRepository(application);
    }

    public LiveData<List<PlaylistEntity>> getVideosByUserId(int userId) {
        return repository.getVideosByUserId(userId);
    }

    public LiveData<Boolean> addVideoToPlaylist(int userId, String videoUrl) {
        return repository.insertVideo(userId, videoUrl);
    }
}
