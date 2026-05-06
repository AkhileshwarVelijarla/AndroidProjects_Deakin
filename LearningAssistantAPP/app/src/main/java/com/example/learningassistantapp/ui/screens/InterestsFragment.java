package com.example.learningassistantapp.ui.screens;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learningassistantapp.R;
import com.example.learningassistantapp.ui.AppNavigator;
import com.example.learningassistantapp.viewmodel.InterestsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Set;

public class InterestsFragment extends Fragment {
    private InterestsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InterestsViewModel.class);

        ChipGroup chipGroup = view.findViewById(R.id.interestsChipGroup);
        TextView selectedCount = view.findViewById(R.id.selectedCountText);
        MaterialButton nextButton = view.findViewById(R.id.interestsNextButton);

        for (String interest : viewModel.getInterestOptions()) {
            Chip chip = new Chip(requireContext());
            chip.setText(interest);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.button_secondary_bg, requireContext().getTheme())));
            chip.setTextColor(getResources().getColor(R.color.text_primary, requireContext().getTheme()));
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.divider_grey, requireContext().getTheme())));
            chip.setChipStrokeWidth(1f);
            chip.setChecked(viewModel.getSelectedInterests().getValue() != null && viewModel.getSelectedInterests().getValue().contains(interest));
            chip.setOnClickListener(v -> viewModel.toggleInterest(interest));
            chipGroup.addView(chip);
        }

        viewModel.getSelectedInterests().observe(getViewLifecycleOwner(), interests -> {
            updateCount(selectedCount, interests);
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                View child = chipGroup.getChildAt(i);
                if (child instanceof Chip) {
                    Chip chip = (Chip) child;
                    boolean selected = interests.contains(chip.getText().toString());
                    chip.setChecked(selected);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(selected ? R.color.accent_yellow : R.color.button_secondary_bg, requireContext().getTheme())));
                    chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(selected ? R.color.accent_yellow_dark : R.color.divider_grey, requireContext().getTheme())));
                }
            }
        });

        nextButton.setOnClickListener(v -> {
            Set<String> interests = viewModel.getSelectedInterests().getValue();
            if (interests == null || interests.isEmpty()) {
                Toast.makeText(requireContext(), "Select at least one interest.", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.saveSelectedInterests();
            ((AppNavigator) requireActivity()).openHome();
        });
    }

    private void updateCount(TextView selectedCount, Set<String> interests) {
        int count = interests == null ? 0 : interests.size();
        selectedCount.setText("Selected interests: " + count);
    }
}
