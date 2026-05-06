package com.example.learningassistantapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.learningassistantapp.model.QuizResult;
import com.example.learningassistantapp.ui.AppNavigator;
import com.example.learningassistantapp.ui.screens.AssessmentFragment;
import com.example.learningassistantapp.ui.screens.HomeFragment;
import com.example.learningassistantapp.ui.screens.InterestsFragment;
import com.example.learningassistantapp.ui.screens.LoginFragment;
import com.example.learningassistantapp.ui.screens.RegisterFragment;
import com.example.learningassistantapp.ui.screens.ResultsFragment;

public class MainActivity extends AppCompatActivity implements AppNavigator {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            navigateTo(new LoginFragment(), false);
        }
    }

    private void navigateTo(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_soft, R.anim.fade_in_soft, R.anim.slide_out_right)
                    .replace(R.id.main, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_soft, R.anim.fade_out_soft)
                    .replace(R.id.main, fragment)
                    .commit();
        }
    }

    @Override
    public void openRegister() {
        navigateTo(new RegisterFragment(), true);
    }

    @Override
    public void openInterests() {
        navigateTo(new InterestsFragment(), true);
    }

    @Override
    public void openHome() {
        getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        navigateTo(new HomeFragment(), false);
    }

    @Override
    public void openAssessment() {
        navigateTo(new AssessmentFragment(), true);
    }

    @Override
    public void openResults(QuizResult result) {
        navigateTo(ResultsFragment.newInstance(result), true);
    }
}
