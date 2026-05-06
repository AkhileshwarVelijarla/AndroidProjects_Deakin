package com.example.lostandfound;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfound.databinding.ItemAdvertBinding;

import java.util.ArrayList;
import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewHolder> {
    public interface OnAdvertClickListener {
        void onAdvertClick(Advert advert);
    }

    private final List<Advert> adverts = new ArrayList<>();
    private final OnAdvertClickListener listener;

    public AdvertAdapter(OnAdvertClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdvertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAdvertBinding binding = ItemAdvertBinding.inflate(inflater, parent, false);
        return new AdvertViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewHolder holder, int position) {
        holder.bind(adverts.get(position));
    }

    @Override
    public int getItemCount() {
        return adverts.size();
    }

    public void setAdverts(List<Advert> newAdverts) {
        adverts.clear();
        if (newAdverts != null) {
            adverts.addAll(newAdverts);
        }
        notifyDataSetChanged();
    }

    class AdvertViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdvertBinding binding;

        AdvertViewHolder(ItemAdvertBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Advert advert) {
            binding.setAdvert(advert);
            binding.getRoot().setOnClickListener(v -> listener.onAdvertClick(advert));
            binding.executePendingBindings();
        }
    }
}
