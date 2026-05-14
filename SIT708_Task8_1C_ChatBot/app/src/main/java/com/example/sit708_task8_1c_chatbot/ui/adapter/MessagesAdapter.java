package com.example.sit708_task8_1c_chatbot.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sit708_task8_1c_chatbot.data.entity.Message;
import com.example.sit708_task8_1c_chatbot.databinding.ItemMessageLeftBinding;
import com.example.sit708_task8_1c_chatbot.databinding.ItemMessageRightBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;
    
    private List<Message> messages = new ArrayList<>();
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    
    public void setMessages(List<Message> newMessages) {
        this.messages = newMessages != null ? newMessages : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUserMessage() ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        
        if (viewType == VIEW_TYPE_USER) {
            ItemMessageRightBinding binding = ItemMessageRightBinding.inflate(inflater, parent, false);
            return new UserMessageViewHolder(binding);
        } else {
            ItemMessageLeftBinding binding = ItemMessageLeftBinding.inflate(inflater, parent, false);
            return new BotMessageViewHolder(binding);
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message);
        } else if (holder instanceof BotMessageViewHolder) {
            ((BotMessageViewHolder) holder).bind(message);
        }
    }
    
    @Override
    public int getItemCount() {
        return messages.size();
    }
    
    private class UserMessageViewHolder extends RecyclerView.ViewHolder {
        private ItemMessageRightBinding binding;
        
        public UserMessageViewHolder(ItemMessageRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        
        public void bind(Message message) {
            binding.rightMessageText.setText(message.getContent());
            binding.rightMessageTime.setText(timeFormat.format(new Date(message.getTimestamp())));
        }
    }
    
    private class BotMessageViewHolder extends RecyclerView.ViewHolder {
        private ItemMessageLeftBinding binding;
        
        public BotMessageViewHolder(ItemMessageLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        
        public void bind(Message message) {
            binding.leftMessageText.setText(message.getContent());
            binding.leftMessageTime.setText(timeFormat.format(new Date(message.getTimestamp())));
        }
    }
}
