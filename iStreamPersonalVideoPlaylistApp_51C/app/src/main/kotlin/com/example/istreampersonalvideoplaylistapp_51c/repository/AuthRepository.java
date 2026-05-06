package com.example.istreampersonalvideoplaylistapp_51c.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.istreampersonalvideoplaylistapp_51c.data.AppDatabase;
import com.example.istreampersonalvideoplaylistapp_51c.data.UserDao;
import com.example.istreampersonalvideoplaylistapp_51c.model.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public AuthRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<UserEntity> login(String username, String password) {
        MutableLiveData<UserEntity> result = new MutableLiveData<>();
        executorService.execute(() -> result.postValue(userDao.login(username, password)));
        return result;
    }

    public LiveData<SignupResult> signup(String fullName, String username, String password) {
        MutableLiveData<SignupResult> result = new MutableLiveData<>();
        executorService.execute(() -> {
            if (userDao.usernameExists(username) > 0) {
                result.postValue(new SignupResult(false, "Username already exists", -1));
                return;
            }

            long userId = userDao.insertUser(new UserEntity(fullName, username, password));
            result.postValue(new SignupResult(true, "Sign up successful", (int) userId));
        });
        return result;
    }

    public static class SignupResult {
        private final boolean success;
        private final String message;
        private final int userId;

        public SignupResult(boolean success, String message, int userId) {
            this.success = success;
            this.message = message;
            this.userId = userId;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public int getUserId() {
            return userId;
        }
    }
}
