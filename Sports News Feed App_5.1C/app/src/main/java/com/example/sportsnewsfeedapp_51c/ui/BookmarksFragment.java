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
import com.example.sportsnewsfeedapp_51c.adapter.BookmarksAdapter;
import com.example.sportsnewsfeedapp_51c.databinding.FragmentBookmarksBinding;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;
import com.example.sportsnewsfeedapp_51c.utils.NavigationUtils;
import com.example.sportsnewsfeedapp_51c.viewmodel.BookmarkViewModel;

import java.util.List;

public class BookmarksFragment extends Fragment {

    private FragmentBookmarksBinding binding;
    private BookmarkViewModel bookmarkViewModel;
    private BookmarksAdapter bookmarksAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookmarkViewModel = new ViewModelProvider(requireActivity()).get(BookmarkViewModel.class);

        bookmarksAdapter = new BookmarksAdapter(this::openDetails);
        binding.bookmarksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.bookmarksRecyclerView.setAdapter(bookmarksAdapter);
        binding.backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        bookmarkViewModel.getBookmarks().observe(getViewLifecycleOwner(), this::showBookmarks);
        bookmarkViewModel.refreshBookmarks();
    }

    private void showBookmarks(List<NewsItem> bookmarks) {
        boolean isEmpty = bookmarks == null || bookmarks.isEmpty();
        binding.emptyBookmarksText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.bookmarksRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        bookmarksAdapter.submitList(bookmarks);
    }

    private void openDetails(NewsItem item) {
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_bookmarksFragment_to_detailFragment, NavigationUtils.createNewsBundle(item.getId()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
