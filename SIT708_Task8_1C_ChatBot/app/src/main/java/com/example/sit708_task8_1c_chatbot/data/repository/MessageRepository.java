package com.example.sit708_task8_1c_chatbot.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.sit708_task8_1c_chatbot.data.dao.MessageDao;
import com.example.sit708_task8_1c_chatbot.data.database.ChatDatabase;
import com.example.sit708_task8_1c_chatbot.data.entity.Message;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageRepository {
    
    private final MessageDao messageDao;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    
    public MessageRepository(Context context) {
        ChatDatabase database = ChatDatabase.getInstance(context);
        messageDao = database.messageDao();
    }
    
    public void insertMessage(Message message) {
        databaseExecutor.execute(() -> messageDao.insertMessage(message));
    }
    
    public LiveData<List<Message>> getAllMessages() {
        return messageDao.getAllMessages();
    }
    
    public List<Message> getAllMessagesSync() {
        return messageDao.getAllMessagesSync();
    }
    
    public void deleteMessage(Message message) {
        databaseExecutor.execute(() -> messageDao.deleteMessage(message));
    }
    
    public void deleteAllMessages() {
        databaseExecutor.execute(messageDao::deleteAllMessages);
    }
}
