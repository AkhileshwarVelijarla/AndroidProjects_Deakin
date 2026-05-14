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
    public double selectedLatitude;
    public double selectedLongitude;

    private final AdvertRepository repository;

    public CreateAdvertViewModel(@NonNull Application application) {
        super(application);
        repository = new AdvertRepository(application);
    }

    public boolean saveAdvert(String postType, String category) {
        if (isEmpty(postType)) {
            errorMessage.setValue("Please select whether the advert is Lost or Found.");
            return false;
        }
        if (isEmpty(name.getValue()) || isEmpty(phone.getValue())
                || isEmpty(description.getValue()) || isEmpty(dateText.getValue())
                || isEmpty(location.getValue()) || isEmpty(category)) {
            errorMessage.setValue("Please complete all advert fields.");
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
        advert.setImageUri(clean(imageUri.getValue()));
        advert.setLatitude(selectedLatitude);
        advert.setLongitude(selectedLongitude);
        advert.setCreatedTimestamp(System.currentTimeMillis());
        return repository.insertItem(advert) > 0;
    }

    public void setSelectedLocation(String locationText, double latitude, double longitude) {
        location.setValue(clean(locationText));
        selectedLatitude = latitude;
        selectedLongitude = longitude;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
