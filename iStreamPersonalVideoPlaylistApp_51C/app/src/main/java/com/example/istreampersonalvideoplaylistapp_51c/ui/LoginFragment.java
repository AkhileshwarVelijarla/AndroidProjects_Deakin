package com.example.istreampersonalvideoplaylistapp_51c.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.istreampersonalvideoplaylistapp_51c.MainActivity;
import com.example.istreampersonalvideoplaylistapp_51c.databinding.FragmentLoginBinding;
import com.example.istreampersonalvideoplaylistapp_51c.model.UserEntity;
import com.example.istreampersonalvideoplaylistapp_51c.utils.SessionManager;
import com.example.istreampersonalvideoplaylistapp_51c.viewmodel.AuthViewModel;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sessionManager = new SessionManager(requireContext());

        binding.loginButton.setOnClickListener(v -> login());
        binding.goToSignupButton.setOnClickListener(v -> ((MainActivity) requireActivity()).openSignup());
    }

    private void login() {
        String username = binding.usernameEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Username and password are required");
            return;
        }

        authViewModel.login(username, password).observe(getViewLifecycleOwner(), user -> handleLoginResult(user));
    }

    private void handleLoginResult(UserEntity user) {
        if (user == null) {
            showMessage("Login failed. Check your username and password.");
            return;
        }

        sessionManager.saveUserSession(user.getUserId());
        ((MainActivity) requireActivity()).openHome(null, false);
    }

    private void showMessage(String message) {
        binding.errorText.setText(message);
        binding.errorText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
