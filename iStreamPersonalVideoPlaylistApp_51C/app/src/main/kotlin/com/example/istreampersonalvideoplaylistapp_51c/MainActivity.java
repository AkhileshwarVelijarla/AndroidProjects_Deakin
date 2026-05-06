package com.example.istreampersonalvideoplaylistapp_51c;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.istreampersonalvideoplaylistapp_51c.databinding.ActivityMainBinding;
import com.example.istreampersonalvideoplaylistapp_51c.ui.HomeFragment;
import com.example.istreampersonalvideoplaylistapp_51c.ui.LoginFragment;
import com.example.istreampersonalvideoplaylistapp_51c.ui.PlaylistFragment;
import com.example.istreampersonalvideoplaylistapp_51c.ui.SignupFragment;
import com.example.istreampersonalvideoplaylistapp_51c.utils.SessionManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(this);
        if (savedInstanceState == null) {
            if (sessionManager.isLoggedIn()) {
                openHome(null, false);
            } else {
                openLogin(false);
            }
        }
    }

    public void openLogin(boolean addToBackStack) {
        replaceFragment(new LoginFragment(), addToBackStack);
    }

    public void openSignup() {
        replaceFragment(new SignupFragment(), true);
    }

    public void openHome(String videoUrl, boolean addToBackStack) {
        replaceFragment(HomeFragment.newInstance(videoUrl), addToBackStack);
    }

    public void openPlaylist() {
        replaceFragment(new PlaylistFragment(), true);
    }

    public void logout() {
        sessionManager.clearSession();
        getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        openLogin(false);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
