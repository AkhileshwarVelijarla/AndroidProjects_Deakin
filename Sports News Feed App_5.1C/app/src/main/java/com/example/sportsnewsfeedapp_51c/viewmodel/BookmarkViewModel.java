package com.example.sportsnewsfeedapp_51c.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sportsnewsfeedapp_51c.model.NewsItem;
import com.example.sportsnewsfeedapp_51c.repository.BookmarkRepository;

import java.util.List;

public class BookmarkViewModel extends AndroidViewModel {

    private final BookmarkRepository repository;
    private final MutableLiveData<List<NewsItem>> bookmarks = new MutableLiveData<>();

    public BookmarkViewModel(@NonNull Application application) {
        super(application);
        repository = new BookmarkRepository(application);
        refreshBookmarks();
    }

    public LiveData<List<NewsItem>> getBookmarks() {
        return bookmarks;
    }

    public boolean isBookmarked(int newsId) {
        return repository.isBookmarked(newsId);
    }

    public void toggleBookmark(int newsId) {
        if (repository.isBookmarked(newsId)) {
            repository.removeBookmark(newsId);
        } else {
            repository.addBookmark(newsId);
        }
        refreshBookmarks();
    }

    public void refreshBookmarks() {
        bookmarks.setValue(repository.getBookmarkedStories());
    }
}
