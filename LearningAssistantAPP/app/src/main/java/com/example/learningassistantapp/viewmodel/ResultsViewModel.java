package com.example.learningassistantapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learningassistantapp.model.LLMRequest;
import com.example.learningassistantapp.model.LLMUiState;
import com.example.learningassistantapp.model.LearningTask;
import com.example.learningassistantapp.model.Question;
import com.example.learningassistantapp.model.QuizResult;
import com.example.learningassistantapp.repository.LLMRepository;
import com.example.learningassistantapp.repository.LearningRepository;

import java.util.List;
import java.util.Set;

public class ResultsViewModel extends AndroidViewModel {
    private final LearningRepository learningRepository;
    private final LLMRepository llmRepository;
    private final MutableLiveData<LLMUiState> mistakesState = new MutableLiveData<>(LLMUiState.idle());

    public ResultsViewModel(@NonNull Application application) {
        super(application);
        learningRepository = new LearningRepository(application);
        llmRepository = new LLMRepository();
    }

    public LearningTask getTask() {
        return learningRepository.getPrimaryTask();
    }

    public List<Question> getQuestions() {
        return learningRepository.getQuestionsForTask(getTask().getId());
    }

    public Set<String> getSelectedInterests() {
        return learningRepository.getSelectedInterests();
    }

    public LiveData<LLMUiState> getMistakesState() {
        return mistakesState;
    }

    public void explainMistakes(QuizResult result, boolean forceFailure) {
        String prompt = "Explain the student's mistakes briefly and create a simple 7-day study plan for a student interested in "
                + getSelectedInterests()
                + ". The student scored "
                + result.getScore()
                + " in the latest quiz. Keep it short and practical.";
        mistakesState.setValue(LLMUiState.loading(prompt));
        llmRepository.requestCompletion(new LLMRequest("mistakes study plan", prompt, forceFailure), new LLMRepository.Callback() {
            @Override
            public void onSuccess(com.example.learningassistantapp.model.LLMResponse response) {
                mistakesState.setValue(LLMUiState.success(response.getPrompt(), response.getResponseText()));
            }

            @Override
            public void onFailure(String errorMessage) {
                mistakesState.setValue(LLMUiState.error(prompt, errorMessage));
            }
        });
    }
}
