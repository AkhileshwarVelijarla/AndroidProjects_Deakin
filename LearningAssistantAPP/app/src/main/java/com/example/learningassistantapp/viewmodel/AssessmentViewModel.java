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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssessmentViewModel extends AndroidViewModel {
    private final LearningRepository learningRepository;
    private final LLMRepository llmRepository;
    private final MutableLiveData<LLMUiState> hintState = new MutableLiveData<>(LLMUiState.idle());
    private final MutableLiveData<LLMUiState> explanationState = new MutableLiveData<>(LLMUiState.idle());
    private final Map<String, Integer> selectedAnswers = new LinkedHashMap<>();

    public AssessmentViewModel(@NonNull Application application) {
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

    public void setSelectedAnswer(String questionId, int answerIndex) {
        selectedAnswers.put(questionId, answerIndex);
    }

    public Map<String, Integer> getSelectedAnswers() {
        return selectedAnswers;
    }

    public LiveData<LLMUiState> getHintState() {
        return hintState;
    }

    public LiveData<LLMUiState> getExplanationState() {
        return explanationState;
    }

    public void generateHint(Question question, boolean forceFailure) {
        String prompt = "Act as a learning tutor. Give one short hint for this question without revealing the answer. Question: "
                + question.getQuestionText()
                + ". Student interest: "
                + question.getTopic()
                + ".";
        hintState.setValue(LLMUiState.loading(prompt));
        request(prompt, "hint", forceFailure, hintState);
    }

    public void explainAnswer(Question question, int selectedIndex, boolean forceFailure) {
        String selected = selectedIndex >= 0 && selectedIndex < question.getOptions().size()
                ? question.getOptions().get(selectedIndex) : "No answer selected";
        String correct = question.getOptions().get(question.getCorrectAnswerIndex());
        String prompt = "Explain why the selected answer is correct or incorrect in simple student-friendly language. Question: "
                + question.getQuestionText()
                + ". Selected answer: "
                + selected
                + ". Correct answer: "
                + correct
                + ".";
        explanationState.setValue(LLMUiState.loading(prompt));
        request(prompt, "explain answer", forceFailure, explanationState);
    }

    public QuizResult submitQuiz() {
        return learningRepository.evaluateQuiz(getTask(), selectedAnswers);
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
