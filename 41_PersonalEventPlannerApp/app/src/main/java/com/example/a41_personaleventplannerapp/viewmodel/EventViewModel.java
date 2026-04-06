package com.example.a41_personaleventplannerapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.a41_personaleventplannerapp.data.EventEntity;
import com.example.a41_personaleventplannerapp.data.EventRepository;
import com.example.a41_personaleventplannerapp.util.DateTimeFormatterUtil;
import com.example.a41_personaleventplannerapp.util.EventValidator;
import com.example.a41_personaleventplannerapp.util.OneTimeEvent;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository repository;
    private final LiveData<List<EventEntity>> allEvents;

    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> category = new MutableLiveData<>("");
    private final MutableLiveData<String> location = new MutableLiveData<>("");
    private final MutableLiveData<String> dateTimeText = new MutableLiveData<>("");
    private final MutableLiveData<String> formHeader = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> emptyStateVisible = new MutableLiveData<>(true);
    private final MutableLiveData<OneTimeEvent<String>> messageEvent = new MutableLiveData<>();
    private final MutableLiveData<OneTimeEvent<Boolean>> saveCompleteEvent = new MutableLiveData<>();
    private final MutableLiveData<Integer> activeEventId = new MutableLiveData<>(-1);

    private Long selectedDateTime;

    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = new EventRepository(application);
        allEvents = repository.getAllEvents();
    }

    public LiveData<List<EventEntity>> getAllEvents() {
        return allEvents;
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getCategory() {
        return category;
    }

    public MutableLiveData<String> getLocation() {
        return location;
    }

    public MutableLiveData<String> getDateTimeText() {
        return dateTimeText;
    }

    public MutableLiveData<String> getFormHeader() {
        return formHeader;
    }

    public MutableLiveData<Boolean> getEmptyStateVisible() {
        return emptyStateVisible;
    }

    public LiveData<OneTimeEvent<String>> getMessageEvent() {
        return messageEvent;
    }

    public LiveData<OneTimeEvent<Boolean>> getSaveCompleteEvent() {
        return saveCompleteEvent;
    }

    public boolean isEditMode() {
        Integer eventId = activeEventId.getValue();
        return eventId != null && eventId > 0;
    }

    public void setEmptyStateVisible(boolean visible) {
        emptyStateVisible.setValue(visible);
    }

    public void prepareForm(int eventId) {
        Integer currentId = activeEventId.getValue();
        if (currentId != null && currentId == eventId && (eventId == -1 || !isBlank(title.getValue()))) {
            updateHeader();
            return;
        }

        activeEventId.setValue(eventId);
        if (eventId <= 0) {
            resetForm();
            return;
        }

        repository.loadEvent(eventId, event -> {
            if (event == null) {
                messageEvent.postValue(new OneTimeEvent<>("Event not found."));
                resetForm();
                return;
            }

            title.postValue(event.getTitle());
            category.postValue(event.getCategory());
            location.postValue(event.getLocation());
            selectedDateTime = event.getEventDateTime();
            dateTimeText.postValue(DateTimeFormatterUtil.format(event.getEventDateTime()));
            formHeader.postValue("Edit Event");
            activeEventId.postValue(event.getId());
        });
    }

    public void updateSelectedDateTime(long dateTimeMillis) {
        selectedDateTime = dateTimeMillis;
        dateTimeText.setValue(DateTimeFormatterUtil.format(dateTimeMillis));
    }

    public void saveEvent() {
        String currentTitle = safeValue(title.getValue()).trim();
        String currentCategory = safeValue(category.getValue()).trim();
        String currentLocation = safeValue(location.getValue()).trim();

        if (!EventValidator.isTitleValid(currentTitle)) {
            messageEvent.setValue(new OneTimeEvent<>("Title is required."));
            return;
        }

        if (!EventValidator.isDateSelected(selectedDateTime)) {
            messageEvent.setValue(new OneTimeEvent<>("Please choose a date and time."));
            return;
        }

        if (EventValidator.isDateInPast(selectedDateTime)) {
            messageEvent.setValue(new OneTimeEvent<>("Past dates are not allowed."));
            return;
        }

        EventEntity event = new EventEntity(currentTitle, currentCategory, currentLocation, selectedDateTime);
        Integer eventId = activeEventId.getValue();
        boolean editMode = eventId != null && eventId > 0;
        if (editMode) {
            event.setId(eventId);
            repository.update(event);
            messageEvent.setValue(new OneTimeEvent<>("Event updated."));
        } else {
            repository.insert(event);
            messageEvent.setValue(new OneTimeEvent<>("Event saved."));
        }

        saveCompleteEvent.setValue(new OneTimeEvent(Boolean.TRUE));
        resetForm();
    }

    public void deleteEvent(EventEntity event) {
        repository.delete(event);
        messageEvent.setValue(new OneTimeEvent<>("Event deleted."));
    }

    public void resetForm() {
        activeEventId.setValue(-1);
        title.setValue("");
        category.setValue("");
        location.setValue("");
        selectedDateTime = null;
        dateTimeText.setValue("");
        updateHeader();
    }

    private void updateHeader() {
        formHeader.setValue(isEditMode() ? "Edit Event" : "Add Event");
    }

    private String safeValue(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
