package com.example.sportsnewsfeedapp_51c.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sportsnewsfeedapp_51c.R;
import com.example.sportsnewsfeedapp_51c.adapter.RelatedStoriesAdapter;
import com.example.sportsnewsfeedapp_51c.databinding.FragmentDetailBinding;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;
import com.example.sportsnewsfeedapp_51c.utils.NavigationUtils;
import com.example.sportsnewsfeedapp_51c.viewmodel.BookmarkViewModel;
import com.example.sportsnewsfeedapp_51c.viewmodel.NewsViewModel;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    private NewsViewModel newsViewModel;
    private BookmarkViewModel bookmarkViewModel;
    private RelatedStoriesAdapter relatedStoriesAdapter;
    private int currentNewsId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        bookmarkViewModel = new ViewModelProvider(requireActivity()).get(BookmarkViewModel.class);

        currentNewsId = requireArguments().getInt(NavigationUtils.ARG_NEWS_ID);
        setupRecyclerView();
        setupClickListeners();
        observeViewModel();

        newsViewModel.loadNewsDetails(currentNewsId);
    }

    private void setupRecyclerView() {
        relatedStoriesAdapter = new RelatedStoriesAdapter(item ->
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_detailFragment_self, NavigationUtils.createNewsBundle(item.getId())));

        binding.relatedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.relatedRecyclerView.setAdapter(relatedStoriesAdapter);
    }

    private void setupClickListeners() {
        binding.backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.bookmarkButton.setOnClickListener(v -> {
            bookmarkViewModel.toggleBookmark(currentNewsId);
            updateBookmarkButton();
        });
    }

    private void observeViewModel() {
        newsViewModel.getSelectedNews().observe(getViewLifecycleOwner(), this::showNewsDetails);
        newsViewModel.getRelatedStories().observe(getViewLifecycleOwner(), relatedStoriesAdapter::submitList);
    }

    private void showNewsDetails(NewsItem item) {
        if (item == null) {
            return;
        }
        currentNewsId = item.getId();
        binding.detailImage.setImageResource(item.getImageResource());
        binding.detailCategory.setText(item.getCategory());
        binding.detailTitle.setText(item.getTitle());
        binding.detailDescription.setText(item.getDescription());
        updateBookmarkButton();
    }

    private void updateBookmarkButton() {
        boolean bookmarked = bookmarkViewModel.isBookmarked(currentNewsId);
        binding.bookmarkButton.setText(bookmarked ? "Bookmarked" : "Bookmark");
        binding.bookmarkButton.setIconResource(bookmarked ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_border);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
