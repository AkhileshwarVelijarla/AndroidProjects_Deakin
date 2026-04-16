package com.example.sportsnewsfeedapp_51c.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sportsnewsfeedapp_51c.R;
import com.example.sportsnewsfeedapp_51c.adapter.FeaturedAdapter;
import com.example.sportsnewsfeedapp_51c.adapter.NewsAdapter;
import com.example.sportsnewsfeedapp_51c.databinding.FragmentHomeBinding;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;
import com.example.sportsnewsfeedapp_51c.utils.NavigationUtils;
import com.example.sportsnewsfeedapp_51c.viewmodel.NewsViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NewsViewModel newsViewModel;
    private FeaturedAdapter featuredAdapter;
    private NewsAdapter newsAdapter;
    private String selectedCategory = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        setupRecyclerViews();
        setupFilter();
        setupClickListeners();
        observeViewModel();
    }

    private void setupRecyclerViews() {
        featuredAdapter = new FeaturedAdapter(this::openDetails);
        newsAdapter = new NewsAdapter(this::openDetails);

        binding.featuredRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.featuredRecyclerView.setAdapter(featuredAdapter);

        binding.latestNewsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.latestNewsRecyclerView.setAdapter(newsAdapter);
    }

    private void setupFilter() {
        String[] categories = {"All", "Football", "Basketball", "Cricket"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        binding.categorySpinner.setAdapter(spinnerAdapter);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories[position];
                updateFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "All";
                updateFilter();
            }
        });

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupClickListeners() {
        binding.bookmarksButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_bookmarksFragment));
    }

    private void observeViewModel() {
        newsViewModel.getFeaturedNews().observe(getViewLifecycleOwner(), featuredAdapter::submitList);
        newsViewModel.getFilteredNews().observe(getViewLifecycleOwner(), newsAdapter::submitList);
    }

    private void updateFilter() {
        if (newsViewModel != null && binding != null) {
            newsViewModel.filterNews(binding.searchEditText.getText().toString(), selectedCategory);
        }
    }

    private void openDetails(NewsItem item) {
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_homeFragment_to_detailFragment, NavigationUtils.createNewsBundle(item.getId()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
