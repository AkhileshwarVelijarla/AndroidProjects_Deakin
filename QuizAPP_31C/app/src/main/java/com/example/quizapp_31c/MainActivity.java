package com.example.quizapp_31c;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizapp_31c.databinding.ActivityMainBinding;
import com.example.quizapp_31c.util.Event;
import com.example.quizapp_31c.util.ThemePreference;
import com.example.quizapp_31c.viewmodel.QuizViewModel;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private QuizViewModel viewModel;
    private ThemePreference themePreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        themePreference = new ThemePreference(this);
        AppCompatDelegate.setDefaultNightMode(themePreference.isDarkModeEnabled()
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        bindThemeToggle();
        observeEvents();
    }

    private void bindThemeToggle() {
        binding.themeSwitch.setChecked(themePreference.isDarkModeEnabled());
        binding.themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            themePreference.setDarkModeEnabled(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }

    private void observeEvents() {
        viewModel.getMessageEvent().observe(this, event -> {
            String message = consume(event);
            if (message != null) {
                Snackbar.make(binding.main, message, Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.getFinishEvent().observe(this, event -> {
            Boolean shouldFinish = consume(event);
            if (Boolean.TRUE.equals(shouldFinish)) {
                finish();
            }
        });
    }

    private <T> T consume(Event<T> event) {
        if (event == null) {
            return null;
        }
        return event.getContentIfNotHandled();
    }
}
