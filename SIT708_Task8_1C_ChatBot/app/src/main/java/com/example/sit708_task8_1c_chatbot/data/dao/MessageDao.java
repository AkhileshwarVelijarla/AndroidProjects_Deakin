package com.example.sit708_task8_1c_chatbot.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sit708_task8_1c_chatbot.data.entity.Message;

import java.util.List;

@Dao
public interface MessageDao {
    
    @Insert
    void insertMessage(Message message);
    
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    LiveData<List<Message>> getAllMessages();
    
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    List<Message> getAllMessagesSync();
    
    @Delete
    void deleteMessage(Message message);
    
    @Query("DELETE FROM messages")
    void deleteAllMessages();
}
