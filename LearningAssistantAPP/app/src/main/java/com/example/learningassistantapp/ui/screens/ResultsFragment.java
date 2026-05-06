package com.example.learningassistantapp.ui.screens;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learningassistantapp.R;
import com.example.learningassistantapp.model.LLMUiState;
import com.example.learningassistantapp.model.Question;
import com.example.learningassistantapp.model.QuizResult;
import com.example.learningassistantapp.ui.AppNavigator;
import com.example.learningassistantapp.viewmodel.ResultsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

public class ResultsFragment extends Fragment {
    private static final String ARG_RESULT = "arg_result";
    private ResultsViewModel viewModel;
    private QuizResult result;

    public static ResultsFragment newInstance(QuizResult result) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ResultsViewModel.class);
        result = readResult();

        TextView scoreText = view.findViewById(R.id.resultsScoreText);
        LinearLayout feedbackContainer = view.findViewById(R.id.resultsFeedbackContainer);
        MaterialButton continueButton = view.findViewById(R.id.resultsContinueButton);
        MaterialButton planButton = view.findViewById(R.id.resultsStudyPlanButton);
        MaterialCheckBox simulateFailureCheck = view.findViewById(R.id.resultsSimulateFailureCheck);

        scoreText.setText("Score: " + result.getScore() + " / " + result.getTotalQuestions());
        renderFeedback(feedbackContainer, viewModel.getQuestions(), result);

        continueButton.setOnClickListener(v -> ((AppNavigator) requireActivity()).openHome());
        planButton.setOnClickListener(v -> viewModel.explainMistakes(result, simulateFailureCheck.isChecked()));

        viewModel.getMistakesState().observe(getViewLifecycleOwner(), state ->
                bindLlmState(
                        view,
                        state,
                        R.id.resultsLlmLoading,
                        R.id.resultsPromptText,
                        R.id.resultsResponseText,
                        R.id.resultsErrorText,
                        R.id.resultsRetryButton,
                        () -> viewModel.explainMistakes(result, false)
                )
        );
    }

    private QuizResult readResult() {
        Bundle args = getArguments();
        if (args == null) {
            return new QuizResult(viewModel.getTask().getId(), viewModel.getTask().getTitle(), 0, 3, new java.util.LinkedHashMap<>());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return args.getSerializable(ARG_RESULT, QuizResult.class);
        }
        return (QuizResult) args.getSerializable(ARG_RESULT);
    }

    private void renderFeedback(LinearLayout container, List<Question> questions, QuizResult quizResult) {
        container.removeAllViews();
        for (Question question : questions) {
            Integer selectedIndex = quizResult.getSelectedAnswers().get(question.getId());
            String selectedText = selectedIndex == null ? "No answer selected" : question.getOptions().get(selectedIndex);
            String correctText = question.getOptions().get(question.getCorrectAnswerIndex());
            boolean correct = selectedIndex != null && selectedIndex == question.getCorrectAnswerIndex();

            MaterialCardView cardView = new MaterialCardView(requireContext());
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.bottomMargin = dp(12);
            cardView.setLayoutParams(cardParams);
            cardView.setCardBackgroundColor(getResources().getColor(R.color.card_white, requireContext().getTheme()));
            cardView.setRadius(dp(12));
            cardView.setCardElevation(dp(4));

            LinearLayout inner = new LinearLayout(requireContext());
            inner.setOrientation(LinearLayout.VERTICAL);
            inner.setPadding(dp(16), dp(16), dp(16), dp(16));

            TextView questionText = new TextView(requireContext());
            questionText.setText(question.getQuestionText());
            questionText.setTextColor(getResources().getColor(R.color.text_primary, requireContext().getTheme()));
            questionText.setTypeface(questionText.getTypeface(), android.graphics.Typeface.BOLD);

            TextView selected = new TextView(requireContext());
            selected.setText("Selected answer: " + selectedText);
            selected.setTextColor(getResources().getColor(R.color.text_secondary, requireContext().getTheme()));
            selected.setPadding(0, dp(8), 0, 0);

            TextView correctAnswer = new TextView(requireContext());
            correctAnswer.setText("Correct answer: " + correctText);
            correctAnswer.setTextColor(getResources().getColor(correct ? R.color.success_green : R.color.error_red, requireContext().getTheme()));
            correctAnswer.setPadding(0, dp(4), 0, 0);

            TextView feedback = new TextView(requireContext());
            feedback.setText(correct ? "Feedback: Good job. You identified the main concept." : "Feedback: Revisit the definition and compare keywords more carefully.");
            feedback.setTextColor(getResources().getColor(R.color.text_secondary, requireContext().getTheme()));
            feedback.setPadding(0, dp(4), 0, 0);

            inner.addView(questionText);
            inner.addView(selected);
            inner.addView(correctAnswer);
            inner.addView(feedback);
            cardView.addView(inner);
            container.addView(cardView);
        }
    }

    private void bindLlmState(View root, LLMUiState state, int loadingId, int promptId, int responseId, int errorId, int retryId, Runnable retryAction) {
        View loading = root.findViewById(loadingId);
        TextView prompt = root.findViewById(promptId);
        TextView response = root.findViewById(responseId);
        TextView error = root.findViewById(errorId);
        MaterialButton retry = root.findViewById(retryId);

        loading.setVisibility(state.isLoading() ? View.VISIBLE : View.GONE);
        prompt.setText(state.getPrompt().isEmpty() ? "Prompt will appear here." : state.getPrompt());
        response.setText(state.getResponse().isEmpty() ? "Response will appear here." : state.getResponse());
        error.setText(state.getError());
        error.setVisibility(state.getError().isEmpty() ? View.GONE : View.VISIBLE);
        retry.setVisibility(state.getError().isEmpty() ? View.GONE : View.VISIBLE);
        retry.setOnClickListener(v -> retryAction.run());
    }

    private int dp(int value) {
        return Math.round(value * requireContext().getResources().getDisplayMetrics().density);
    }
}
