package com.example.istreampersonalvideoplaylistapp_51c.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istreampersonalvideoplaylistapp_51c.databinding.ItemPlaylistBinding;
import com.example.istreampersonalvideoplaylistapp_51c.model.PlaylistEntity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private final List<PlaylistEntity> videos = new ArrayList<>();
    private final OnVideoClickListener listener;

    public PlaylistAdapter(OnVideoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void setVideos(List<PlaylistEntity> newVideos) {
        videos.clear();
        if (newVideos != null) {
            videos.addAll(newVideos);
        }
        notifyDataSetChanged();
    }

    class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlaylistBinding binding;

        PlaylistViewHolder(ItemPlaylistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PlaylistEntity video) {
            binding.urlText.setText(video.getVideoUrl());
            String savedDate = DateFormat.getDateTimeInstance().format(new Date(video.getTimestamp()));
            binding.savedText.setText("Saved " + savedDate);
            binding.getRoot().setOnClickListener(v -> listener.onVideoClick(video.getVideoUrl()));
        }
    }

    public interface OnVideoClickListener {
        void onVideoClick(String videoUrl);
    }
}
