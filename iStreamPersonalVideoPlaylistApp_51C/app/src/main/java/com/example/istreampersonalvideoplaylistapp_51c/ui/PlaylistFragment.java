package com.example.istreampersonalvideoplaylistapp_51c.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.istreampersonalvideoplaylistapp_51c.MainActivity;
import com.example.istreampersonalvideoplaylistapp_51c.adapter.PlaylistAdapter;
import com.example.istreampersonalvideoplaylistapp_51c.databinding.FragmentPlaylistBinding;
import com.example.istreampersonalvideoplaylistapp_51c.utils.SessionManager;
import com.example.istreampersonalvideoplaylistapp_51c.viewmodel.PlaylistViewModel;

public class PlaylistFragment extends Fragment {
    private FragmentPlaylistBinding binding;
    private PlaylistAdapter adapter;
    private PlaylistViewModel playlistViewModel;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        sessionManager = new SessionManager(requireContext());

        adapter = new PlaylistAdapter(url -> ((MainActivity) requireActivity()).openHome(url, true));
        binding.playlistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.playlistRecyclerView.setAdapter(adapter);

        binding.backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.logoutButton.setOnClickListener(v -> ((MainActivity) requireActivity()).logout());

        int userId = sessionManager.getLoggedInUserId();
        if (userId == -1) {
            ((MainActivity) requireActivity()).logout();
            return;
        }

        playlistViewModel.getVideosByUserId(userId).observe(getViewLifecycleOwner(), videos -> {
            adapter.setVideos(videos);
            binding.emptyText.setVisibility(videos == null || videos.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
