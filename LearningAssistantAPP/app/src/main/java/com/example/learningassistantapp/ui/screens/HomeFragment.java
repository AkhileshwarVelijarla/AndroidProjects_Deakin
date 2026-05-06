package com.example.learningassistantapp.ui.screens;

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
import com.example.learningassistantapp.model.LearningTask;
import com.example.learningassistantapp.ui.AppNavigator;
import com.example.learningassistantapp.viewmodel.HomeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        TextView greeting = view.findViewById(R.id.homeGreetingText);
        TextView interestsSummary = view.findViewById(R.id.homeInterestsSummaryText);
        TextView historySummary = view.findViewById(R.id.homeHistorySummaryText);
        TextView heroBadge = view.findViewById(R.id.homeHeroBadgeText);
        LinearLayout taskContainer = view.findViewById(R.id.taskContainer);
        MaterialButton startAssessmentButton = view.findViewById(R.id.startAssessmentButton);
        MaterialButton lessonSummaryButton = view.findViewById(R.id.lessonSummaryButton);
        MaterialButton flashcardsButton = view.findViewById(R.id.flashcardsButton);
        MaterialButton studyPlanButton = view.findViewById(R.id.studyPlanButton);
        MaterialCheckBox simulateFailureCheck = view.findViewById(R.id.homeSimulateFailureCheck);

        greeting.setText("Hello, " + viewModel.getUser().getUsername());
        interestsSummary.setText("Selected interests: " + viewModel.getSelectedInterests());
        historySummary.setText(viewModel.getQuizHistorySummary());
        heroBadge.setText("Focused topic: " + viewModel.getLearningTasks().get(0).getTopic());
        renderTasks(taskContainer, viewModel.getLearningTasks());
        animateSection(view.findViewById(R.id.homeHeroCard), 0);
        animateSection(taskContainer, 80);
        animateSection(view.findViewById(R.id.homeUtilitiesCard), 140);
        animateSection(view.findViewById(R.id.lessonSummaryCard), 200);
        animateSection(view.findViewById(R.id.flashcardsCard), 260);
        animateSection(view.findViewById(R.id.studyPlanCard), 320);

        startAssessmentButton.setOnClickListener(v -> ((AppNavigator) requireActivity()).openAssessment());
        lessonSummaryButton.setOnClickListener(v -> viewModel.generateLessonSummary(simulateFailureCheck.isChecked()));
        flashcardsButton.setOnClickListener(v -> viewModel.generateFlashcards(simulateFailureCheck.isChecked()));
        studyPlanButton.setOnClickListener(v -> viewModel.generateStudyPlan(simulateFailureCheck.isChecked()));

        viewModel.getLessonSummaryState().observe(getViewLifecycleOwner(), state ->
                bindLlmState(
                        view,
                        state,
                        R.id.lessonSummaryLoading,
                        R.id.lessonSummaryPromptText,
                        R.id.lessonSummaryResponseText,
                        R.id.lessonSummaryErrorText,
                        R.id.lessonSummaryRetryButton,
                        () -> viewModel.generateLessonSummary(false)
                )
        );

        viewModel.getFlashcardState().observe(getViewLifecycleOwner(), state ->
                bindLlmState(
                        view,
                        state,
                        R.id.flashcardsLoading,
                        R.id.flashcardsPromptText,
                        R.id.flashcardsResponseText,
                        R.id.flashcardsErrorText,
                        R.id.flashcardsRetryButton,
                        () -> viewModel.generateFlashcards(false)
                )
        );

        viewModel.getStudyPlanState().observe(getViewLifecycleOwner(), state ->
                bindLlmState(
                        view,
                        state,
                        R.id.studyPlanLoading,
                        R.id.studyPlanPromptText,
                        R.id.studyPlanResponseText,
                        R.id.studyPlanErrorText,
                        R.id.studyPlanRetryButton,
                        () -> viewModel.generateStudyPlan(false)
                )
        );
    }

    private void renderTasks(LinearLayout container, List<LearningTask> tasks) {
        container.removeAllViews();
        for (LearningTask task : tasks) {
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

            TextView title = new TextView(requireContext());
            title.setText(task.getTitle());
            title.setTextSize(18f);
            title.setTextColor(getResources().getColor(R.color.text_primary, requireContext().getTheme()));
            title.setTypeface(title.getTypeface(), android.graphics.Typeface.BOLD);

            TextView description = new TextView(requireContext());
            description.setText(task.getDescription());
            description.setTextColor(getResources().getColor(R.color.text_secondary, requireContext().getTheme()));
            description.setPadding(0, dp(6), 0, 0);

            inner.addView(title);
            inner.addView(description);
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

    private void animateSection(View target, long delay) {
        target.setAlpha(0f);
        target.setTranslationY(dp(18));
        target.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(delay)
                .setDuration(280L)
                .start();
    }

    private int dp(int value) {
        return Math.round(value * requireContext().getResources().getDisplayMetrics().density);
    }
}
