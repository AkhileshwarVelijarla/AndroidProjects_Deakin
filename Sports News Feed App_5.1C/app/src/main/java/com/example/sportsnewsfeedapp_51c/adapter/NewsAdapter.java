package com.example.sportsnewsfeedapp_51c.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeedapp_51c.databinding.ItemNewsBinding;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public interface OnNewsClickListener {
        void onNewsClick(NewsItem item);
    }

    private final List<NewsItem> items = new ArrayList<>();
    private final OnNewsClickListener listener;

    public NewsAdapter(OnNewsClickListener listener) {
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
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private final ItemNewsBinding binding;

        NewsViewHolder(ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NewsItem item) {
            binding.newsImage.setImageResource(item.getImageResource());
            binding.newsTitle.setText(item.getTitle());
            binding.newsCategory.setText(item.getCategory());
            binding.newsSummary.setText(item.getShortSummary());
            binding.getRoot().setOnClickListener(v -> listener.onNewsClick(item));
        }
    }
}
