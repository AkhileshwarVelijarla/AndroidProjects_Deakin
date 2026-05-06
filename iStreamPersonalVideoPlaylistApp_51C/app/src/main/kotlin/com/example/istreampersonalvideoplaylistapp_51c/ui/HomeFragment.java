package com.example.istreampersonalvideoplaylistapp_51c.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.istreampersonalvideoplaylistapp_51c.MainActivity;
import com.example.istreampersonalvideoplaylistapp_51c.databinding.FragmentHomeBinding;
import com.example.istreampersonalvideoplaylistapp_51c.utils.SessionManager;
import com.example.istreampersonalvideoplaylistapp_51c.utils.YouTubeUtils;
import com.example.istreampersonalvideoplaylistapp_51c.viewmodel.PlaylistViewModel;

public class HomeFragment extends Fragment {
    private static final String ARG_VIDEO_URL = "video_url";
    private FragmentHomeBinding binding;
    private PlaylistViewModel playlistViewModel;
    private SessionManager sessionManager;
    private String currentValidUrl;

    public static HomeFragment newInstance(String videoUrl) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_URL, videoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        sessionManager = new SessionManager(requireContext());

        setupWebView();
        binding.playButton.setOnClickListener(v -> playFromInput());
        binding.addToPlaylistButton.setOnClickListener(v -> addToPlaylist());
        binding.myPlaylistButton.setOnClickListener(v -> ((MainActivity) requireActivity()).openPlaylist());
        binding.logoutButton.setOnClickListener(v -> ((MainActivity) requireActivity()).logout());

        if (getArguments() != null) {
            String selectedUrl = getArguments().getString(ARG_VIDEO_URL);
            if (selectedUrl != null && !selectedUrl.isEmpty()) {
                binding.videoUrlEditText.setText(selectedUrl);
                playVideo(selectedUrl);
            }
        }
    }

    private void setupWebView() {
        WebSettings settings = binding.youtubeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        binding.youtubeWebView.setWebViewClient(new WebViewClient());
        binding.youtubeWebView.setWebChromeClient(new WebChromeClient());
    }

    private void playFromInput() {
        String url = binding.videoUrlEditText.getText().toString().trim();
        playVideo(url);
    }

    private void playVideo(String url) {
        String videoId = YouTubeUtils.extractVideoId(url);
        if (videoId == null) {
            currentValidUrl = null;
            showMessage("Please enter a valid YouTube video URL");
            return;
        }

        currentValidUrl = url;
        binding.messageText.setVisibility(View.GONE);
        binding.youtubeWebView.loadDataWithBaseURL(
                "https://www.youtube.com",
                YouTubeUtils.buildEmbedHtml(videoId),
                "text/html",
                "UTF-8",
                null
        );
    }

    private void addToPlaylist() {
        String url = binding.videoUrlEditText.getText().toString().trim();
        String videoId = YouTubeUtils.extractVideoId(url);
        if (videoId == null) {
            showMessage("Only valid YouTube URLs can be added to your playlist");
            return;
        }

        currentValidUrl = url;
        int userId = sessionManager.getLoggedInUserId();
        if (userId == -1) {
            ((MainActivity) requireActivity()).logout();
            return;
        }

        playlistViewModel.addVideoToPlaylist(userId, currentValidUrl)
                .observe(getViewLifecycleOwner(), saved -> showMessage("Video added to playlist"));
    }

    private void showMessage(String message) {
        binding.messageText.setText(message);
        binding.messageText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        if (binding != null) {
            binding.youtubeWebView.destroy();
        }
        super.onDestroyView();
        binding = null;
    }
}
