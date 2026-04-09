package com.example.a41_personaleventplannerapp.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a41_personaleventplannerapp.R;
import com.example.a41_personaleventplannerapp.databinding.FragmentAddEventBinding;
import com.example.a41_personaleventplannerapp.util.OneTimeEvent;
import com.example.a41_personaleventplannerapp.viewmodel.EventViewModel;

import java.util.Calendar;

public class AddEditEventFragment extends Fragment {

    private FragmentAddEventBinding binding;
    private EventViewModel viewModel;
    private int eventId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        Bundle arguments = getArguments();
        eventId = arguments == null ? -1 : arguments.getInt("eventId", -1);
        viewModel.prepareForm(eventId);

        setupCategoryDropdown();
        binding.titleEditText.addTextChangedListener(createTextWatcher(viewModel::clearTitleError));
        binding.dateInputLayout.setEndIconOnClickListener(v -> showDateTimePicker());
        binding.dateTimeEditText.setOnClickListener(v -> showDateTimePicker());
        binding.saveButton.setOnClickListener(v -> viewModel.saveEvent());
        binding.clearButton.setOnClickListener(v -> handleSecondaryAction());

        viewModel.getTitleError().observe(getViewLifecycleOwner(), binding.titleInputLayout::setError);
        viewModel.getDateTimeError().observe(getViewLifecycleOwner(), binding.dateInputLayout::setError);
        viewModel.getMessageEvent().observe(getViewLifecycleOwner(), this::showMessageIfNeeded);
        viewModel.getSaveCompleteEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null || event.getContentIfNotHandled() == null) {
                return;
            }

            NavController navController = NavHostFragment.findNavController(this);
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() == R.id.addEventFragment) {
                navController.popBackStack(R.id.eventListFragment, false);
            }
        });
    }

    private void setupCategoryDropdown() {
        String[] categories = getResources().getStringArray(R.array.event_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                categories
        );
        binding.categoryDropdown.setAdapter(adapter);
        binding.categoryDropdown.setKeyListener(null);
        binding.categoryDropdown.setOnClickListener(v -> binding.categoryDropdown.showDropDown());
        binding.categoryDropdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.categoryDropdown.showDropDown();
            }
        });
    }

    private void handleSecondaryAction() {
        if (viewModel.isEditMode()) {
            viewModel.resetForm();
            NavHostFragment.findNavController(this).popBackStack(R.id.eventListFragment, false);
            return;
        }

        viewModel.resetForm();
    }

    private void showDateTimePicker() {
        Calendar current = Calendar.getInstance();
        Long selectedDateTime = viewModel.getSelectedDateTime();
        if (selectedDateTime != null && selectedDateTime > 0L) {
            current.setTimeInMillis(selectedDateTime);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(Calendar.YEAR, year);
                    selected.set(Calendar.MONTH, month);
                    selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireContext(),
                            (timeView, hourOfDay, minute) -> {
                                selected.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selected.set(Calendar.MINUTE, minute);
                                selected.set(Calendar.SECOND, 0);
                                selected.set(Calendar.MILLISECOND, 0);
                                viewModel.updateSelectedDateTime(selected.getTimeInMillis());
                            },
                            current.get(Calendar.HOUR_OF_DAY),
                            current.get(Calendar.MINUTE),
                            false
                    );
                    timePickerDialog.show();
                },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showMessageIfNeeded(OneTimeEvent<String> event) {
        if (event == null) {
            return;
        }

        String message = event.getContentIfNotHandled();
        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private TextWatcher createTextWatcher(Runnable onTextChanged) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextChanged.run();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
