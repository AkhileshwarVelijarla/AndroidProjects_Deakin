package com.example.a41_personaleventplannerapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.a41_personaleventplannerapp.R;
import com.example.a41_personaleventplannerapp.data.EventEntity;
import com.example.a41_personaleventplannerapp.databinding.FragmentEventListBinding;
import com.example.a41_personaleventplannerapp.util.OneTimeEvent;
import com.example.a41_personaleventplannerapp.viewmodel.EventViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EventListFragment extends Fragment implements EventAdapter.EventActionListener {

    private FragmentEventListBinding binding;
    private EventViewModel viewModel;
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        adapter = new EventAdapter(this);
        binding.eventsRecyclerView.setAdapter(adapter);
        viewModel.refreshEvents();

        viewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.submitList(events == null ? new ArrayList<>() : new ArrayList<>(events));
            viewModel.setEmptyStateVisible(events == null || events.isEmpty());
        });

        viewModel.getMessageEvent().observe(getViewLifecycleOwner(), this::showMessageIfNeeded);
    }

    @Override
    public void onEdit(EventEntity event) {
        Bundle args = new Bundle();
        args.putInt("eventId", event.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.addEventFragment, args);
    }

    @Override
    public void onDelete(EventEntity event) {
        viewModel.deleteEvent(event);
    }

    private void showMessageIfNeeded(OneTimeEvent<String> event) {
        if (event == null) {
            return;
        }

        String message = event.getContentIfNotHandled();
        if (message != null) {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshEvents();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
