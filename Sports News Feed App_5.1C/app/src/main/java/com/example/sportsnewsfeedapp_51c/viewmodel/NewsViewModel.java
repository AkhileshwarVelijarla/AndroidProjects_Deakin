package com.example.sportsnewsfeedapp_51c.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportsnewsfeedapp_51c.model.NewsItem;
import com.example.sportsnewsfeedapp_51c.repository.NewsRepository;

import java.util.List;

public class NewsViewModel extends ViewModel {

    private final NewsRepository repository;
    private final MutableLiveData<List<NewsItem>> featuredNews = new MutableLiveData<>();
    private final MutableLiveData<List<NewsItem>> latestNews = new MutableLiveData<>();
    private final MutableLiveData<List<NewsItem>> filteredNews = new MutableLiveData<>();
    private final MutableLiveData<NewsItem> selectedNews = new MutableLiveData<>();
    private final MutableLiveData<List<NewsItem>> relatedStories = new MutableLiveData<>();

    public NewsViewModel() {
        repository = NewsRepository.getInstance();
        featuredNews.setValue(repository.getFeaturedNews());
        latestNews.setValue(repository.getAllNews());
        filteredNews.setValue(repository.getAllNews());
    }

    public LiveData<List<NewsItem>> getFeaturedNews() {
        return featuredNews;
    }

    public LiveData<List<NewsItem>> getLatestNews() {
        return latestNews;
    }

    public LiveData<List<NewsItem>> getFilteredNews() {
        return filteredNews;
    }

    public LiveData<NewsItem> getSelectedNews() {
        return selectedNews;
    }

    public LiveData<List<NewsItem>> getRelatedStories() {
        return relatedStories;
    }

    public void filterNews(String query, String category) {
        filteredNews.setValue(repository.filterNews(query, category));
    }

    public void loadNewsDetails(int newsId) {
        NewsItem item = repository.getNewsById(newsId);
        selectedNews.setValue(item);
        relatedStories.setValue(repository.getRelatedStories(item));
    }
}
