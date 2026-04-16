package com.example.sportsnewsfeedapp_51c.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeedapp_51c.databinding.ItemBookmarkBinding;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder> {

    public interface OnBookmarkClickListener {
        void onBookmarkClick(NewsItem item);
    }

    private final List<NewsItem> items = new ArrayList<>();
    private final OnBookmarkClickListener listener;

    public BookmarksAdapter(OnBookmarkClickListener listener) {
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
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookmarkBinding binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BookmarkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class BookmarkViewHolder extends RecyclerView.ViewHolder {
        private final ItemBookmarkBinding binding;

        BookmarkViewHolder(ItemBookmarkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NewsItem item) {
            binding.bookmarkImage.setImageResource(item.getImageResource());
            binding.bookmarkTitle.setText(item.getTitle());
            binding.bookmarkCategory.setText(item.getCategory());
            binding.bookmarkSummary.setText(item.getShortSummary());
            binding.getRoot().setOnClickListener(v -> listener.onBookmarkClick(item));
        }
    }
}
