package com.example.lostandfound;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ItemListViewModel extends AndroidViewModel {
    public final MutableLiveData<List<Advert>> items = new MutableLiveData<>();
    public final MutableLiveData<String> selectedCategory = new MutableLiveData<>("All");

    private final AdvertRepository repository;

    public ItemListViewModel(@NonNull Application application) {
        super(application);
        repository = new AdvertRepository(application);
    }

    public void loadItems() {
        items.setValue(repository.getItemsByCategory(selectedCategory.getValue()));
    }

    public void setCategory(String category) {
        selectedCategory.setValue(category);
        loadItems();
    }
}
