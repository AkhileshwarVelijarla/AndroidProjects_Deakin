package com.example.learningassistantapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learningassistantapp.model.LLMRequest;
import com.example.learningassistantapp.model.LLMUiState;
import com.example.learningassistantapp.model.LearningTask;
import com.example.learningassistantapp.model.User;
import com.example.learningassistantapp.repository.LLMRepository;
import com.example.learningassistantapp.repository.LearningRepository;

import java.util.List;
import java.util.Set;

public class HomeViewModel extends AndroidViewModel {
    private final LearningRepository learningRepository;
    private final LLMRepository llmRepository;
    private final MutableLiveData<LLMUiState> lessonSummaryState = new MutableLiveData<>(LLMUiState.idle());
    private final MutableLiveData<LLMUiState> studyPlanState = new MutableLiveData<>(LLMUiState.idle());
    private final MutableLiveData<LLMUiState> flashcardState = new MutableLiveData<>(LLMUiState.idle());

    public HomeViewModel(@NonNull Application application) {
        super(application);
        learningRepository = new LearningRepository(application);
        llmRepository = new LLMRepository();
    }

    public User getUser() {
        return learningRepository.getUser();
    }

    public Set<String> getSelectedInterests() {
        return learningRepository.getSelectedInterests();
    }

    public List<LearningTask> getLearningTasks() {
        return learningRepository.getLearningTasks();
    }

    public String getQuizHistorySummary() {
        return learningRepository.getDummyQuizHistorySummary();
    }

    public LiveData<LLMUiState> getStudyPlanState() {
        return studyPlanState;
    }

    public LiveData<LLMUiState> getLessonSummaryState() {
        return lessonSummaryState;
    }

    public LiveData<LLMUiState> getFlashcardState() {
        return flashcardState;
    }

    public void generateLessonSummary(boolean forceFailure) {
        String topic = getLearningTasks().get(0).getTopic();
        String prompt = "Produce a short summary of a lesson for the topic "
                + topic
                + ". Keep it beginner friendly, practical, and under 5 sentences for a student interested in "
                + getSelectedInterests()
                + ".";
        lessonSummaryState.setValue(LLMUiState.loading(prompt));
        request(prompt, "lesson summary", forceFailure, lessonSummaryState);
    }

    public void generateStudyPlan(boolean forceFailure) {
        String prompt = "Create a simple 7-day study plan for a student interested in "
                + getSelectedInterests()
                + ". The student scored "
                + learningRepository.getLastScore()
                + "/"
                + learningRepository.getLastTotal()
                + " in the latest quiz. Quiz history: "
                + learningRepository.getDummyQuizHistorySummary()
                + ". Keep it short and practical.";
        studyPlanState.setValue(LLMUiState.loading(prompt));
        request(prompt, "study plan", forceFailure, studyPlanState);
    }

    public void generateFlashcards(boolean forceFailure) {
        String topic = getSelectedInterests().iterator().next();
        String prompt = "Create 3 beginner-friendly flashcards for the topic " + topic + ". Format as Question and Answer.";
        flashcardState.setValue(LLMUiState.loading(prompt));
        request(prompt, "flashcards", forceFailure, flashcardState);
    }

    private void request(String prompt, String purpose, boolean forceFailure, MutableLiveData<LLMUiState> state) {
        llmRepository.requestCompletion(new LLMRequest(purpose, prompt, forceFailure), new LLMRepository.Callback() {
            @Override
            public void onSuccess(com.example.learningassistantapp.model.LLMResponse response) {
                state.setValue(LLMUiState.success(response.getPrompt(), response.getResponseText()));
            }

            @Override
            public void onFailure(String errorMessage) {
                state.setValue(LLMUiState.error(prompt, errorMessage));
            }
        });
    }
}
