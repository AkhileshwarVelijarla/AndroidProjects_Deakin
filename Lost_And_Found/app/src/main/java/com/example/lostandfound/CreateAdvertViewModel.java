package com.example.lostandfound;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CreateAdvertViewModel extends AndroidViewModel {
    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public final MutableLiveData<String> phone = new MutableLiveData<>("");
    public final MutableLiveData<String> description = new MutableLiveData<>("");
    public final MutableLiveData<String> dateText = new MutableLiveData<>("");
    public final MutableLiveData<String> location = new MutableLiveData<>("");
    public final MutableLiveData<String> imageUri = new MutableLiveData<>("");
    public final MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    private final AdvertRepository repository;

    public CreateAdvertViewModel(@NonNull Application application) {
        super(application);
        repository = new AdvertRepository(application);
    }

    public boolean saveAdvert(String postType, String category) {
        String image = clean(imageUri.getValue());
        if (isEmpty(postType)) {
            errorMessage.setValue("Please select whether the advert is Lost or Found.");
            return false;
        }
        if (isEmpty(name.getValue()) || isEmpty(phone.getValue())
                || isEmpty(description.getValue()) || isEmpty(dateText.getValue())
                || isEmpty(location.getValue()) || isEmpty(category) || isEmpty(image)) {
            errorMessage.setValue("Please complete all fields, including date/time and image.");
            return false;
        }

        Advert advert = new Advert();
        advert.setPostType(postType);
        advert.setName(clean(name.getValue()));
        advert.setPhone(clean(phone.getValue()));
        advert.setDescription(clean(description.getValue()));
        advert.setDateText(clean(dateText.getValue()));
        advert.setLocation(clean(location.getValue()));
        advert.setCategory(category);
        advert.setImageUri(image);
        advert.setCreatedTimestamp(System.currentTimeMillis());
        return repository.insertItem(advert) > 0;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
