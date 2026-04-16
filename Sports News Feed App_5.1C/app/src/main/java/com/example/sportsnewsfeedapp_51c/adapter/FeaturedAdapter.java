package com.example.sportsnewsfeedapp_51c.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeedapp_51c.databinding.ItemFeaturedMatchBinding;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    public interface OnNewsClickListener {
        void onNewsClick(NewsItem item);
    }

    private final List<NewsItem> items = new ArrayList<>();
    private final OnNewsClickListener listener;

    public FeaturedAdapter(OnNewsClickListener listener) {
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
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeaturedMatchBinding binding = ItemFeaturedMatchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FeaturedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class FeaturedViewHolder extends RecyclerView.ViewHolder {
        private final ItemFeaturedMatchBinding binding;

        FeaturedViewHolder(ItemFeaturedMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NewsItem item) {
            binding.featuredImage.setImageResource(item.getImageResource());
            binding.featuredTitle.setText(item.getTitle());
            binding.featuredCategory.setText(item.getCategory());
            binding.getRoot().setOnClickListener(v -> listener.onNewsClick(item));
        }
    }
}
