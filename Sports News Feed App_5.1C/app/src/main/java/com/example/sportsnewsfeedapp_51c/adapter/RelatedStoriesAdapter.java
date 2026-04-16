package com.example.sportsnewsfeedapp_51c.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeedapp_51c.databinding.ItemRelatedStoryBinding;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class RelatedStoriesAdapter extends RecyclerView.Adapter<RelatedStoriesAdapter.RelatedViewHolder> {

    public interface OnRelatedClickListener {
        void onRelatedClick(NewsItem item);
    }

    private final List<NewsItem> items = new ArrayList<>();
    private final OnRelatedClickListener listener;

    public RelatedStoriesAdapter(OnRelatedClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<NewsItem> newsItems) {
        items.clear();
        if (newsItems != null) {
            items.addAll(newsItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRelatedStoryBinding binding = ItemRelatedStoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RelatedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RelatedViewHolder extends RecyclerView.ViewHolder {
        private final ItemRelatedStoryBinding binding;

        RelatedViewHolder(ItemRelatedStoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NewsItem item) {
            binding.relatedImage.setImageResource(item.getImageResource());
            binding.relatedTitle.setText(item.getTitle());
            binding.relatedCategory.setText(item.getCategory());
            binding.getRoot().setOnClickListener(v -> listener.onRelatedClick(item));
        }
    }
}
