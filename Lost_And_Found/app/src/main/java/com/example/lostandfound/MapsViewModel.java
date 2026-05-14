package com.example.lostandfound;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MapsViewModel extends AndroidViewModel {
    public final MutableLiveData<List<Advert>> adverts = new MutableLiveData<>();

    private final AdvertRepository repository;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        repository = new AdvertRepository(application);
    }

    public void loadAdverts() {
        adverts.setValue(repository.getAllItems());
    }
}
