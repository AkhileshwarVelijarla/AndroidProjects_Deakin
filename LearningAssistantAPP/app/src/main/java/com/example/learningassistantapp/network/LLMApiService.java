package com.example.learningassistantapp.network;

import android.os.Handler;
import android.os.Looper;

import com.example.learningassistantapp.BuildConfig;
import com.example.learningassistantapp.model.LLMRequest;
import com.example.learningassistantapp.model.LLMResponse;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LLMApiService {
    public interface Callback {
        void onSuccess(LLMResponse response);
        void onFailure(String errorMessage);
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public void generateResponse(LLMRequest request, Callback callback) {
        executorService.execute(() -> {
            try {
                Thread.sleep(1100L);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            mainHandler.post(() -> {
                if (request.isForceFailure()) {
                    callback.onFailure("Mock API failure triggered. Tap Retry to request the same prompt again.");
                    return;
                }

                // Insert a real network call here when LLM_API_KEY and LLM_BASE_URL are provided.
                String prompt = request.getPrompt();
                String responseText = createMockResponse(request.getPurpose(), prompt);
                callback.onSuccess(new LLMResponse(prompt, responseText, BuildConfig.MOCK_LLM || BuildConfig.LLM_BASE_URL.isBlank()));
            });
        });
    }

    private String createMockResponse(String purpose, String prompt) {
        String normalizedPurpose = purpose.toLowerCase(Locale.ROOT);
        if (normalizedPurpose.contains("hint")) {
            return "Hint: focus on the core definition first, then eliminate options that describe implementation details instead of the main concept.";
        }
        if (normalizedPurpose.contains("study")) {
            return "Day 1: Review weak quiz topics.\nDay 2: Practice Algorithms problems.\nDay 3: Revise Android UI basics.\nDay 4: Study Machine Learning terms.\nDay 5: Complete one short quiz.\nDay 6: Create flashcards from mistakes.\nDay 7: Summarise what improved and retry the assessment.";
        }
        if (normalizedPurpose.contains("summary")) {
            return "Lesson summary: an algorithm is a clear sequence of steps used to solve a problem. Good solutions are precise, repeatable, and efficient. Start by understanding the input, expected output, and the decision steps in between. Then practise with one small example before attempting a larger task.";
        }
        if (normalizedPurpose.contains("flashcard")) {
            return "1. Q: What is a data structure? A: A way to organise data for efficient access.\n2. Q: What does an algorithm describe? A: A step-by-step method to solve a problem.\n3. Q: Why use testing? A: It checks correctness and reduces regressions.";
        }
        if (normalizedPurpose.contains("mistake") || normalizedPurpose.contains("explain")) {
            return "Your answer missed the key concept. Compare the selected option with the correct one by asking which choice best matches the definition, not just a related keyword.";
        }
        return "Mock response generated for prompt:\n" + prompt;
    }
}
