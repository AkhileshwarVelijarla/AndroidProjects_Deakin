package com.example.learningassistantapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learningassistantapp.repository.LearningRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class InterestsViewModel extends AndroidViewModel {
    private final LearningRepository repository;
    private final MutableLiveData<Set<String>> selectedInterests = new MutableLiveData<>();

    public InterestsViewModel(@NonNull Application application) {
        super(application);
        repository = new LearningRepository(application);
        selectedInterests.setValue(repository.getSelectedInterests());
    }

    public List<String> getInterestOptions() {
        return repository.getInterestOptions();
    }

    public LiveData<Set<String>> getSelectedInterests() {
        return selectedInterests;
    }

    public void toggleInterest(String interest) {
        Set<String> current = new LinkedHashSet<>(selectedInterests.getValue() == null ? new LinkedHashSet<>() : selectedInterests.getValue());
        if (current.contains(interest)) {
            current.remove(interest);
        } else {
            current.add(interest);
        }
        selectedInterests.setValue(current);
    }

    public void saveSelectedInterests() {
        Set<String> interests = selectedInterests.getValue();
        if (interests != null && !interests.isEmpty()) {
            repository.saveSelectedInterests(interests);
        }
    }
}
