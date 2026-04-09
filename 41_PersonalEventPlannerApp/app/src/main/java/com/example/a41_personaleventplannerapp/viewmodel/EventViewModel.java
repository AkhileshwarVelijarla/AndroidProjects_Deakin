package com.example.a41_personaleventplannerapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.a41_personaleventplannerapp.R;
import com.example.a41_personaleventplannerapp.data.EventEntity;
import com.example.a41_personaleventplannerapp.data.EventRepository;
import com.example.a41_personaleventplannerapp.util.DateTimeFormatterUtil;
import com.example.a41_personaleventplannerapp.util.EventValidator;
import com.example.a41_personaleventplannerapp.util.OneTimeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository repository;
    private final MediatorLiveData<List<EventEntity>> allEvents = new MediatorLiveData<>();
    private List<EventEntity> cachedEvents = Collections.emptyList();

    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> category = new MutableLiveData<>("");
    private final MutableLiveData<String> location = new MutableLiveData<>("");
    private final MutableLiveData<String> dateTimeText = new MutableLiveData<>("");
    private final MutableLiveData<String> formHeader = new MutableLiveData<>("");
    private final MutableLiveData<String> saveButtonText = new MutableLiveData<>("");
    private final MutableLiveData<String> secondaryButtonText = new MutableLiveData<>("");
    private final MutableLiveData<String> titleError = new MutableLiveData<>();
    private final MutableLiveData<String> dateTimeError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> emptyStateVisible = new MutableLiveData<>(true);
    private final MutableLiveData<OneTimeEvent<String>> messageEvent = new MutableLiveData<>();
    private final MutableLiveData<OneTimeEvent<Boolean>> saveCompleteEvent = new MutableLiveData<>();
    private final MutableLiveData<Integer> activeEventId = new MutableLiveData<>(-1);

    private Long selectedDateTime;

    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = new EventRepository(application);
        allEvents.addSource(repository.getAllEvents(), events -> {
            cachedEvents = events == null ? Collections.emptyList() : events;
            publishUpcomingEvents();
        });
        updateFormMode(false);
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

    public MutableLiveData<String> getSaveButtonText() {
        return saveButtonText;
    }

    public MutableLiveData<String> getSecondaryButtonText() {
        return secondaryButtonText;
    }

    public LiveData<String> getTitleError() {
        return titleError;
    }

    public LiveData<String> getDateTimeError() {
        return dateTimeError;
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

    public void refreshEvents() {
        publishUpcomingEvents();
    }

    public void prepareForm(int eventId) {
        Integer currentId = activeEventId.getValue();
        if (currentId != null && currentId == eventId && (eventId == -1 || !isBlank(title.getValue()))) {
            updateFormMode(eventId > 0);
            return;
        }

        activeEventId.setValue(eventId);
        clearValidationErrors();
        if (eventId <= 0) {
            resetForm();
            return;
        }

        repository.loadEvent(eventId, event -> {
            if (event == null) {
                messageEvent.setValue(new OneTimeEvent<>(getString(R.string.event_not_found_message)));
                resetForm();
                return;
            }

            title.setValue(event.getTitle());
            category.setValue(event.getCategory());
            location.setValue(event.getLocation());
            selectedDateTime = event.getEventDateTime();
            dateTimeText.setValue(DateTimeFormatterUtil.format(event.getEventDateTime()));
            activeEventId.setValue(event.getId());
            updateFormMode(true);
        });
    }

    public void updateSelectedDateTime(long dateTimeMillis) {
        selectedDateTime = dateTimeMillis;
        clearDateTimeError();
        dateTimeText.setValue(DateTimeFormatterUtil.format(dateTimeMillis));
    }

    @Nullable
    public Long getSelectedDateTime() {
        return selectedDateTime;
    }

    public void clearTitleError() {
        titleError.setValue(null);
    }

    public void clearDateTimeError() {
        dateTimeError.setValue(null);
    }

    public void saveEvent() {
        String currentTitle = safeValue(title.getValue()).trim();
        String currentCategory = safeValue(category.getValue()).trim();
        String currentLocation = safeValue(location.getValue()).trim();
        clearValidationErrors();

        if (!EventValidator.isTitleValid(currentTitle)) {
            String message = getString(R.string.validation_title_required);
            titleError.setValue(message);
            messageEvent.setValue(new OneTimeEvent<>(message));
            return;
        }

        if (!EventValidator.isDateSelected(selectedDateTime)) {
            String message = getString(R.string.validation_date_required);
            dateTimeError.setValue(message);
            messageEvent.setValue(new OneTimeEvent<>(message));
            return;
        }

        if (EventValidator.isDateInPast(selectedDateTime)) {
            String message = getString(R.string.validation_future_date);
            dateTimeError.setValue(message);
            messageEvent.setValue(new OneTimeEvent<>(message));
            return;
        }

        EventEntity event = new EventEntity(currentTitle, currentCategory, currentLocation, selectedDateTime);
        Integer eventId = activeEventId.getValue();
        boolean editMode = eventId != null && eventId > 0;
        if (editMode) {
            event.setId(eventId);
            repository.update(event);
            messageEvent.setValue(new OneTimeEvent<>(getString(R.string.event_updated_message)));
        } else {
            repository.insert(event);
            messageEvent.setValue(new OneTimeEvent<>(getString(R.string.event_saved_message)));
        }

        saveCompleteEvent.setValue(new OneTimeEvent(Boolean.TRUE));
        resetForm();
    }

    public void deleteEvent(EventEntity event) {
        repository.delete(event);
        messageEvent.setValue(new OneTimeEvent<>(getString(R.string.event_deleted_message)));
    }

    public void resetForm() {
        activeEventId.setValue(-1);
        title.setValue("");
        category.setValue("");
        location.setValue("");
        selectedDateTime = null;
        dateTimeText.setValue("");
        clearValidationErrors();
        updateFormMode(false);
    }

    private void updateFormMode(boolean editMode) {
        formHeader.setValue(getString(editMode ? R.string.edit_event_header : R.string.add_event_header));
        saveButtonText.setValue(getString(editMode ? R.string.update_button : R.string.save_button));
        secondaryButtonText.setValue(getString(editMode ? R.string.cancel_button : R.string.clear_button));
    }

    private void clearValidationErrors() {
        titleError.setValue(null);
        dateTimeError.setValue(null);
    }

    private void publishUpcomingEvents() {
        long now = System.currentTimeMillis();
        List<EventEntity> upcomingEvents = new ArrayList<>();

        for (EventEntity event : cachedEvents) {
            if (event.getEventDateTime() >= now) {
                upcomingEvents.add(event);
            }
        }

        allEvents.setValue(upcomingEvents);
        emptyStateVisible.setValue(upcomingEvents.isEmpty());
    }

    private String getString(int stringResId) {
        return getApplication().getString(stringResId);
    }

    private String safeValue(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
