package com.example.a41_personaleventplannerapp.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a41_personaleventplannerapp.data.EventEntity;
import com.example.a41_personaleventplannerapp.databinding.ItemEventBinding;
import com.example.a41_personaleventplannerapp.util.DateTimeFormatterUtil;

public class EventAdapter extends ListAdapter<EventEntity, EventAdapter.EventViewHolder> {

    public interface EventActionListener {
        void onEdit(EventEntity event);
        void onDelete(EventEntity event);
    }

    private final EventActionListener listener;

    public EventAdapter(EventActionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<EventEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<EventEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull EventEntity oldItem, @NonNull EventEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull EventEntity oldItem, @NonNull EventEntity newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle())
                            && oldItem.getCategory().equals(newItem.getCategory())
                            && oldItem.getLocation().equals(newItem.getLocation())
                            && oldItem.getEventDateTime() == newItem.getEventDateTime();
                }
            };

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemEventBinding binding = ItemEventBinding.inflate(layoutInflater, parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        private final ItemEventBinding binding;

        EventViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(EventEntity event) {
            binding.titleText.setText(event.getTitle());
            binding.categoryText.setText(
                    event.getCategory().isEmpty() ? "General" : event.getCategory()
            );
            binding.locationText.setText(
                    event.getLocation().isEmpty() ? "Location not specified" : event.getLocation()
            );
            binding.dateTimeText.setText(DateTimeFormatterUtil.format(event.getEventDateTime()));
            binding.editButton.setOnClickListener(v -> listener.onEdit(event));
            binding.deleteButton.setOnClickListener(v -> listener.onDelete(event));
            binding.eventCard.setOnClickListener(v -> listener.onEdit(event));
        }
    }
}
