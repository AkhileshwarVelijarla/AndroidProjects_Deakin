package com.example.learningassistantapp.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.learningassistantapp.viewmodel.AssessmentViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

public class AssessmentFragment extends Fragment {
    private AssessmentViewModel viewModel;
    private List<Question> questions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assessment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        questions = viewModel.getQuestions();

        TextView title = view.findViewById(R.id.assessmentTitleText);
        TextView description = view.findViewById(R.id.assessmentDescriptionText);
        LinearLayout questionContainer = view.findViewById(R.id.questionContainer);
        MaterialButton generateHintButton = view.findViewById(R.id.generateHintButton);
        MaterialButton explainAnswerButton = view.findViewById(R.id.explainAnswerButton);
        MaterialButton submitButton = view.findViewById(R.id.submitAssessmentButton);
        MaterialCheckBox simulateFailureCheck = view.findViewById(R.id.assessmentSimulateFailureCheck);

        title.setText(viewModel.getTask().getTitle());
        description.setText(viewModel.getTask().getDescription());
        renderQuestions(questionContainer);

        generateHintButton.setOnClickListener(v -> viewModel.generateHint(questions.get(0), simulateFailureCheck.isChecked()));
        explainAnswerButton.setOnClickListener(v -> {
            Integer selected = viewModel.getSelectedAnswers().get("q1");
            viewModel.explainAnswer(questions.get(0), selected == null ? -1 : selected, simulateFailureCheck.isChecked());
        });
        submitButton.setOnClickListener(v -> {
            QuizResult result = viewModel.submitQuiz();
            ((AppNavigator) requireActivity()).openResults(result);
        });

        viewModel.getHintState().observe(getViewLifecycleOwner(), state ->
                bindLlmState(
                        view,
                        state,
                        R.id.hintLoading,
                        R.id.hintPromptText,
                        R.id.hintResponseText,
                        R.id.hintErrorText,
                        R.id.hintRetryButton,
                        () -> viewModel.generateHint(questions.get(0), false)
                )
        );

        viewModel.getExplanationState().observe(getViewLifecycleOwner(), state ->
                bindLlmState(
                        view,
                        state,
                        R.id.explanationLoading,
                        R.id.explanationPromptText,
                        R.id.explanationResponseText,
                        R.id.explanationErrorText,
                        R.id.explanationRetryButton,
                        () -> {
                            Integer selected = viewModel.getSelectedAnswers().get("q1");
                            viewModel.explainAnswer(questions.get(0), selected == null ? -1 : selected, false);
                        }
                )
        );
    }

    private void renderQuestions(LinearLayout container) {
        container.removeAllViews();
        for (int index = 0; index < questions.size(); index++) {
            Question question = questions.get(index);
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

            TextView questionTitle = new TextView(requireContext());
            questionTitle.setText("Question " + (index + 1));
            questionTitle.setTextSize(18f);
            questionTitle.setTextColor(getResources().getColor(R.color.text_primary, requireContext().getTheme()));
            questionTitle.setTypeface(questionTitle.getTypeface(), android.graphics.Typeface.BOLD);

            TextView questionText = new TextView(requireContext());
            questionText.setText(question.getQuestionText());
            questionText.setTextColor(getResources().getColor(R.color.text_secondary, requireContext().getTheme()));
            questionText.setPadding(0, dp(6), 0, dp(10));

            RadioGroup radioGroup = new RadioGroup(requireContext());
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            for (int optionIndex = 0; optionIndex < question.getOptions().size(); optionIndex++) {
                RadioButton radioButton = new RadioButton(requireContext());
                radioButton.setText(question.getOptions().get(optionIndex));
                radioButton.setTextColor(getResources().getColor(R.color.text_primary, requireContext().getTheme()));
                final int answerIndex = optionIndex;
                radioButton.setOnClickListener(v -> viewModel.setSelectedAnswer(question.getId(), answerIndex));
                radioGroup.addView(radioButton);
            }

            inner.addView(questionTitle);
            inner.addView(questionText);
            inner.addView(radioGroup);
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
