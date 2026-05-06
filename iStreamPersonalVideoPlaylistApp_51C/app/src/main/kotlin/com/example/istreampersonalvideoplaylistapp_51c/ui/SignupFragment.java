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
import com.example.istreampersonalvideoplaylistapp_51c.databinding.FragmentSignupBinding;
import com.example.istreampersonalvideoplaylistapp_51c.repository.AuthRepository;
import com.example.istreampersonalvideoplaylistapp_51c.viewmodel.AuthViewModel;

public class SignupFragment extends Fragment {
    private FragmentSignupBinding binding;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.signupButton.setOnClickListener(v -> signup());
        binding.backToLoginButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void signup() {
        String fullName = binding.fullNameEditText.getText().toString();
        String username = binding.usernameEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString();

        authViewModel.signup(fullName, username, password, confirmPassword)
                .observe(getViewLifecycleOwner(), this::handleSignupResult);
    }

    private void handleSignupResult(AuthRepository.SignupResult result) {
        if (result == null) {
            return;
        }

        showMessage(result.getMessage(), result.isSuccess());
        if (result.isSuccess()) {
            ((MainActivity) requireActivity()).openLogin(false);
        }
    }

    private void showMessage(String message, boolean success) {
        binding.messageText.setText(message);
        binding.messageText.setTextColor(getResources().getColor(
                success ? android.R.color.holo_green_dark : android.R.color.holo_red_dark,
                requireContext().getTheme()
        ));
        binding.messageText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
