package com.example.istreampersonalvideoplaylistapp_51c.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.istreampersonalvideoplaylistapp_51c.model.UserEntity;

@Dao
public interface UserDao {
    @Insert
    long insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    UserEntity login(String username, String password);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int usernameExists(String username);
}
