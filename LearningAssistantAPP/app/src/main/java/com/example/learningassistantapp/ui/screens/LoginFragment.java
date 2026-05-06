package com.example.learningassistantapp.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learningassistantapp.R;
import com.example.learningassistantapp.ui.AppNavigator;
import com.example.learningassistantapp.viewmodel.LoginViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        TextInputEditText usernameInput = view.findViewById(R.id.usernameInput);
        TextInputEditText passwordInput = view.findViewById(R.id.passwordInput);
        TextView errorText = view.findViewById(R.id.errorText);
        MaterialButton loginButton = view.findViewById(R.id.loginButton);
        MaterialButton registerButton = view.findViewById(R.id.needAccountButton);

        loginButton.setOnClickListener(v -> {
            String username = valueOf(usernameInput);
            String password = valueOf(passwordInput);
            if (viewModel.login(username, password)) {
                errorText.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();
                ((AppNavigator) requireActivity()).openHome();
            } else {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Please enter a username and password.");
            }
        });

        registerButton.setOnClickListener(v -> ((AppNavigator) requireActivity()).openRegister());
    }

    private String valueOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
