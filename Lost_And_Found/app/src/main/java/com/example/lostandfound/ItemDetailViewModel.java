package com.example.lostandfound;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ItemDetailViewModel extends AndroidViewModel {
    public final MutableLiveData<Advert> advert = new MutableLiveData<>();

    private final AdvertRepository repository;
    private int advertId;

    public ItemDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new AdvertRepository(application);
    }

    public void loadItem(int id) {
        advertId = id;
        advert.setValue(repository.getItemById(id));
    }

    public boolean deleteItem() {
        return repository.deleteItem(advertId);
    }
}
